package com.andrersguedes.pointit.data.local.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andrersguedes.pointit.data.local.entity.EstimateEntity
import kotlinx.coroutines.flow.Flow

interface EstimateDao {

    @Query("SELECT * FROM estimate_history ORDER BY timestamp DESC")
    fun getAllEstimates(): Flow<List<EstimateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstimate(estimate: EstimateEntity)

    @Query("DELETE FROM estimate_history")
    suspend fun clearHistory()

}