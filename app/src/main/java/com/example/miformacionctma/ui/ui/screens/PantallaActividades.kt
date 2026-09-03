@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.example.miformacionctma.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.miformacionctma.model.ActividadFormativa
import com.example.miformacionctma.ui.theme.MiFormacionCTMATheme
import com.example.miformacionctma.uii.components.TarjetaActividad


@Composable
fun PantallaActividades(
    actividades: List<ActividadFormativa> = actividadesEjemplo
) {

    var filtroSeleccionado by remember {
        mutableStateOf("Todas")
    }

    val actividadesFiltradas = when (filtroSeleccionado) {

        "Completadas" -> actividades.filter {
            it.estado == "Completada"
        }

        "En proceso" -> actividades.filter {
            it.estado == "En proceso"
        }

        "Pendientes" -> actividades.filter {
            it.estado == "Pendiente"
        }

        else -> actividades
    }

    Scaffold(

        topBar = {

            TopAppBar(
                title = {
                    Text("Mi Formación CTMA")
                }
            )
        }

    ) { paddingValues ->

        BoxWithConstraints(

            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {

            // Guardamos el ancho disponible para decidir
            // qué tipo de diseño mostrar.
            val anchoDisponible = maxWidth

            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                EncabezadoYFiltros(
                    filtroSeleccionado = filtroSeleccionado,
                    onFiltroSeleccionado = {
                        filtroSeleccionado = it
                    },
                    cantidadActividades = actividadesFiltradas.size
                )

                if (actividades.isEmpty()) {

                    EstadoVacio(
                        modifier = Modifier
                            .fillMaxSize()
                    )

                } else {

                    if (anchoDisponible < 600.dp) {

                        // Pantalla compacta:
                        // mostramos una lista vertical.
                        ListaActividades(
                            actividades = actividadesFiltradas,
                            modifier = Modifier.weight(1f)
                        )

                    } else {

                        // Pantalla amplia:
                        // mostramos dos columnas.
                        CuadriculaActividades(
                            actividades = actividadesFiltradas,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}


/**
 * Encabezado y filtros de actividades.
 */
@Composable
fun EncabezadoYFiltros(
    filtroSeleccionado: String,
    onFiltroSeleccionado: (String) -> Unit,
    cantidadActividades: Int
) {

    Column(

        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "Actividades formativas",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Consulta tus actividades, fechas, estados y progreso."
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text = "Filtrar por estado:",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(

            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            FilterChip(

                selected = filtroSeleccionado == "Todas",

                onClick = {
                    onFiltroSeleccionado("Todas")
                },

                label = {
                    Text("Todas")
                }
            )

            FilterChip(

                selected = filtroSeleccionado == "Completadas",

                onClick = {
                    onFiltroSeleccionado("Completadas")
                },

                label = {
                    Text("Completadas")
                }
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Row(

            modifier = Modifier.fillMaxWidth(),

            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            FilterChip(

                selected = filtroSeleccionado == "En proceso",

                onClick = {
                    onFiltroSeleccionado("En proceso")
                },

                label = {
                    Text("En proceso")
                }
            )

            FilterChip(

                selected = filtroSeleccionado == "Pendientes",

                onClick = {
                    onFiltroSeleccionado("Pendientes")
                },

                label = {
                    Text("Pendientes")
                }
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text = "Mostrando $cantidadActividades actividad(es)",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}


/**
 * Lista vertical para pantallas pequeñas.
 */
@Composable
fun ListaActividades(
    actividades: List<ActividadFormativa>,
    modifier: Modifier = Modifier
) {

    if (actividades.isEmpty()) {

        SinResultados(
            modifier = modifier
        )

    } else {

        LazyColumn(

            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(

                items = actividades,

                key = { actividad ->
                    actividad.id
                }

            ) { actividad ->

                TarjetaActividad(
                    actividad = actividad
                )
            }
        }
    }
}


/**
 * Cuadrícula de dos columnas para pantallas amplias.
 */
@Composable
fun CuadriculaActividades(
    actividades: List<ActividadFormativa>,
    modifier: Modifier = Modifier
) {

    if (actividades.isEmpty()) {

        SinResultados(
            modifier = modifier
        )

    } else {

        LazyVerticalGrid(

            columns = GridCells.Fixed(2),

            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),

            horizontalArrangement = Arrangement.spacedBy(12.dp),

            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(

                items = actividades,

                key = { actividad ->
                    actividad.id
                }

            ) { actividad ->

                TarjetaActividad(
                    actividad = actividad
                )
            }
        }
    }
}


/**
 * Se muestra cuando el filtro no encuentra actividades.
 */
@Composable
fun SinResultados(
    modifier: Modifier = Modifier
) {

    Box(

        modifier = modifier.fillMaxSize(),

        contentAlignment = Alignment.Center
    ) {

        Text(
            text = "No hay actividades con este estado.",
            style = MaterialTheme.typography.bodyLarge
        )
    }
}


/**
 * Se muestra cuando la lista general está vacía.
 */
@Composable
fun EstadoVacio(
    modifier: Modifier = Modifier
) {

    Box(

        modifier = modifier,

        contentAlignment = Alignment.Center
    ) {

        Column(

            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "No hay actividades",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Todavía no tienes actividades registradas."
            )

            Button(

                onClick = {},

                modifier = Modifier.padding(top = 16.dp)
            ) {

                Text("Actualizar")
            }
        }
    }
}


/**
 * Datos de ejemplo.
 */
val actividadesEjemplo = listOf(

    ActividadFormativa(
        id = 1,
        titulo = "Introducción al desarrollo móvil",
        descripcion = "Conceptos básicos del desarrollo de aplicaciones móviles Android.",
        fecha = "11 de agosto",
        estado = "Completada",
        progreso = 100
    ),

    ActividadFormativa(
        id = 2,
        titulo = "Programación en Kotlin",
        descripcion = "Variables, funciones, colecciones y clases en Kotlin.",
        fecha = "12 de agosto",
        estado = "Completada",
        progreso = 100
    ),

    ActividadFormativa(
        id = 3,
        titulo = "Manifiesto Ágil",
        descripcion = "Estudio de los cuatro valores y doce principios del Manifiesto Ágil.",
        fecha = "13 de agosto",
        estado = "Completada",
        progreso = 100
    ),

    ActividadFormativa(
        id = 4,
        titulo = "Introducción a Scrum",
        descripcion = "Roles, eventos y artefactos principales de Scrum.",
        fecha = "14 de agosto",
        estado = "En proceso",
        progreso = 70
    ),

    ActividadFormativa(
        id = 5,
        titulo = "Pruebas de software",
        descripcion = "Identificación de los principales tipos de pruebas de software.",
        fecha = "15 de agosto",
        estado = "En proceso",
        progreso = 60
    ),

    ActividadFormativa(
        id = 6,
        titulo = "Jetpack Compose",
        descripcion = "Construcción de interfaces utilizando Jetpack Compose.",
        fecha = "16 de agosto",
        estado = "En proceso",
        progreso = 50
    ),

    ActividadFormativa(
        id = 7,
        titulo = "Material 3",
        descripcion = "Uso de componentes, colores y tipografía de Material 3.",
        fecha = "17 de agosto",
        estado = "Pendiente",
        progreso = 0
    ),

    ActividadFormativa(
        id = 8,
        titulo = "Accesibilidad",
        descripcion = "Revisión de contraste, tamaño de texto y zonas táctiles.",
        fecha = "18 de agosto",
        estado = "Pendiente",
        progreso = 0
    ),

    ActividadFormativa(
        id = 9,
        titulo = "Diseño adaptable",
        descripcion = "Adaptación de la interfaz para diferentes tamaños de pantalla.",
        fecha = "19 de agosto",
        estado = "Pendiente",
        progreso = 0
    ),

    ActividadFormativa(
        id = 10,
        titulo = "Proyecto Mi Formación CTMA",
        descripcion = "Integración de las actividades y evidencias del proceso formativo.",
        fecha = "20 de agosto",
        estado = "Pendiente",
        progreso = 0
    )
)


/**
 * Preview normal.
 */
@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PantallaActividadesPreview() {

    MiFormacionCTMATheme {

        PantallaActividades()
    }
}


/**
 * Preview de pantalla amplia.
 *
 * Al superar 600dp se activa
 * la cuadrícula de dos columnas.
 */
@Preview(
    showBackground = true,
    showSystemUi = true,
    widthDp = 1000,
    heightDp = 700
)
@Composable
fun PantallaActividadesAnchaPreview() {

    MiFormacionCTMATheme {

        PantallaActividades()
    }
}


/**
 * Preview del estado vacío.
 */
@Preview(
    showBackground = true
)
@Composable
fun EstadoVacioPreview() {

    MiFormacionCTMATheme {

        EstadoVacio(
            modifier = Modifier.fillMaxSize()
        )
    }
}