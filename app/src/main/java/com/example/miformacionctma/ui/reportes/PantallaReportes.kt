package com.example.miformacionctma.ui.reportes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.miformacionctma.domain.model.OrdenReportes
import com.example.miformacionctma.domain.model.Reporte

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaReportes(
    uiState: ReportesUiState,
    onTextoBusquedaCambia: (String) -> Unit,
    onOrdenSeleccionado: (OrdenReportes) -> Unit,
    onCategoriaFiltroSeleccionada: (Long?) -> Unit,
    onAlternarResuelto: (Reporte) -> Unit,
    onEliminar: (Reporte) -> Unit,
    onNuevoReporte: () -> Unit
) {
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = onNuevoReporte) {
                Icon(Icons.Filled.Add, contentDescription = "Nuevo reporte")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            Text("Reportes de novedades", style = MaterialTheme.typography.headlineSmall)
            Spacer4()

            OutlinedTextField(
                value = uiState.textoBusqueda,
                onValueChange = { onTextoBusquedaCambia(it) },
                label = { Text("Buscar por título o descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer4()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FiltroCategoria(
                    categorias = uiState.categorias,
                    categoriaSeleccionadaId = uiState.categoriaFiltroId,
                    onSeleccionar = onCategoriaFiltroSeleccionada
                )
                SelectorOrden(orden = uiState.orden, onOrdenSeleccionado = onOrdenSeleccionado)
            }
            Spacer4()

            if (uiState.cargando) {
                Text("Cargando…")
            } else if (uiState.reportes.isEmpty()) {
                Text("No hay reportes que coincidan con el filtro actual.")
            } else {
                LazyColumn {
                    items(uiState.reportes, key = { it.id }) { reporte ->
                        TarjetaReporte(
                            reporte = reporte,
                            nombreCategoria = uiState.categorias
                                .firstOrNull { it.id == reporte.categoriaId }?.nombre,
                            onAlternarResuelto = { onAlternarResuelto(reporte) },
                            onEliminar = { onEliminar(reporte) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Spacer4() = Spacer(Modifier.padding(4.dp))

@Composable
private fun TarjetaReporte(
    reporte: Reporte,
    nombreCategoria: String?,
    onAlternarResuelto: () -> Unit,
    onEliminar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                Text(reporte.titulo, style = MaterialTheme.typography.titleMedium)
                Text(reporte.descripcion, style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = nombreCategoria ?: "Sin categoría",
                    style = MaterialTheme.typography.labelMedium
                )
            }
            Row {
                IconButton(onClick = onAlternarResuelto) {
                    Icon(
                        imageVector = if (reporte.resuelto) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                        contentDescription = "Marcar como resuelto"
                    )
                }
                IconButton(onClick = onEliminar) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                }
            }
        }
    }
}

@Composable
private fun FiltroCategoria(
    categorias: List<com.example.miformacionctma.domain.model.Categoria>,
    categoriaSeleccionadaId: Long?,
    onSeleccionar: (Long?) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    val nombreSeleccionado = categorias.firstOrNull { it.id == categoriaSeleccionadaId }?.nombre
        ?: "Todas"

    Column {
        FilterChip(
            selected = categoriaSeleccionadaId != null,
            onClick = { expandido = true },
            label = { Text(nombreSeleccionado) }
        )
        DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
            DropdownMenuItem(
                text = { Text("Todas") },
                onClick = { onSeleccionar(null); expandido = false }
            )
            categorias.forEach { categoria ->
                DropdownMenuItem(
                    text = { Text(categoria.nombre) },
                    onClick = { onSeleccionar(categoria.id); expandido = false }
                )
            }
        }
    }
}

@Composable
private fun SelectorOrden(
    orden: OrdenReportes,
    onOrdenSeleccionado: (OrdenReportes) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    val etiqueta = when (orden) {
        OrdenReportes.FECHA_DESC -> "Más recientes"
        OrdenReportes.FECHA_ASC -> "Más antiguos"
        OrdenReportes.TITULO_ASC -> "Título A-Z"
    }
    Column {
        TextButton(onClick = { expandido = true }) { Text(etiqueta) }
        DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
            DropdownMenuItem(
                text = { Text("Más recientes") },
                onClick = { onOrdenSeleccionado(OrdenReportes.FECHA_DESC); expandido = false }
            )
            DropdownMenuItem(
                text = { Text("Más antiguos") },
                onClick = { onOrdenSeleccionado(OrdenReportes.FECHA_ASC); expandido = false }
            )
            DropdownMenuItem(
                text = { Text("Título A-Z") },
                onClick = { onOrdenSeleccionado(OrdenReportes.TITULO_ASC); expandido = false }
            )
        }
    }
}
