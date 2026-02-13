package com.synac.todoflex.ui.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.synac.todoflex.ui.theme.AccentCyan
import com.synac.todoflex.ui.theme.TextWhite
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarStrip(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val today = remember { LocalDate.now() }

    //Generate next 14 days so user can plan ahead
    val days = remember {
        (0..6).map { today.plusDays(it.toLong())}
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 24.dp, bottom = 16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(days) { date ->
            val isSelected = date.isEqual(selectedDate)

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(if(isSelected) Color(0xFF333333) else Color.Transparent)
                    .clickable{ onDateSelected(date) } //handle Click
                    .padding(vertical = 12.dp, horizontal = 16.dp)
            ) {
                Text(
                    text = date.dayOfWeek.getDisplayName(TextStyle.NARROW, Locale.getDefault()),
                    color = if(isSelected) TextWhite else Color.Gray,
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = date.dayOfMonth.toString(),
                    color = if(isSelected) AccentCyan else Color.Gray,
                    fontWeight = if(isSelected) FontWeight.Bold else FontWeight.Normal,
                    fontSize = 18.sp
                )

                if(isSelected) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .size(4.dp)
                            .background(AccentCyan, CircleShape)
                    )
                }
            }
        }
    }
}