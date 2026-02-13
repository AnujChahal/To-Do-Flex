package com.synac.todoflex.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.synac.todoflex.model.Task
import com.synac.todoflex.ui.ReorderableLazyColumn2
import com.synac.todoflex.ui.ToDoFlexViewModel
import com.synac.todoflex.ui.navigation.AddTask
import com.synac.todoflex.ui.screens.components.CalendarStrip
import com.synac.todoflex.ui.screens.components.HeaderSection
import com.synac.todoflex.ui.screens.components.TaskCard
import com.synac.todoflex.ui.theme.AccentCyan
import com.synac.todoflex.ui.theme.DarkBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToDoFlexScreen(
    viewModel: ToDoFlexViewModel,
    navController: NavHostController
) {
    val dbTasks by viewModel.allTask.observeAsState(initial = emptyList())
    val uiTasks = remember { mutableStateListOf<Task>() }

    //observe the selected date
    val selectedDate by viewModel.selectedDate

    LaunchedEffect(dbTasks, selectedDate) {
        uiTasks.clear()
        uiTasks.addAll(
            dbTasks
                .filter { it.taskDate == selectedDate.toString() }
                .sortedBy { it.taskPriority }
        )
    }

    Scaffold(
        containerColor = DarkBackground,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(AddTask)},
                containerColor = AccentCyan,
                contentColor = Color.Black,
                shape = CircleShape
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Task")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(DarkBackground)
        ) {
            HeaderSection()
            CalendarStrip(
                selectedDate = selectedDate,
                onDateSelected = { newDate -> viewModel.selectedDate.value  = newDate }
            )
            if(uiTasks.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                    Text("No tasks for this day", color = Color.Gray)
                }
            } else {
                ReorderableLazyColumn2(
                    items = uiTasks,
                    key = { task -> task.taskId ?: 0},
                    onDragEnd = { updatedList -> viewModel.updatePriority(updatedList) },
                    modifier = Modifier.weight(1f)
                ) { task, isDragging ->
                    val dismissState = rememberSwipeToDismissBoxState(
                        confirmValueChange = {
                            if(it == SwipeToDismissBoxValue.EndToStart) {
                                viewModel.deleteTask(task)
                                uiTasks.remove(task)
                                true
                            } else false
                        }
                    )

                    SwipeToDismissBox(
                        state = dismissState,
                        backgroundContent = {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color.Transparent)
                                    .padding(horizontal = 24.dp),
                                contentAlignment = Alignment.CenterEnd
                            ){
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete Task",
                                    tint = Color.Red,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        },
                        content = {
                            TaskCard(
                                task = task,
                                isDragging = isDragging,
                                onCheckedChange = { isDone ->
                                    val updatedTask = task.copy(isDone = isDone)
                                    viewModel.updateTask(updatedTask)
                                    val index = uiTasks.indexOfFirst{it.taskId == task.taskId}
                                    if (index != -1) {
                                        uiTasks[index] = updatedTask
                                    }
                                }
                            )
                        }
                    )
                }
            }

        }
    }
}