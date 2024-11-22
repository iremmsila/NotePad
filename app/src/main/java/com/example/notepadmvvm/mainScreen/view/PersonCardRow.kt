package com.example.notepadmvvm.mainScreen.view

import androidx.compose.runtime.Composable
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.notepadmvvm.R
import com.example.notepadmvvm.mainScreen.model.PersonDetailEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.material3.CardDefaults

@Composable
fun EditableCard(
    person: PersonDetailEntity, // Person modelini kullanın
    navController: NavController,
    cardBackgroundColor: Color,
    screenHeight: Dp,
    screenWidth: Dp,
    daysLeft: Long
) {
    Box(
        modifier = Modifier
            .height(screenHeight * 0.15f)
            .width(screenWidth * 0.35f)
            .padding(screenWidth * 0.05f)
            .background(
                color = Color.Black.copy(alpha = 0.01f), // Gölge etkisi için bir arka plan
                shape = RoundedCornerShape(15.dp)
            )
            .padding(top = 4.dp, start = 4.dp) // Gölgeyi biraz aşağı ve sağa kaydır
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .clickable {
                    CoroutineScope(Dispatchers.Main).launch {
                        // ID'yi geçerek düzenleme sayfasına yönlendirme
                        navController.navigate("edit_plan/${person._id}")
                    }
                },
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.elevatedCardElevation(8.dp) // Kartın yukarıda durma etkisi
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Box(
                    modifier = Modifier
                        .height(80.dp)
                        .width(8.dp)
                        .background(color = cardBackgroundColor)
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 12.dp),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "${person.namee}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                if (daysLeft < 0) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = stringResource(R.string.past_date_warning),
                        modifier = Modifier.size(15.dp),
                        tint = Color.Red // Uyarı ikonu için kırmızı renk
                    )
                }
            }
        }
    }
}
