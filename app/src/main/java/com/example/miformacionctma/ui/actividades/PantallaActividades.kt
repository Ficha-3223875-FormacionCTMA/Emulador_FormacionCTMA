package com.example.miformacionctma.ui.actividades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.miformacionctma.domain.model.Actividad
import com.example.miformacionctma.domain.model.EstadoActividad

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaActividades(
    uiState: ActividadesUiState,
    onCambiarOrden: () -> Unit,
    onFiltrarEstado: (EstadoActividad?) -> Unit,
    onGuardarActividad: (Actividad) -> Unit,
    onEliminarActividad: (Actividad) -> Unit
) {
    var mostrarDialogoNueva by remember { mutableStateOf(false) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { mostrarDialogoNueva = true }) {
                Icon(Icons.Filled.Add, contentDescription = "Nueva Actividad")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Gestión de Actividades",
                style = MaterialTheme.typography.headlineSmall
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Fila de estado de operación
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Operación: ${uiState.estadoOperacion.name}",
                    style = MaterialTheme.typography.labelLarge,
                    color = when (uiState.estadoOperacion) {
                        EstadoOperacionActividades.EN_CURSO -> MaterialTheme.colorScheme.primary
                        EstadoOperacionActividades.EXITOSA -> MaterialTheme.colorScheme.secondary
                        EstadoOperacionActividades.FALLIDA -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurfaceVariant
                    }
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Total: ${uiState.totalActividades}",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            uiState.errorMensaje?.let { msg ->
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Error: $msg", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Filtros y Ordenamiento
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FiltroEstadoActividad(
                    seleccionado = uiState.filtroEstado,
                    onSeleccionar = onFiltrarEstado
                )

                Button(onClick = onCambiarOrden) {
                    Icon(
                        imageVector = if (uiState.ordenProgresoDesc) Icons.Filled.ArrowDownward else Icons.Filled.ArrowUpward,
                        contentDescription = "Orden"
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (uiState.ordenProgresoDesc) "Progreso Desc" else "Progreso Asc")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Renderizar de acuerdo al estado de la pantalla
            when (uiState.estadoPantalla) {
                EstadoPantallaActividades.CARGANDO -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }
                EstadoPantallaActividades.VACIO -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("No se encontraron actividades.", style = MaterialTheme.typography.bodyLarge)
                    }
                }
                EstadoPantallaActividades.ERROR -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(
                            text = "Error al cargar datos: ${uiState.errorMensaje ?: "Desconocido"}",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                EstadoPantallaActividades.CONTENIDO -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.actividades, key = { it.id }) { actividad ->
                            TarjetaActividad(
                                actividad = actividad,
                                onEliminar = { onEliminarActividad(actividad) }
                            )
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogoNueva) {
        DialogoNuevaActividad(
            onDismiss = { mostrarDialogoNueva = false },
            onConfirmar = { nombre, desc, progreso, estado ->
                onGuardarActividad(
                    Actividad(
                        nombre = nombre,
                        descripcion = desc,
                        progreso = progreso,
                        estado = estado,
                        fechaCreacion = System.currentTimeMillis()
                    )
                )
                mostrarDialogoNueva = false
            }
        )
    }
}

@Composable
private fun TarjetaActividad(
    actividad: Actividad,
    onEliminar: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = actividad.nombre, style = MaterialTheme.typography.titleMedium)
                Text(text = actividad.descripcion, style = MaterialTheme.typography.bodyMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    SuggestionChip(onClick = {}, label = { Text(actividad.estado.name) })
                    SuggestionChip(onClick = {}, label = { Text("Progreso: ${actividad.progreso}%") })
                }
            }
            IconButton(onClick = onEliminar) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@Composable
private fun FiltroEstadoActividad(
    seleccionado: EstadoActividad?,
    onSeleccionar: (EstadoActividad?) -> Unit
) {
    var expandido by remember { mutableStateOf(false) }
    val textoLabel = seleccionado?.name ?: "Todos los estados"

    Box {
        OutlinedButton(onClick = { expandido = true }) {
            Text(textoLabel)
        }
        DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
            DropdownMenuItem(
                text = { Text("Todos los estados") },
                onClick = { onSeleccionar(null); expandido = false }
            )
            EstadoActividad.values().forEach { estado ->
                DropdownMenuItem(
                    text = { Text(estado.name) },
                    onClick = { onSeleccionar(estado); expandido = false }
                )
            }
        }
    }
}

@Composable
private fun DialogoNuevaActividad(
    onDismiss: () -> Unit,
    onConfirmar: (String, String, Int, EstadoActividad) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var progresoStr by remember { mutableStateOf("0") }
    var estado by remember { mutableStateOf(EstadoActividad.PENDIENTE) }
    var expandidoEstado by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Actividad") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = progresoStr,
                    onValueChange = { progresoStr = it },
                    label = { Text("Progreso (0-100)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Box {
                    OutlinedButton(onClick = { expandidoEstado = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("Estado: ${estado.name}")
                    }
                    DropdownMenu(expanded = expandidoEstado, onDismissRequest = { expandidoEstado = false }) {
                        EstadoActividad.values().forEach { est ->
                            DropdownMenuItem(
                                text = { Text(est.name) },
                                onClick = { estado = est; expandidoEstado = false }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val progreso = progresoStr.toIntOrNull() ?: 0
                    if (nombre.isNotBlank()) {
                        onConfirmar(nombre, descripcion, progreso.coerceIn(0, 100), estado)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
