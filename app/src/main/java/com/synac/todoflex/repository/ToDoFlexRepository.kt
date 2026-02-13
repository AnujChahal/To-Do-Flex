package com.synac.todoflex.repository

import com.synac.todoflex.dao.ToDoFlexDao
import com.synac.todoflex.model.Task
import kotlinx.coroutines.flow.Flow

class ToDoFlexRepository(
    private val toDoFlexDao: ToDoFlexDao
) {
    val allTask: Flow<List<Task>> = toDoFlexDao.getAllTasks()

    suspend fun insert(task: Task){
        toDoFlexDao.upsertTask(task)
    }

    suspend fun delete(task: Task){
        toDoFlexDao.deleteTask(task)
    }
}