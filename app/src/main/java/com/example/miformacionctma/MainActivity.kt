package com.example.miformacionctma

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miformacionctma.data.preferences.PreferenciasRepository
import com.example.miformacionctma.data.repository.ActividadRepository
import com.example.miformacionctma.model.ActividadFormativa
import com.example.miformacionctma.ui.ActividadesViewModel
import com.example.miformacionctma.ui.ListadoUiState
import com.example.miformacionctma.ui.theme.MiFormacionCTMATheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MiFormacionCTMATheme {
                val viewModel: ActividadesViewModel = viewModel(
                    factory = object : ViewModelProvider.Factory {
                        override fun <T : ViewModel> create(modelClass: Class<T>): T {
                            // Implementación funcional del repositorio de actividades
                            val repository = object : ActividadRepository {
                                override fun obtenerActividades(
                                    filtro: String,
                                    orden: String,
                                    busqueda: String
                                ): Flow<List<ActividadFormativa>> = flowOf(actividades)

                                override suspend fun insertarActividad(actividad: ActividadFormativa) {}
                                override suspend fun eliminarActividad(actividad: ActividadFormativa) {}
                            }

                            // Implementación funcional del repositorio de preferencias
                            val preferenciasRepository = object : PreferenciasRepository {
                                override fun obtenerFiltroCompetencia(): Flow<String> = flowOf("Todas")
                                override fun obtenerOrdenamiento(): Flow<String> = flowOf("Fecha")
                                override suspend fun guardarFiltroCompetencia(filtro: String) {}
                                override suspend fun guardarOrdenamiento(orden: String) {}
                            }

                            @Suppress("UNCHECKED_CAST")
                            return ActividadesViewModel(repository, preferenciasRepository) as T
                        }
                    }
                )

                val uiState by viewModel.uiState.collectAsStateWithLifecycle()

                when (val state = uiState) {
                    is ListadoUiState.Cargando -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                    is ListadoUiState.Contenido -> {
                        PantallaFormacion(lista = state.actividades)
                    }
                    is ListadoUiState.Vacio -> {
                        MensajeSinActividades()
                    }
                    is ListadoUiState.Error -> {
                        MensajeSinActividades()
                    }
                }
            }
        }
    }
}

// Las 10 actividades ajustadas a la firma (id, tituló, fecha, estado, progreso)
val actividades = listOf(
    ActividadFormativa(
        id = 1,
        titulo = "Manifiesto Ágil",
        fecha = "11 de agosto",
        estado = "Completada",
        progreso = 100
    ),
    ActividadFormativa(
        id = 2,
        titulo = "Valores del Manifiesto Ágil",
        fecha = "12 de agosto",
        estado = "Completada",
        progreso = 100
    ),
    ActividadFormativa(
        id = 3,
        titulo = "Principios Ágiles",
        fecha = "13 de agosto",
        estado = "Completada",
        progreso = 100
    ),
    ActividadFormativa(
        id = 4,
        titulo = "Introducción a Scrum",
        fecha = "14 de agosto",
        estado = "En proceso",
        progreso = 75
    ),
    ActividadFormativa(
        id = 5,
        titulo = "Roles de Scrum",
        fecha = "15 de agosto",
        estado = "En proceso",
        progreso = 60
    ),
    ActividadFormativa(
        id = 6,
        titulo = "Artefactos de Scrum",
        fecha = "16 de agosto",
        estado = "En proceso",
        progreso = 50
    ),
    ActividadFormativa(
        id = 7,
        titulo = "Pruebas de software",
        fecha = "17 de agosto",
        estado = "Pendiente",
        progreso = 0
    ),
    ActividadFormativa(
        id = 8,
        titulo = "Tipos de pruebas",
        fecha = "18 de agosto",
        estado = "Pendiente",
        progreso = 0
    ),
    ActividadFormativa(
        id = 9,
        titulo = "Jetpack Compose",
        fecha = "19 de agosto",
        estado = "Pendiente",
        progreso = 0
    ),
    ActividadFormativa(
        id = 10,
        titulo = "Proyecto Mi Formación CTMA",
        fecha = "20 de agosto",
        estado = "Pendiente",
        progreso = 0
    )
)


// Pantalla principal original intacta
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

// Encabezado original
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

// Tarjeta individual original
// Tarjeta individual original (con ID visible)
@Composable
fun Tarjeta(
    actividad: ActividadFormativa
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription =
                    "ID ${actividad.id}, " +
                            "${actividad.titulo}, " +
                            "fecha ${actividad.fecha}, " +
                            "estado ${actividad.estado}, " +
                            "progreso ${actividad.progreso} por ciento"
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // ---> AQUÍ VA EL ID DE LA ACTIVIDAD <---
            Text(
                text = "ID: ${actividad.id}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

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
                progress = { actividad.progreso / 100f },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// Estado vacío original
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

@Preview(showBackground = true)
@Composable
fun VistaFormacion() {
    MiFormacionCTMATheme {
        PantallaFormacion()
    }
}