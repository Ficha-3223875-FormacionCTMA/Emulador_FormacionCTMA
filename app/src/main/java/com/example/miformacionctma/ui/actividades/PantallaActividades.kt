package com.example.miformacionctma.ui.actividades

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.Refresh
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
    onEliminarActividad: (Actividad) -> Unit,
    onSincronizarServidor: () -> Unit
) {
    var mostrarDialogoNueva by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Gestión de Actividades / Tareas",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f)
            )
            
            // Botón de sincronización con servidor REST amigable (Semana 8)
            IconButton(onClick = onSincronizarServidor) {
                Icon(Icons.Filled.Refresh, contentDescription = "Sincronizar Servidor")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

        // Fila de estado de operación básica sin alterar el diseño original
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
            Text(
                text = msg, 
                color = if (uiState.estadoOperacion == EstadoOperacionActividades.FALLIDA) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline, 
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Mantener los filtros que siempre ha tenido el proyecto
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

        // Renderizado de acuerdo a los estados de la pantalla requeridos
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
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
                            text = "Error al cargar datos locales.",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
                EstadoPantallaActividades.CONTENIDO -> {
                    val bloquesA_Mostrar = if (uiState.filtroEstado != null) {
                        listOf(uiState.filtroEstado)
                    } else {
                        EstadoActividad.entries
                    }

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        bloquesA_Mostrar.forEach { estado ->
                            val listaFiltradaPorBloque = uiState.actividades.filter { it.estado == estado }
                            if (listaFiltradaPorBloque.isNotEmpty()) {
                                item(key = "header_${estado.name}") {
                                    Surface(
                                        color = MaterialTheme.colorScheme.secondaryContainer,
                                        shape = MaterialTheme.shapes.small,
                                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = when (estado) {
                                                EstadoActividad.PENDIENTE -> "📌 Tareas Pendientes"
                                                EstadoActividad.EN_PROCESO -> "⚡ En Proceso"
                                                EstadoActividad.COMPLETADA -> "✅ Completadas"
                                            },
                                            style = MaterialTheme.typography.titleMedium,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                            color = MaterialTheme.colorScheme.onSecondaryContainer
                                        )
                                    }
                                }

                                items(listaFiltradaPorBloque, key = { it.id }) { actividad ->
                                    TarjetaActividad(
                                        actividad = actividad,
                                        onAlternarCompletado = {
                                            val nuevoEstado = if (actividad.estado == EstadoActividad.COMPLETADA) {
                                                EstadoActividad.PENDIENTE
                                            } else {
                                                EstadoActividad.COMPLETADA
                                            }
                                            val nuevoProgreso = if (nuevoEstado == EstadoActividad.COMPLETADA) 100 else 0
                                            onGuardarActividad(actividad.copy(estado = nuevoEstado, progreso = nuevoProgreso))
                                        },
                                        onEliminar = { onEliminarActividad(actividad) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Botón muy visible en la parte inferior del campo de actividades
        Button(
            onClick = { mostrarDialogoNueva = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "AGREGAR NUEVA ACTIVIDAD / TAREA", style = MaterialTheme.typography.titleSmall)
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
    onAlternarCompletado: () -> Unit,
    onEliminar: () -> Unit
) {
    val esCompletada = actividad.estado == EstadoActividad.COMPLETADA

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onAlternarCompletado) {
                Icon(
                    imageVector = if (esCompletada) Icons.Filled.CheckCircle else Icons.Filled.RadioButtonUnchecked,
                    contentDescription = "Completar Tarea",
                    tint = if (esCompletada) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
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
            EstadoActividad.entries.forEach { estado ->
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
        title = { Text("Nueva Actividad / Tarea") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre de la Tarea") },
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
                    label = { Text("Progreso inicial (0-100)") },
                    modifier = Modifier.fillMaxWidth()
                )
                Box {
                    OutlinedButton(onClick = { expandidoEstado = true }, modifier = Modifier.fillMaxWidth()) {
                        Text("Estado: ${estado.name}")
                    }
                    DropdownMenu(expanded = expandidoEstado, onDismissRequest = { expandidoEstado = false }) {
                        EstadoActividad.entries.forEach { est ->
                            DropdownMenuItem(
                                text = { Text(est.name) },
                                onClick = {
                                    estado = est
                                    if (est == EstadoActividad.COMPLETADA) {
                                        progresoStr = "100"
                                    }
                                    expandidoEstado = false
                                }
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
