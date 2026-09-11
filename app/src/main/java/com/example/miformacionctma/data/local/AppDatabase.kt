package com.example.miformacionctma.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.miformacionctma.model.ActividadFormativa

@Database(entities = [ActividadFormativa::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun actividadDao(): ActividadDao
}