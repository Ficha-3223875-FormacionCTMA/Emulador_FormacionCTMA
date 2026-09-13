package com.example.miformacionctma.data.local

import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.execSQL

/**
 * Migración 1 → 2: agrega la columna "resuelto" a la tabla "reportes"
 * con valor por defecto false (0), sin borrado destructivo (PA-06).
 *
 * Esquema v1: reportes(id, titulo, descripcion, categoriaId, fechaCreacion)
 * Esquema v2: reportes(id, titulo, descripcion, categoriaId, fechaCreacion, resuelto)
 *
 * En Room 2.7.0+ las migraciones pueden recibir un SQLiteConnection (androidx.sqlite)
 * en lugar del antiguo SupportSQLiteDatabase.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            "ALTER TABLE reportes ADD COLUMN resuelto INTEGER NOT NULL DEFAULT 0"
        )
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            """
            CREATE TABLE IF NOT EXISTS actividades (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                nombre TEXT NOT NULL,
                descripcion TEXT NOT NULL,
                progreso INTEGER NOT NULL,
                estado TEXT NOT NULL,
                fechaCreacion INTEGER NOT NULL
            )
            """.trimIndent()
        )
    }
}
