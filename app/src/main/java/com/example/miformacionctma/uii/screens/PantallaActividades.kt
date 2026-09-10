@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
package com.example.miformacionctma.uii.screens
import androidx.compose.foundation.layout.BoxWithConstraints


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.miformacionctma.model.ActividadFormativa
import com.example.miformacionctma.uii.components.TarjetaActividad
import com.example.miformacionctma.ui.theme.MiFormacionCTMATheme

@Composable
fun PantallaActividades(
    actividades: List<ActividadFormativa> = actividadesEjemplo
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Formación CTMA") }
            )
        }
    ) { paddingValues ->
        if (actividades.isEmpty()) {
            EstadoVacio(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            )
        } else {
            ContenidoAdaptable(
                actividades = actividades,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
            )
        }
    }
}

@Composable
fun EstadoVacio(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "No hay actividades",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "Todavía no tienes actividades registradas.",
                modifier = Modifier.padding(top = 8.dp)
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

@Composable
fun ContenidoAdaptable(
    actividades: List<ActividadFormativa>,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(modifier = modifier) {
        if (this.maxWidth < 600.dp) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text(
                            text = "Actividades formativas",
                            style = MaterialTheme.typography.headlineSmall
                        )
                        Text(
                            text = "Consulta tus actividades, fechas, estados y progreso.",
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
                items(actividades, key = { it.id }) { actividad ->
                    TarjetaActividad(actividad = actividad)
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 16.dp)
            ) {
                items(actividades, key = { it.id }) { actividad ->
                    TarjetaActividad(actividad = actividad)
                }
            }
        }
    }
}

val actividadesEjemplo = listOf(
    ActividadFormativa(1, "Introducción al desarrollo móvil", "Conceptos básicos del desarrollo de aplicaciones móviles Android.", "11 de agosto", "Completada", 100),
    ActividadFormativa(2, "Programación en Kotlin", "Variables, funciones, colecciones y clases en Kotlin.", "12 de agosto", "Completada", 100),
    ActividadFormativa(3, "Manifiesto Ágil", "Estudio de los cuatro valores y doce principios del Manifiesto Ágil.", "13 de agosto", "Completada", 100),
    ActividadFormativa(4, "Introducción a Scrum", "Roles, eventos y artefactos principales de Scrum.", "14 de agosto", "En proceso", 70),
    ActividadFormativa(5, "Pruebas de software", "Identificación de los principales tipos de pruebas de software.", "15 de agosto", "En proceso", 60),
    ActividadFormativa(6, "Jetpack Compose", "Construcción de interfaces utilizando Jetpack Compose.", "16 de agosto", "En proceso", 50),
    ActividadFormativa(7, "Material 3", "Uso de componentes, colores y tipografía de Material 3.", "17 de agosto", "Pendiente", 0),
    ActividadFormativa(8, "Accesibilidad", "Revisión de contraste, tamaño de texto y zonas táctiles.", "18 de agosto", "Pendiente", 0),
    ActividadFormativa(9, "Diseño adaptable", "Adaptación de la interfaz para diferentes tamaños de pantalla.", "19 de agosto", "Pendiente", 0),
    ActividadFormativa(10, "Proyecto Mi Formación CTMA", "Integración de las actividades y evidencias del proceso formativo.", "20 de agosto", "Pendiente", 0)
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PantallaActividadesPreview() {
    MiFormacionCTMATheme {
        PantallaActividades()
    }
}

@Preview(showBackground = true)
@Composable
fun EstadoVacioPreview() {
    MiFormacionCTMATheme {
        EstadoVacio(modifier = Modifier.fillMaxSize())
    }
}

@Preview(showBackground = true, fontScale = 1.5f)
@Composable
fun PantallaActividadesFuenteGrandePreview() {
    MiFormacionCTMATheme {
        PantallaActividades()
    }
}

@Preview(showBackground = true, widthDp = 700)
@Composable
fun PantallaActividadesAnchoAmpliadoPreview() {
    MiFormacionCTMATheme {
        ContenidoAdaptable(actividadesEjemplo)
    }
}
