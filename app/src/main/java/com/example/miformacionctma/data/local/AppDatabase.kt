package com.example.miformacionctma.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.miformacionctma.data.local.entity.EvidenciaEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [ActividadEntity::class, EvidenciaEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun actividadDao(): ActividadDao
    abstract fun evidenciaDao(): EvidenciaDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "miformacion_database"
                )
                    .addCallback(DatabaseCallback()) // <-- Añadimos el callback de precarga inicial
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Callback para insertar las 10 actividades por defecto la primera vez que se crea la BD
        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        poblarDatosIniciales(database.actividadDao())
                    }
                }
            }
        }

        suspend fun poblarDatosIniciales(dao: ActividadDao) {
            val actividadesIniciales = listOf(
                ActividadEntity(id = 1, titulo = "Manifiesto Ágil", descripcion = "", competencia = "", fechaEntrega = "11 de agosto", estado = "Completada", progreso = 100),
                ActividadEntity(id = 2, titulo = "Valores del Manifiesto Ágil", descripcion = "", competencia = "", fechaEntrega = "12 de agosto", estado = "Completada", progreso = 100),
                ActividadEntity(id = 3, titulo = "Principios Ágiles", descripcion = "", competencia = "", fechaEntrega = "13 de agosto", estado = "Completada", progreso = 100),
                ActividadEntity(id = 4, titulo = "Introducción a Scrum", descripcion = "", competencia = "", fechaEntrega = "14 de agosto", estado = "En proceso", progreso = 75),
                ActividadEntity(id = 5, titulo = "Roles de Scrum", descripcion = "", competencia = "", fechaEntrega = "15 de agosto", estado = "En proceso", progreso = 60),
                ActividadEntity(id = 6, titulo = "Artefactos de Scrum", descripcion = "", competencia = "", fechaEntrega = "16 de agosto", estado = "En proceso", progreso = 50),
                ActividadEntity(id = 7, titulo = "Pruebas de software", descripcion = "", competencia = "", fechaEntrega = "17 de agosto", estado = "Pendiente", progreso = 0),
                ActividadEntity(id = 8, titulo = "Tipos de pruebas", descripcion = "", competencia = "", fechaEntrega = "18 de agosto", estado = "Pendiente", progreso = 0),
                ActividadEntity(id = 9, titulo = "Jetpack Compose", descripcion = "", competencia = "", fechaEntrega = "19 de agosto", estado = "Pendiente", progreso = 0),
                ActividadEntity(id = 10, titulo = "Proyecto Mi Formación CTMA", descripcion = "", competencia = "", fechaEntrega = "20 de agosto", estado = "Pendiente", progreso = 0)
            )
            dao.insertarTodas(actividadesIniciales)
        }
    }
}