package com.example.miformacionctma
import com.example.miformacionctma.uii.screens.PantallaActividades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.miformacionctma.ui.theme.MiFormacionCTMATheme


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MiFormacionCTMATheme {
                PantallaActividades()
            }
        }
    }
}


// Modelo de una actividad formativa
data class ActividadFormativa(
    val id: Int,
    val titulo: String,
    val fecha: String,
    val estado: String,
    val progreso: Int
)


// Las 10 actividades solicitadas
val actividades = listOf(

    ActividadFormativa(
        1,
        "Manifiesto Ágil",
        "11 de agosto",
        "Completada",
        100
    ),

    ActividadFormativa(
        2,
        "Valores del Manifiesto Ágil",
        "12 de agosto",
        "Completada",
        100
    ),

    ActividadFormativa(
        3,
        "Principios Ágiles",
        "13 de agosto",
        "Completada",
        100
    ),

    ActividadFormativa(
        4,
        "Introducción a Scrum",
        "14 de agosto",
        "En proceso",
        75
    ),

    ActividadFormativa(
        5,
        "Roles de Scrum",
        "15 de agosto",
        "En proceso",
        60
    ),

    ActividadFormativa(
        6,
        "Artefactos de Scrum",
        "16 de agosto",
        "En proceso",
        50
    ),

    ActividadFormativa(
        7,
        "Pruebas de software",
        "17 de agosto",
        "Pendiente",
        0
    ),

    ActividadFormativa(
        8,
        "Tipos de pruebas",
        "18 de agosto",
        "Pendiente",
        0
    ),

    ActividadFormativa(
        9,
        "Jetpack Compose",
        "19 de agosto",
        "Pendiente",
        0
    ),

    ActividadFormativa(
        10,
        "Proyecto Mi Formación CTMA",
        "20 de agosto",
        "Pendiente",
        0
    )
)


// Pantalla principal
@Composable
fun PantallaFormacion(
    lista: List<ActividadFormativa> = actividades
) {

    Surface(
        modifier = Modifier.fillMaxSize()
    ) {

        if (lista.isEmpty()) {

            MensajeSinActividades()

        } else {

            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {

                val anchoGrande = maxWidth >= 600.dp

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {

                    Encabezado(
                        cantidad = lista.size
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    if (anchoGrande) {

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            items(
                                items = lista,
                                key = { actividad -> actividad.id }
                            ) { actividad ->

                                Tarjeta(
                                    actividad = actividad
                                )
                            }
                        }

                    } else {

                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {

                            items(
                                items = lista,
                                key = { actividad -> actividad.id }
                            ) { actividad ->

                                Tarjeta(
                                    actividad = actividad
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}


// Encabezado de la aplicación
@Composable
fun Encabezado(
    cantidad: Int
) {

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(
            text = "Mi Formación CTMA",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(5.dp)
        )

        Text(
            text = "Seguimiento de actividades formativas",
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = "$cantidad actividades registradas",
            style = MaterialTheme.typography.labelLarge
        )
    }
}


// Tarjeta individual
@Composable
fun Tarjeta(
    actividad: ActividadFormativa
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {

                contentDescription =
                    "${actividad.titulo}, " +
                            "fecha ${actividad.fecha}, " +
                            "estado ${actividad.estado}, " +
                            "progreso ${actividad.progreso} por ciento"
            }
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = actividad.titulo,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Fecha"
                )

                Text(
                    text = actividad.fecha
                )
            }

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Estado"
                )

                Text(
                    text = actividad.estado
                )
            }

            Spacer(
                modifier = Modifier.height(10.dp)
            )

            Text(
                text = "Progreso: ${actividad.progreso}%"
            )

            Spacer(
                modifier = Modifier.height(5.dp)
            )

            LinearProgressIndicator(
                progress = actividad.progreso / 100f,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


// Estado vacío
@Composable
fun MensajeSinActividades() {

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(24.dp)
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

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Button(
                onClick = { }
            ) {

                Text(
                    text = "Actualizar"
                )
            }
        }
    }
}


// Preview de la pantalla
@Preview(
    showBackground = true
)
@Composable
fun VistaFormacion() {

    MiFormacionCTMATheme {
        PantallaFormacion()
    }
}


// Preview para comprobar textos largos
@Preview(
    showBackground = true
)
@Composable
fun VistaTextoLargo() {

    MiFormacionCTMATheme {

        Tarjeta(
            actividad = ActividadFormativa(
                id = 20,
                titulo = "Actividad con un título muy largo para comprobar que la interfaz se adapte correctamente",
                fecha = "25 de agosto",
                estado = "En proceso",
                progreso = 50
            )
        )
    }
}


// Preview del estado vacío
@Preview(
    showBackground = true
)
@Composable
fun VistaVacia() {

    MiFormacionCTMATheme {

        MensajeSinActividades()
    }
}


