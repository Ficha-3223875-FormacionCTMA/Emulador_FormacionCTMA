@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.miformacionctma.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miformacionctma.model.ActividadFormativa
import com.example.miformacionctma.ui.ActividadesViewModel
import com.example.miformacionctma.ui.ListadoUiState
import com.example.miformacionctma.ui.components.TarjetaActividad
import com.example.miformacionctma.ui.theme.MiFormacionCTMATheme

@Composable
fun PantallaActividades(
    viewModel: ActividadesViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val busquedaQuery by viewModel.busquedaQuery.collectAsStateWithLifecycle()

    PantallaActividadesContent(
        uiState = uiState,
        busquedaQuery = busquedaQuery,
        onBusquedaChange = viewModel::onBusquedaChanged,
        onReintentar = { },
        viewModel = viewModel
    )
}

@Composable
fun PantallaActividadesContent(
    uiState: ListadoUiState,
    busquedaQuery: String,
    onBusquedaChange: (String) -> Unit,
    onReintentar: () -> Unit,
    viewModel: ActividadesViewModel? = null
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mi Formación CTMA") }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            OutlinedTextField(
                value = busquedaQuery,
                onValueChange = onBusquedaChange,
                label = { Text("Buscar actividad...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )

            when (uiState) {
                is ListadoUiState.Cargando -> {
                    EstadoCargando(modifier = Modifier.fillMaxSize())
                }
                is ListadoUiState.Vacio -> {
                    EstadoVacio(
                        modifier = Modifier.fillMaxSize(),
                        onActualizar = onReintentar
                    )
                }
                is ListadoUiState.Contenido -> {
                    ContenidoAdaptable(
                        actividades = uiState.actividades,
                        viewModel = viewModel,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp)
                    )
                }
                is ListadoUiState.Error -> {
                    EstadoError(
                        mensaje = uiState.mensaje,
                        onReintentar = onReintentar,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    }
}

@Composable
fun EstadoCargando(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun EstadoError(
    mensaje: String,
    onReintentar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Ha ocurrido un error",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.error
            )
            Text(
                text = mensaje,
                modifier = Modifier.padding(top = 8.dp)
            )
            Button(
                onClick = onReintentar,
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Reintentar")
            }
        }
    }
}

@Composable
fun EstadoVacio(
    modifier: Modifier = Modifier,
    onActualizar: () -> Unit = {}
) {
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
                onClick = onActualizar,
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
    viewModel: ActividadesViewModel? = null,
    modifier: Modifier = Modifier
) {
    val activeViewModel = viewModel ?: viewModel()

    BoxWithConstraints(modifier = modifier) {
        if (maxWidth < 600.dp) {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(actividades, key = { it.id }) { actividad ->
                    TarjetaActividad(
                        actividad = actividad,
                        viewModel = activeViewModel
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(top = 8.dp)
            ) {
                items(actividades, key = { it.id }) { actividad ->
                    TarjetaActividad(
                        actividad = actividad,
                        viewModel = activeViewModel
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaActividadesContenidoPreview() {
    MiFormacionCTMATheme {
        PantallaActividadesContent(
            uiState = ListadoUiState.Contenido(actividadesEjemplo),
            busquedaQuery = "",
            onBusquedaChange = {},
            onReintentar = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaActividadesCargandoPreview() {
    MiFormacionCTMATheme {
        PantallaActividadesContent(
            uiState = ListadoUiState.Cargando,
            busquedaQuery = "",
            onBusquedaChange = {},
            onReintentar = {}
        )
    }
}

val actividadesEjemplo = listOf(
    ActividadFormativa(1, "Introducción al desarrollo móvil", "Conceptos básicos del desarrollo de aplicaciones móviles Android.", "11 de agosto", "Completada", 100),
    ActividadFormativa(2, "Programación en Kotlin", "Variables, funciones, colecciones y clases en Kotlin.", "12 de agosto", "Completada", 100)
)