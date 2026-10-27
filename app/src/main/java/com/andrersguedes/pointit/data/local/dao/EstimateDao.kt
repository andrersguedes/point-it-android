package com.andrersguedes.pointit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.andrersguedes.pointit.data.local.entity.EstimateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EstimateDao {

    @Query("SELECT * FROM estimate_history ORDER BY timestamp DESC")
    fun getAllEstimates(): Flow<List<EstimateEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstimate(estimate: EstimateEntity)

    @Query("DELETE FROM estimate_history")
    suspend fun clearHistory()

    @Query("SELECT * FROM estimate_history WHERE id = :estimateId")
    suspend fun getEstimateById(estimateId: Long): EstimateEntity?

}