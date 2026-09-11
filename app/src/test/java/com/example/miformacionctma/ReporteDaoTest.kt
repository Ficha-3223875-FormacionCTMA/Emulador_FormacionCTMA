package com.example.miformacionctma

import com.example.miformacionctma.data.local.AppDatabase
import com.example.miformacionctma.data.local.MIGRATION_1_2
import com.example.miformacionctma.data.local.dao.CategoriaDao
import com.example.miformacionctma.data.local.dao.ReporteDao
import com.example.miformacionctma.data.local.entity.CategoriaEntity
import com.example.miformacionctma.data.local.entity.ReporteEntity
import kotlinx.coroutines.flow.first
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas instrumentadas del DAO (punto 9 del laboratorio incremental):
 * CRUD, búsqueda y relación. Se ejecuta en memoria para no dejar
 * artefactos en el dispositivo/emulador.
 */
@RunWith(AndroidJUnit4::class)
class ReporteDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var reporteDao: ReporteDao
    private lateinit var categoriaDao: CategoriaDao

    @Before
    fun crearBaseEnMemoria() {
        val contexto = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(contexto, AppDatabase::class.java)
            .setDriver(BundledSQLiteDriver())
            .addMigrations(MIGRATION_1_2)
            .allowMainThreadQueries()
            .build()
        reporteDao = db.reporteDao()
        categoriaDao = db.categoriaDao()
    }

    @After
    fun cerrarBaseDeDatos() {
        db.close()
    }

    @Test
    fun insertarYLeerReporte_devuelveElMismoReporte() = runTest {
        val categoriaId = categoriaDao.insertar(CategoriaEntity(nombre = "Eléctrico"))
        val id = reporteDao.insertar(
            ReporteEntity(
                titulo = "Fuga en el tablero",
                descripcion = "Se detectó fuga de corriente",
                categoriaId = categoriaId,
                fechaCreacion = 1_000L
            )
        )

        val lista = reporteDao.observarReportes(null, "", "FECHA_DESC").first()

        Assert.assertEquals(1, lista.size)
        Assert.assertEquals(id, lista.first().id)
        Assert.assertEquals("Fuga en el tablero", lista.first().titulo)
        Assert.assertEquals(false, lista.first().resuelto)
    }

    @Test
    fun actualizarReporte_reflejaCambiosSinRecargaManual() = runTest {
        val id = reporteDao.insertar(
            ReporteEntity(
                titulo = "Sensor caído",
                descripcion = "Revisar cableado",
                categoriaId = null,
                fechaCreacion = 1_000L
            )
        )
        val original = reporteDao.observarPorId(id).first()!!
        reporteDao.actualizar(original.copy(resuelto = true))

        val actualizado = reporteDao.observarPorId(id).first()
        Assert.assertTrue(actualizado!!.resuelto)
    }

    @Test
    fun eliminarReporte_loQuitaDeLaLista() = runTest {
        val id = reporteDao.insertar(
            ReporteEntity(
                titulo = "Ruido en compresor",
                descripcion = "Ruido intermitente",
                categoriaId = null,
                fechaCreacion = 1_000L
            )
        )
        val reporte = reporteDao.observarPorId(id).first()!!
        reporteDao.eliminar(reporte)

        Assert.assertEquals(0, reporteDao.contar())
    }

    @Test
    fun buscarPorTexto_encuentraCoincidenciasEnTituloYDescripcion() = runTest {
        reporteDao.insertar(
            ReporteEntity(1, "Fuga de aceite", "Motor principal", null, 1_000L)
        )
        reporteDao.insertar(
            ReporteEntity(2, "Falla de software", "La app se cierra sola", null, 2_000L)
        )

        val resultado = reporteDao.buscar("fuga")

        Assert.assertEquals(1, resultado.size)
        Assert.assertEquals("Fuga de aceite", resultado.first().titulo)
    }

    @Test
    fun consultaTransaccionalConCategoria_devuelveCategoriaAsociada() = runTest {
        val categoriaId = categoriaDao.insertar(CategoriaEntity(nombre = "Mecánico"))
        val reporteId = reporteDao.insertar(
            ReporteEntity(
                titulo = "Correa desgastada",
                descripcion = "Cambiar antes de 500 h",
                categoriaId = categoriaId,
                fechaCreacion = 1_000L
            )
        )

        val resultado = reporteDao.obtenerConCategoria(reporteId)

        Assert.assertNotNull(resultado)
        Assert.assertEquals("Mecánico", resultado!!.categoria?.nombre)
    }

    @Test
    fun filtrarPorCategoria_devuelveSoloCoincidencias() = runTest {
        val electrico = categoriaDao.insertar(CategoriaEntity(nombre = "Eléctrico"))
        val mecanico = categoriaDao.insertar(CategoriaEntity(nombre = "Mecánico"))
        reporteDao.insertar(
            ReporteEntity(
                titulo = "Corto circuito",
                descripcion = "",
                categoriaId = electrico,
                fechaCreacion = 1_000L
            )
        )
        reporteDao.insertar(
            ReporteEntity(
                titulo = "Correa rota",
                descripcion = "",
                categoriaId = mecanico,
                fechaCreacion = 2_000L
            )
        )

        val soloElectricos = reporteDao.observarReportes(electrico, "", "FECHA_DESC").first()

        Assert.assertEquals(1, soloElectricos.size)
        Assert.assertEquals("Corto circuito", soloElectricos.first().titulo)
    }
}