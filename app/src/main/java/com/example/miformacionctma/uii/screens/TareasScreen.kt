package com.example.miformacionctma.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.miformacionctma.model.TareaEntity

@Composable
fun TareasScreen(
    listaTareas: List<TareaEntity>,
    onAgregarTarea: (String, String) -> Unit,
    onEliminarTarea: (TareaEntity) -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Registro de Tareas",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- FORMULARIO DE REGISTRO ---
        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título de la tarea") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                if (titulo.isNotBlank()) {
                    onAgregarTarea(titulo, descripcion)
                    titulo = ""
                    descripcion = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Tarea")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Lista de Tareas Registradas",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        // --- LISTA DE TAREAS QUE VIENEN DE LA BASE DE DATOS ---
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listaTareas, key = { it.id }) { tarea ->
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(text = tarea.titulo, style = MaterialTheme.typography.titleMedium)
                            if (tarea.descripcion.isNotBlank()) {
                                Text(text = tarea.descripcion, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                        IconButton(onClick = { onEliminarTarea(tarea) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Eliminar tarea",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}