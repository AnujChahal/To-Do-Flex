package com.synac.todoflex.ui.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.synac.todoflex.ui.theme.TextWhite
import java.time.LocalDate

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, top = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Today's",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
            Text(
                text = "Schedule",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = "${LocalDate.now()}", color = Color.Gray)
        }
    }
}