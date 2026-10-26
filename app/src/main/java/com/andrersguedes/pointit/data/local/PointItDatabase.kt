package com.andrersguedes.pointit.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.andrersguedes.pointit.data.local.dao.EstimateDao
import com.andrersguedes.pointit.data.local.entity.EstimateEntity

@Database(entities = [EstimateEntity::class], version = 1, exportSchema = false)
abstract class PointItDatabase : RoomDatabase() {

    abstract fun estimateDao(): EstimateDao

    companion object {

        const val DATABASE_NAME = "point_it_database.db"

        @Volatile
        private var INSTANCE: PointItDatabase? = null

        fun getDatabase(context: Context): PointItDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PointItDatabase::class.java,
                    DATABASE_NAME
                    ).build()
                INSTANCE = instance
                instance
            }
        }

    }

}