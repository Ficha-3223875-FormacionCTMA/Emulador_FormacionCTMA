package com.example.miformacionctma

import com.example.miformacionctma.model.ActividadFormativa
import org.junit.Assert.assertEquals
import org.junit.Test

class ActividadFormativaTest {

    @Test
    fun `verificar integridad de datos de Programacion en Kotlin`() {
        // Dado: la actividad "Programación en Kotlin"
        val actividad = ActividadFormativa(
            id = 2,
            titulo = "Programación en Kotlin",
            descripcion = "Variables, funciones, colecciones y clases en Kotlin.",
            fecha = "12 de agosto",
            estado = "Completada",
            progreso = 100
        )

        // Cuando: se accede a sus datos (en la tarjeta)
        // Entonces: debe aparecer su título, descripción, fecha, estado y progreso
        assertEquals("Programación en Kotlin", actividad.titulo)
        assertEquals("Variables, funciones, colecciones y clases en Kotlin.", actividad.descripcion)
        assertEquals("12 de agosto", actividad.fecha)
        assertEquals("Completada", actividad.estado)
        assertEquals(100, actividad.progreso)
    }
}
