package com.synac.todoflex.ui.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.synac.todoflex.model.Task
import com.synac.todoflex.ui.theme.AccentCyan
import com.synac.todoflex.ui.theme.CardBackground
import com.synac.todoflex.ui.theme.TextGray
import com.synac.todoflex.ui.theme.TextWhite

@Composable
fun TaskCard(
    task: Task,
    isDragging: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.width(70.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = task.startTime,
                color = if(task.isDone) Color.Gray.copy(alpha = 0.5f) else TextGray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(70.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = task.endTime,
                color = if(task.isDone) Color.Gray.copy(alpha = 0.5f) else TextGray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
            )
        }
        Card(
            modifier = Modifier
                .weight(1f)
                .heightIn(min = 110.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = if(isDragging) CardBackground.copy(alpha = 0.8f) else CardBackground
            ),
            elevation = CardDefaults.cardElevation(if (isDragging) 8.dp else 0.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = task.taskTitle,
                        color = if (task.isDone) TextWhite.copy(alpha = 0.4f) else TextWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textDecoration = if(task.isDone) TextDecoration.LineThrough else null
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = task.taskDescription,
                        color = if (task.isDone) TextGray.copy(alpha = 0.4f) else TextGray,
                        fontSize = 16.sp,
                        lineHeight = 22.sp,
                        maxLines = 2,
                        textDecoration = if(task.isDone) TextDecoration.LineThrough else null
                    )
                }

                //Checkbox Logic
                Spacer(modifier = Modifier.width(12.dp))
                IconButton(
                    onClick = {onCheckedChange(!task.isDone)},
                    modifier = Modifier.size(32.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if(task.isDone) AccentCyan else Color.Transparent,
                        border = if(task.isDone) null else BorderStroke(2.dp, AccentCyan),
                        modifier = Modifier.size(28.dp)
                    ) {
                        if(task.isDone){
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = Color.Black,
                                modifier = Modifier.padding(6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}