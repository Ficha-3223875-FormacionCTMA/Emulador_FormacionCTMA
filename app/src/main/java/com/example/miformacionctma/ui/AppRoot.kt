package com.example.miformacionctma.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.miformacionctma.PantallaInicio
import com.example.miformacionctma.di.AppContainer
import com.example.miformacionctma.ui.reportes.PantallaNuevoReporte
import com.example.miformacionctma.ui.reportes.PantallaReportes
import com.example.miformacionctma.ui.reportes.ReportesViewModel

private enum class Destino { INICIO, REPORTES }

/**
 * Contenedor raíz de la app: conserva la pantalla teórica de semanas
 * anteriores (PantallaInicio) y agrega la nueva funcionalidad de
 * persistencia (Reportes) en una segunda pestaña, sin tocar código ya
 * entregado.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot(container: AppContainer) {
    var destino by remember { mutableStateOf(Destino.INICIO) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = destino == Destino.INICIO,
                    onClick = { destino = Destino.INICIO },
                    icon = { Icon(Icons.Filled.Home, contentDescription = null) },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = destino == Destino.REPORTES,
                    onClick = { destino = Destino.REPORTES },
                    icon = { Icon(Icons.Filled.Assignment, contentDescription = null) },
                    label = { Text("Reportes") }
                )
            }
        }
    ) { padding ->
        when (destino) {
            Destino.INICIO -> PantallaInicio(nombre = "Aprendiz")
            Destino.REPORTES -> SeccionReportes(container)
        }
    }
}

@Composable
private fun SeccionReportes(container: AppContainer) {
    val viewModel: ReportesViewModel = viewModel(factory = ReportesViewModel.factory(container))
    val uiState by viewModel.uiState.collectAsState()
    var mostrandoFormulario by remember { mutableStateOf(false) }

    if (mostrandoFormulario) {
        PantallaNuevoReporte(
            categorias = uiState.categorias,
            onGuardar = { titulo, descripcion, categoriaId ->
                viewModel.guardarReporte(titulo, descripcion, categoriaId)
                mostrandoFormulario = false
            },
            onCancelar = { mostrandoFormulario = false }
        )
    } else {
        PantallaReportes(
            uiState = uiState,
            onTextoBusquedaCambia = viewModel::onTextoBusquedaCambia,
            onOrdenSeleccionado = viewModel::onOrdenSeleccionado,
            onCategoriaFiltroSeleccionada = viewModel::onCategoriaFiltroSeleccionada,
            onAlternarResuelto = viewModel::alternarResuelto,
            onEliminar = viewModel::eliminarReporte,
            onNuevoReporte = { mostrandoFormulario = true }
        )
    }
}