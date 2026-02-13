package com.synac.todoflex.entity

import androidx.room.Database
import androidx.room.RoomDatabase
import com.synac.todoflex.dao.ToDoFlexDao
import com.synac.todoflex.model.Task

@Database(
    entities = [Task::class],
    version = 1,
    exportSchema = false
)
abstract class TaskDatabase : RoomDatabase(){

    abstract fun taskDao(): ToDoFlexDao
}