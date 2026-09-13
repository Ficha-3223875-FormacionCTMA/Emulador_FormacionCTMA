package com.example.miformacionctma

import android.content.Context
import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.miformacionctma.data.local.AppDatabase
import com.example.miformacionctma.data.local.dao.ActividadDao
import com.example.miformacionctma.data.local.entity.ActividadEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActividadDaoTest {

    private lateinit var db: AppDatabase
    private lateinit var dao: ActividadDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .setDriver(BundledSQLiteDriver())
            .allowMainThreadQueries()
            .build()
        dao = db.actividadDao()
    }

    @After
    fun teardown() {
        db.close()
    }

    @Test
    fun contarActividades_devuelveElTotalCorrecto() = runTest {
        repeat(10) { i ->
            dao.insertar(crearActividad(nombre = "Actividad $i"))
        }
        val total = dao.contar()
        assertEquals(10, total)
    }

    @Test
    fun actualizarActividad_mantieneLaIntegridad() = runTest {
        dao.insertar(crearActividad(nombre = "Original", progreso = 0))
        val original = dao.observarTodas().first().first()
        dao.actualizar(original.copy(progreso = 50))
        val actualizada = dao.observarTodas().first().first()
        assertEquals(50, actualizada.progreso)
    }

    @Test
    fun ordenarPorProgreso_devuelveElOrdenEsperado() = runTest {
        dao.insertar(crearActividad(nombre = "Bajo", progreso = 0))
        dao.insertar(crearActividad(nombre = "Alto", progreso = 100))

        val desc = dao.observarOrdenadasPorProgresoDesc().first()
        assertEquals(100, desc[0].progreso)
        assertEquals(0, desc[1].progreso)
    }

    private fun crearActividad(
        nombre: String,
        progreso: Int = 0,
        estado: String = "PENDIENTE"
    ) = ActividadEntity(
        nombre = nombre,
        descripcion = "Desc",
        progreso = progreso,
        estado = estado,
        fechaCreacion = System.currentTimeMillis()
    )
}
