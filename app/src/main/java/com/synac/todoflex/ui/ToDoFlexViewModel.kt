package com.synac.todoflex.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.synac.todoflex.model.Task
import com.synac.todoflex.repository.ToDoFlexRepository
import kotlinx.coroutines.launch
import java.time.LocalDate

class ToDoFlexViewModel(
    private val repository: ToDoFlexRepository
): ViewModel() {

    var selectedDate = mutableStateOf(LocalDate.now())
    val allTask: LiveData<List<Task>> = repository.allTask.asLiveData()

    fun addTask(
        taskTitle: String,
        taskDescription: String,
        startTime: String,
        endTime: String
    ){
        viewModelScope.launch {
            val task = Task(
                taskTitle = taskTitle,
                taskDescription = taskDescription,
                taskDate = selectedDate.value.toString(),
                taskPriority = 1,
                isDone = false,
                startTime = startTime,
                endTime = endTime
            )
            repository.insert(task)
        }
    }

    fun updatePriority(tasks: List<Task>) {
        viewModelScope.launch {
            tasks.forEachIndexed { index, task ->
                if(task.taskPriority != index){
                    repository.insert(task.copy(taskPriority = index))
                }
            }
        }
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            repository.insert(task) // Room's OnConflictStrategy.REPLACE or upsert handles updates
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            repository.delete(task)
        }
    }
}

class TaskViewModelFactory(
    private val repository: ToDoFlexRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ToDoFlexViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return ToDoFlexViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}