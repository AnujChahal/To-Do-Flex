package com.synac.todoflex.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.synac.todoflex.model.Task
import kotlinx.coroutines.flow.Flow

@Dao
interface ToDoFlexDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun upsertTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<Task>>

//    @Query("SELECT * FROM task ORDER BY taskPriority ASC")
//    fun getTasksOrderedByPriority(): Flow<List<Task>>

//    @Query("SELECT * FROM task ORDER BY taskTitle ASC")
//    fun getTasksOrderedByTitle(): Flow<List<Task>>

//    @Query("SELECT * FROM task ORDER BY taskDate ASC")
//    fun getTasksOrderedByDate(): Flow<List<Task>>

//    @Query("SELECT * FROM task ORDER BY taskDescription ASC")
//    fun getTasksOrderedByDescription(): Flow<List<Task>>
}