package com.example.notepadmvvm.mainScreen.viewModel


import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notepadmvvm.mainScreen.model.PersonDetailEntity
import kotlinx.coroutines.launch

class SwipeToDismissViewModel : ViewModel() {
    private var deletedItem: PersonDetailEntity? = null

    fun removeItem(
        item: PersonDetailEntity,
        onRemove: (String) -> Unit,
        snackbarHostState: SnackbarHostState,
        onRestore: (PersonDetailEntity) -> Unit
    ) {
        deletedItem = item
        onRemove((item._id ?: "").toString())

        // Show Snackbar for undo option
        viewModelScope.launch {
            val snackbarResult = snackbarHostState.showSnackbar(
                message = "Item deleted",
                actionLabel = "Undo"
            )

            if (snackbarResult == SnackbarResult.ActionPerformed) {
                deletedItem?.let { onRestore(it) }
                deletedItem = null
            }
        }
    }

    fun editItem(itemId: String, onEdit: (String) -> Unit) {
        onEdit(itemId)
    }
}
