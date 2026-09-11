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
 * En Room 3.0 las migraciones reciben un SQLiteConnection (androidx.sqlite)
 * en lugar del antiguo SupportSQLiteDatabase, y migrate() es suspend.
 */
val MIGRATION_1_2 = object : Migration(1, 2) {
    override suspend fun migrate(connection: SQLiteConnection) {
        connection.execSQL(
            "ALTER TABLE reportes ADD COLUMN resuelto INTEGER NOT NULL DEFAULT 0"
        )
    }
}