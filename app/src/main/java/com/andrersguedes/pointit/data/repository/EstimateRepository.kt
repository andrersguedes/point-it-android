package com.andrersguedes.pointit.data.repository

import com.andrersguedes.pointit.data.local.dao.EstimateDao
import com.andrersguedes.pointit.data.local.entity.EstimateEntity

class EstimateRepository(private val estimateDao: EstimateDao) {

    fun getAllEstimates() = estimateDao.getAllEstimates()

    suspend fun insertEstimate(taskTitle: String, userName: String, finalAverage: Float) {
        val entity = EstimateEntity(
            taskTitle = taskTitle,
            userName = userName,
            finalAverage = finalAverage
        )
        estimateDao.insertEstimate(entity)
    }

    suspend fun clearHistory() = estimateDao.clearHistory()

}