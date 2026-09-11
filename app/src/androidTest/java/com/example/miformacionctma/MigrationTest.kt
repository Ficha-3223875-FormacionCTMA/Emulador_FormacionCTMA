package com.example.miformacionctma

import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.driver.AndroidSQLiteDriver
import androidx.test.platform.app.InstrumentationRegistry
import com.example.miformacionctma.data.local.AppDatabase
import com.example.miformacionctma.data.local.MIGRATION_1_2
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Prueba de migración 1 → 2 (punto 9 y caso PA-06 de la guía): crea la
 * base en esquema v1, inserta un reporte, migra a v2 y verifica que el
 * dato se conserva y que "resuelto" nace en false.
 */
class MigrationTest {

    private val instrumentation = InstrumentationRegistry.getInstrumentation()
    private val nombreBaseDatosPrueba = "migration-test-reportactma"

    @get:Rule
    val helper = MigrationTestHelper(
        instrumentation = instrumentation,
        databaseClass = AppDatabase::class,
        driver = AndroidSQLiteDriver(),
        file = instrumentation.targetContext.getDatabasePath(nombreBaseDatosPrueba),
    )

    @Test
    fun migrar1a2_conservaReporteYResueltoIniciaEnFalse() = runTest {
        // Esquema v1: sin la columna "resuelto".
        val conexionV1 = helper.createDatabase(1)
        conexionV1.execSQL(
            """
            INSERT INTO reportes (id, titulo, descripcion, categoriaId, fechaCreacion)
            VALUES (1, 'Fuga de aceite', 'Motor principal', NULL, 1690000000000)
            """.trimIndent()
        )
        conexionV1.close()

        // Migra a v2 aplicando MIGRATION_1_2 y valida el esquema resultante.
        val conexionV2 = helper.runMigrationsAndValidate(2, listOf(MIGRATION_1_2))
        conexionV2.prepare("SELECT titulo, resuelto FROM reportes WHERE id = 1").use { stmt ->
            assertTrue(stmt.step())
            assertEquals("Fuga de aceite", stmt.getText(0))
            assertEquals(0L, stmt.getLong(1))
        }
        conexionV2.close()
    }
}