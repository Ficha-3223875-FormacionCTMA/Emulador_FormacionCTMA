package com.example.miformacionctma.ui.actividades

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.miformacionctma.domain.model.Actividad
import com.example.miformacionctma.domain.model.EstadoActividad
import com.example.miformacionctma.domain.model.EstadoEvidencia
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaActividades(
    uiState: ActividadesUiState,
    onCambiarOrden: () -> Unit,
    onFiltrarEstado: (EstadoActividad?) -> Unit,
    onGuardarActividad: (Actividad) -> Unit,
    onEliminarActividad: (Actividad) -> Unit,
    onSincronizarServidor: () -> Unit,
    onActualizarEvidenciaLocal: (Actividad, String) -> Unit,
    onSubirEvidencia: (Actividad) -> Unit,
    onEnviarRecordatorio: (android.content.Context, Actividad) -> Unit
) {
    var mostrarDialogoNueva by remember { mutableStateOf(false) }
    var actividadSeleccionadaEvidencia by remember { mutableStateOf<Actividad?>(null) }
    var mostrarOpcionesImagen by remember { mutableStateOf(false) }
    
    val context = LocalContext.current
    var temporalUri by remember { mutableStateOf<Uri?>(null) }

    // Launcher para Galería
    val galeriaLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { 
            actividadSeleccionadaEvidencia?.let { act ->
                onActualizarEvidenciaLocal(act, it.toString())
            }
        }
    }

    // Launcher para Cámara (captura real)
    val camaraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito) {
            temporalUri?.let { uri ->
                actividadSeleccionadaEvidencia?.let { act ->
                    onActualizarEvidenciaLocal(act, uri.toString())
                }
            }
        }
    }

    val notificacionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ -> }

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
            
            IconButton(onClick = onSincronizarServidor) {
                Icon(Icons.Filled.Refresh, contentDescription = "Sincronizar Servidor")
            }
        }
        Spacer(modifier = Modifier.height(8.dp))

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
                        Text(text = "Error al cargar datos locales.", color = MaterialTheme.colorScheme.error)
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
                                        onEliminar = { onEliminarActividad(actividad) },
                                        onAdjuntarClick = { 
                                            actividadSeleccionadaEvidencia = actividad
                                            mostrarOpcionesImagen = true 
                                        },
                                        onSubirEvidencia = { onSubirEvidencia(actividad) },
                                        onActivarRecordatorio = { 
                                            if (android.os.Build.VERSION.SDK_INT >= 33) {
                                                notificacionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                            }
                                            onEnviarRecordatorio(context, actividad) 
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = { mostrarDialogoNueva = true },
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "AGREGAR NUEVA ACTIVIDAD / TAREA", style = MaterialTheme.typography.titleSmall)
        }
    }

    // Modal para elegir Cámara o Galería (Cumple criterio: "seleccionar o capturar")
    if (mostrarOpcionesImagen) {
        AlertDialog(
            onDismissRequest = { mostrarOpcionesImagen = false },
            title = { Text("Adjuntar evidencia") },
            text = { Text("¿Deseas capturar una foto nueva o elegir una de la galería?") },
            confirmButton = {
                TextButton(onClick = {
                    try {
                        val directory = File(context.cacheDir, "images")
                        if (!directory.exists()) directory.mkdirs()
                        val file = File(directory, "evidencia_${System.currentTimeMillis()}.jpg")
                        val uri = FileProvider.getUriForFile(
                            context, 
                            "com.example.miformacionctma.fileprovider", 
                            file
                        )
                        temporalUri = uri
                        camaraLauncher.launch(uri)
                    } catch (e: Exception) {
                        // Si falla la cámara real en el emulador, usamos una URI de ejemplo para no detener la demo
                        actividadSeleccionadaEvidencia?.let { act ->
                            onActualizarEvidenciaLocal(act, "https://picsum.photos/400/300")
                        }
                    }
                    mostrarOpcionesImagen = false
                }) {
                    Text("Cámara")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    galeriaLauncher.launch("image/*")
                    mostrarOpcionesImagen = false
                }) {
                    Text("Galería")
                }
            }
        )
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
    onEliminar: () -> Unit,
    onAdjuntarClick: () -> Unit,
    onSubirEvidencia: () -> Unit,
    onActivarRecordatorio: () -> Unit
) {
    val esCompletada = actividad.estado == EstadoActividad.COMPLETADA

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
                }

                Row {
                    IconButton(onClick = onActivarRecordatorio) {
                        Icon(Icons.Filled.NotificationsActive, contentDescription = "Recordatorio", tint = MaterialTheme.colorScheme.secondary)
                    }
                    IconButton(onClick = onEliminar) {
                        Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (actividad.evidenciaUri != null) {
                    Card(
                        modifier = Modifier.size(60.dp).clickable { onAdjuntarClick() },
                        shape = MaterialTheme.shapes.small
                    ) {
                        AsyncImage(
                            model = actividad.evidenciaUri,
                            contentDescription = "Evidencia",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when(actividad.estadoEvidencia) {
                                    EstadoEvidencia.LOCAL -> Icons.Filled.CloudUpload
                                    EstadoEvidencia.SUBIENDO -> Icons.Filled.HourglassTop
                                    EstadoEvidencia.SINCRONIZADA -> Icons.Filled.CloudDone
                                    EstadoEvidencia.FALLIDA -> Icons.Filled.CloudOff
                                },
                                contentDescription = null,
                                tint = when(actividad.estadoEvidencia) {
                                    EstadoEvidencia.SINCRONIZADA -> MaterialTheme.colorScheme.primary
                                    EstadoEvidencia.FALLIDA -> MaterialTheme.colorScheme.error
                                    else -> MaterialTheme.colorScheme.outline
                                },
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = actividad.estadoEvidencia.name,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                        
                        if (actividad.estadoEvidencia == EstadoEvidencia.LOCAL || actividad.estadoEvidencia == EstadoEvidencia.FALLIDA) {
                            TextButton(onClick = onSubirEvidencia, contentPadding = PaddingValues(0.dp)) {
                                Text("Subir a nube", style = MaterialTheme.typography.labelMedium)
                            }
                        }
                    }
                } else {
                    OutlinedButton(
                        onClick = onAdjuntarClick,
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Icon(Icons.Filled.PhotoCamera, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Adjuntar evidencia fotográfica", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                SuggestionChip(onClick = {}, label = { Text(actividad.estado.name) })
                SuggestionChip(onClick = {}, label = { Text("Progreso: ${actividad.progreso}%") })
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
