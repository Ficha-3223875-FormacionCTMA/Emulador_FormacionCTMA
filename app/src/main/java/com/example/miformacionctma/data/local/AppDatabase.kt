package com.example.miformacionctma.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [ActividadEntity::class], // Usa la entidad de la base de datos, NO el modelo UI
    version = 1,
    exportSchema = true // Exigido por la Guía 6 para auditoría de cambios
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun actividadDao(): ActividadDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "mi_formacion_ctma.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}