package com.example.miformacionctma.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.miformacionctma.data.local.dao.ReporteDao
import com.example.miformacionctma.data.local.entity.CategoriaEntity
import com.example.miformacionctma.data.local.entity.ReporteEntity

@Database(
    entities = [ReporteEntity::class, CategoriaEntity::class],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reporteDao(): ReporteDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun obtenerInstancia(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instancia = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reportactma.db"
                ).build()
                INSTANCE = instancia
                instancia
            }
        }
    }
}