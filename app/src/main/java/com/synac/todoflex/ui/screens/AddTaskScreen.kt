package com.synac.todoflex.ui.screens

import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.synac.todoflex.ui.ToDoFlexViewModel
import com.synac.todoflex.ui.navigation.Home
import com.synac.todoflex.ui.theme.AccentCyan
import com.synac.todoflex.ui.theme.CardBackground
import com.synac.todoflex.ui.theme.DarkBackground
import com.synac.todoflex.ui.theme.TextGray
import com.synac.todoflex.ui.theme.TextWhite
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskScreen(
    viewModel: ToDoFlexViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    var taskTitle by remember { mutableStateOf("") }
    var taskDescription by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("09:00 AM") }
    var endTime by remember { mutableStateOf("10:00 AM") }

    //state to control visibility of the picker
    var showStartPicker by remember { mutableStateOf(false) }
    var showEndPicker by remember { mutableStateOf(false) }

    //Helper state for the time picker logic
    val timePickerState = rememberTimePickerState(
        initialHour = 9,
        initialMinute = 0,
        is24Hour = false
    )

    // Function to format time from the picker state
    fun formatTime(state: TimePickerState): String {
        val hour = state.hour
        val minute = state.minute
        val amPm = if (hour < 12) "AM" else "PM"
        val displayHour = if (hour == 0) 12 else if (hour > 12) hour - 12 else hour
        return String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, minute, amPm)
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                modifier = Modifier.height(48.dp),
                title = {
                    Text(
                        text = "New Task",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextWhite
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .padding(16.dp),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Title",
                color = TextGray,
                fontSize = 14.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = taskTitle,
                onValueChange = { taskTitle = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    cursorColor = AccentCyan,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.padding(24.dp))
            Text("Description", color = TextGray, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = taskDescription,
                onValueChange = { taskDescription = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .clip(RoundedCornerShape(8.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = CardBackground,
                    unfocusedContainerColor = CardBackground,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    cursorColor = AccentCyan,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
            Spacer(modifier = Modifier.padding(24.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Start Time",
                        color = TextGray,
                        fontSize = 8.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CardBackground)
                            .clickable{ showStartPicker = true }
                            .padding(16.dp)
                    ) {
                        Text(
                            text = startTime,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "End Time",
                        color = TextGray,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(CardBackground)
                            .clickable{ showEndPicker = true }
                            .padding(16.dp)
                    ) {
                        Text(
                            text = endTime,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {
                    if (taskTitle.isNotEmpty()) {
                        viewModel.addTask(taskTitle, taskDescription, startTime, endTime)
                        navController.navigate(Home) {
                            popUpTo(Home) { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Title is required", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentCyan)
            ) {
                Text(
                    text = "Create Task",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    //Dark Themed Time Picker Dialogs
    if(showStartPicker) {
        CustomTimePickerDialog(
            onDismissRequest = { showStartPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    startTime = formatTime(timePickerState)
                    showStartPicker = false
                }) {
                    Text("OK", color = AccentCyan)
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartPicker = false }) {
                    Text("Cancel", color = AccentCyan)
                }
            }
        ) {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = DarkBackground,
                    clockDialSelectedContentColor = Color.Black,
                    clockDialUnselectedContentColor = TextWhite,
                    selectorColor = AccentCyan,
                    containerColor = CardBackground,
                    periodSelectorBorderColor = AccentCyan,
                    periodSelectorSelectedContainerColor = AccentCyan.copy(alpha = 0.5f),
                    periodSelectorUnselectedContainerColor = Color.Transparent,
                    periodSelectorSelectedContentColor = TextWhite,
                    periodSelectorUnselectedContentColor = TextGray,
                    timeSelectorSelectedContainerColor = CardBackground,
                    timeSelectorUnselectedContainerColor = CardBackground,
                    timeSelectorSelectedContentColor = AccentCyan,
                    timeSelectorUnselectedContentColor = TextWhite
                )
            )
        }
    }

    if (showEndPicker) {
        CustomTimePickerDialog(
            onDismissRequest = { showEndPicker = false },
            confirmButton = {
                TextButton(onClick = {
                    endTime = formatTime(timePickerState)
                    showEndPicker = false
                }) {
                    Text("OK", color = AccentCyan)
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndPicker = false }) {
                    Text("Cancel", color = AccentCyan)
                }
            }
        ) {
            TimePicker(
                state = timePickerState,
                colors = TimePickerDefaults.colors(
                    clockDialColor = DarkBackground,
                    clockDialSelectedContentColor = Color.Black,
                    clockDialUnselectedContentColor = TextWhite,
                    selectorColor = AccentCyan,
                    containerColor = CardBackground,
                    periodSelectorBorderColor = AccentCyan,
                    periodSelectorSelectedContainerColor = AccentCyan.copy(alpha = 0.5f),
                    periodSelectorUnselectedContainerColor = Color.Transparent,
                    periodSelectorSelectedContentColor = TextWhite,
                    periodSelectorUnselectedContentColor = TextGray,
                    timeSelectorSelectedContainerColor = CardBackground,
                    timeSelectorUnselectedContainerColor = CardBackground,
                    timeSelectorSelectedContentColor = AccentCyan,
                    timeSelectorUnselectedContentColor = TextWhite
                )
            )
        }
    }
}

@Composable
private fun CustomTimePickerDialog(
   onDismissRequest: () -> Unit,
   confirmButton: @Composable () -> Unit,
   dismissButton: @Composable () -> Unit,
   content: @Composable () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CardBackground,
            tonalElevation = 6.dp,
            modifier = Modifier
                .width(IntrinsicSize.Min)
                .height(IntrinsicSize.Min)
                .background(shape = RoundedCornerShape(24.dp), color = CardBackground)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content()
                Row(
                    modifier = Modifier
                        .height(40.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    dismissButton()
                    confirmButton()
                }
            }
        }
    }
}