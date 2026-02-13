package com.synac.todoflex.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int? = null,
    val taskTitle: String,
    val taskDescription: String,
    val taskDate: String,
    val taskPriority: Int,
    val isDone: Boolean = false,
    val startTime: String,
    val endTime: String
)
