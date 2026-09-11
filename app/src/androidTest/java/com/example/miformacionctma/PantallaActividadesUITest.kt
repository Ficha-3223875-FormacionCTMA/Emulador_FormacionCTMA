package com.example.miformacionctma

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import com.example.miformacionctma.model.ActividadFormativa
import com.example.miformacionctma.ui.screens.PantallaActividades
import com.example.miformacionctma.ui.theme.MiFormacionCTMATheme
import org.junit.Rule
import org.junit.Test

class PantallaActividadesUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun alSeleccionarFiltro_soloAparecenActividadesCorrespondientes() {
        val actividades = listOf(
            ActividadFormativa(1, "Kotlin", "Desc", "Hoy", "Completada", 100),
            ActividadFormativa(2, "Java", "Desc", "Mañana", "Pendiente", 0)
        )

        composeTestRule.setContent {
            MiFormacionCTMATheme {
                // Simulamos el estado del filtro
                PantallaActividades(
                    actividades = actividades.filter { it.estado == "Completada" },
                    filtroSeleccionado = "Completadas"
                )
            }
        }

        // Entonces: Solo debe aparecer la de Kotlin
        composeTestRule.onNodeWithText("Kotlin").assertExists()
        composeTestRule.onNodeWithText("Java").assertDoesNotExist()
        composeTestRule.onNodeWithText("Mostrando 1 actividad(es)").assertExists()
    }

    @Test
    fun siNoHayActividades_apareceMensajeVacio() {
        composeTestRule.setContent {
            MiFormacionCTMATheme {
                PantallaActividades(actividades = emptyList())
            }
        }

        composeTestRule.onNodeWithText("No hay actividades").assertExists()
        composeTestRule.onNodeWithText("Todavía no tienes actividades registradas.").assertExists()
    }

    @Test
    fun tarjetaActividad_muestraTodosLosDatos() {
        val actividad = ActividadFormativa(
            id = 2,
            titulo = "Programación en Kotlin",
            descripcion = "Variables, funciones, colecciones y clases en Kotlin.",
            fecha = "12 de agosto",
            estado = "Completada",
            progreso = 100
        )

        composeTestRule.setContent {
            MiFormacionCTMATheme {
                PantallaActividades(actividades = listOf(actividad))
            }
        }

        composeTestRule.onNodeWithText("Programación en Kotlin").assertExists()
        composeTestRule.onNodeWithText("Variables, funciones, colecciones y clases en Kotlin.").assertExists()
        composeTestRule.onNodeWithText("Fecha: 12 de agosto").assertExists()
        composeTestRule.onNodeWithText("Completada").assertExists()
        composeTestRule.onNodeWithText("Progreso: 100%").assertExists()
    }
}
