package com.example.miformacionctma.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.example.miformacionctma.data.local.dao.ActividadDao
import com.example.miformacionctma.data.local.dao.CategoriaDao
import com.example.miformacionctma.data.local.dao.ReporteDao
import com.example.miformacionctma.data.local.entity.ActividadEntity
import com.example.miformacionctma.data.local.entity.CategoriaEntity
import com.example.miformacionctma.data.local.entity.ReporteEntity
import kotlinx.coroutines.Dispatchers

/**
 * Punto de entrada de Room. Solo debe existir una instancia por proceso
 * (ver AppContainer): evita reabrir el archivo de base de datos y
 * garantiza que todas las capas observen la misma fuente única de verdad.
 */
@Database(
    entities = [ReporteEntity::class, CategoriaEntity::class, ActividadEntity::class],
    version = 3,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun reporteDao(): ReporteDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun actividadDao(): ActividadDao

    companion object {
        private const val NOMBRE_BASE_DATOS = "miformacionctma.db"

        @Volatile
        private var instancia: AppDatabase? = null

        fun obtener(context: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: construir(context).also { instancia = it }
            }

        private fun construir(context: Context): AppDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                NOMBRE_BASE_DATOS
            )
                // Room 3.0 requiere un SQLiteDriver explícito; usamos el
                // driver empaquetado para no depender de la versión de
                // SQLite del dispositivo.
                .setDriver(BundledSQLiteDriver())
                .setQueryCoroutineContext(Dispatchers.IO)
                .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                .build()
    }
}