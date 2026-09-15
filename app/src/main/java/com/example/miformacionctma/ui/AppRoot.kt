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
import com.example.miformacionctma.ui.actividades.ActividadesViewModel
import com.example.miformacionctma.ui.actividades.PantallaActividades

private enum class Destino { INICIO, ACTIVIDADES }

/**
 * Contenedor raíz de la app simplificado: conserva la pantalla teórica obligatoria
 * (PantallaInicio) y establece a "Actividades" como la única pestaña de gestión práctica
 * con persistencia en base de datos local y filtros unificados.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppRoot(container: AppContainer) {
    var destino by remember { mutableStateOf(Destino.ACTIVIDADES) }

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
                    selected = destino == Destino.ACTIVIDADES,
                    onClick = { destino = Destino.ACTIVIDADES },
                    icon = { Icon(Icons.Filled.Assignment, contentDescription = null) },
                    label = { Text("Actividades") }
                )
            }
        }
    ) { padding ->
        when (destino) {
            Destino.INICIO -> PantallaInicio(nombre = "Aprendiz")
            Destino.ACTIVIDADES -> SeccionActividades(container)
        }
    }
}

@Composable
private fun SeccionActividades(container: AppContainer) {
    val viewModel: ActividadesViewModel = viewModel(factory = ActividadesViewModel.factory(container))
    val uiState by viewModel.uiState.collectAsState()

    PantallaActividades(
        uiState = uiState,
        onCambiarOrden = viewModel::onCambiarOrdenProgreso,
        onFiltrarEstado = viewModel::onFiltrarEstado,
        onGuardarActividad = viewModel::guardarActividad,
        onEliminarActividad = viewModel::eliminarActividad,
        onSincronizarServidor = viewModel::sincronizarConServidorRemoto
    )
}
