package com.example.myapplication.ui_items

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.myapplication.R
import com.example.myapplication.helper.PersonDetailEntity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissItem(
    item: PersonDetailEntity,
    onRemove: (String) -> Unit,  // Callback to remove item
    onRestore: (PersonDetailEntity) -> Unit,  // Callback to restore deleted item
    onEdit: (String) -> Unit,  // Callback to edit item
    modifier: Modifier = Modifier,
    snackbarHostState: SnackbarHostState,  // SnackbarHostState for undo feature
    content: @Composable () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var showDialog by remember { mutableStateOf(false) }
    var deletedItem by remember { mutableStateOf<PersonDetailEntity?>(null) }
    val scope = rememberCoroutineScope()

// Handle swipe state
    val swipeToDismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { state ->
            when (state) {
                SwipeToDismissBoxValue.EndToStart -> {
                    showDialog = true
                    false
                }

                SwipeToDismissBoxValue.StartToEnd -> {
                    onEdit((item._id ?: "").toString())  // Trigger edit
                    false
                }

                else -> false
            }
        }
    )

// Confirmation dialog for deletion
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = stringResource(R.string.confirm_deletion)) },
            text = { Text(stringResource(R.string.are_you_sure_you_want_to_delete_this_item)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        deletedItem = item  // Set the deleted item

                        // Trigger removal from the list
                        onRemove(((item._id ?: "").toString()))

                        // Show Snackbar for undo option
                        /*  scope.launch {
                              val snackbarResult = snackbarHostState.showSnackbar(
                                  message = "Item deleted",
                                  actionLabel = "Undo",
                                  duration = SnackbarDuration.Indefinite // Keeps on screen until interacted
                              )

                              if (snackbarResult == SnackbarResult.ActionPerformed) {
                                  onRestore(deletedItem!!)
                              }

                              deletedItem = null
                          }*/

                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    coroutineScope.launch {
                        swipeToDismissState.reset()
                    }
                }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }


// Swipe to dismiss box
    SwipeToDismissBox(
        state = swipeToDismissState,
        backgroundContent = {
            val backgroundColor by animateColorAsState(
                targetValue = when (swipeToDismissState.targetValue) {
                    SwipeToDismissBoxValue.StartToEnd -> Color(0xBD8BD54B)
                    SwipeToDismissBoxValue.EndToStart -> Color(0xB3FC3131)
                    else -> MaterialTheme.colorScheme.onBackground
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(backgroundColor)
                    .padding(horizontal = 16.dp)
            ) {
                if (swipeToDismissState.targetValue == SwipeToDismissBoxValue.StartToEnd) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .size(36.dp)
                    )
                }
                if (swipeToDismissState.targetValue == SwipeToDismissBoxValue.EndToStart) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .size(36.dp)
                    )
                }
            }
        },
        modifier = modifier
    ) {
        content()
    }
}