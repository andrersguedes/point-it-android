package com.andrersguedes.pointit.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "estimate_history")
data class EstimateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val taskTitle: String,
    val userName: String,
    val finalAverage: Float,
    val timestamp: Long = System.currentTimeMillis()
)