package com.example.notepadmvvm.mainScreen.view


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.SuggestionChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp


@Composable
fun FilterButtons(
    filters: List<String>,
    selectedFilter: MutableState<String>,
    onFilterSelected: (String) -> Unit,
    criticalColor: Color,
    importantColor: Color,
    normalColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        filters.forEach { filter ->
            val buttonColor = when (filter) {
                "Critical" -> criticalColor
                "Important" -> importantColor
                "Normal" -> normalColor
                else -> MaterialTheme.colorScheme.primary
            }

            val isSelected = selectedFilter.value == filter
            val backgroundColor = if (isSelected) buttonColor else Color.Transparent
            val textColor =
                if (isSelected) MaterialTheme.colorScheme.surface else buttonColor
            val borderColor =
                if (isSelected) MaterialTheme.colorScheme.onSurface else buttonColor

            SuggestionChip(
                onClick = {
                    selectedFilter.value = filter
                    onFilterSelected(filter) // Filtre değiştiğinde geri çağırma
                },
                colors = SuggestionChipDefaults.suggestionChipColors(
                    containerColor = backgroundColor
                ),
                label = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(
                            text = filter,
                            color = textColor,
                            modifier = Modifier.align(Alignment.Center),
                            maxLines = 1,
                            overflow = TextOverflow.Visible
                        )
                    }
                },
                modifier = Modifier
                    .height(40.dp)
                    .weight(1f),
                border = BorderStroke(2.dp, borderColor),
                shape = RoundedCornerShape(26.dp)
            )
        }
    }
}
