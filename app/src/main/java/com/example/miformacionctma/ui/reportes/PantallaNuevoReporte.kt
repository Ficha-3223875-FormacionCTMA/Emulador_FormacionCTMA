package com.example.miformacionctma.ui.reportes

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.miformacionctma.domain.model.Categoria

@Composable
fun PantallaNuevoReporte(
    categorias: List<Categoria>,
    onGuardar: (titulo: String, descripcion: String, categoriaId: Long?) -> Unit,
    onCancelar: () -> Unit
) {
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var categoriaId by remember { mutableStateOf<Long?>(null) }
    var expandido by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Nuevo reporte", style = MaterialTheme.typography.headlineSmall)

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        )
        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
        )

        Column(modifier = Modifier.padding(top = 12.dp)) {
            TextButton(onClick = { expandido = true }) {
                Text(
                    categorias.firstOrNull { it.id == categoriaId }?.nombre
                        ?: "Seleccionar categoría"
                )
            }
            DropdownMenu(expanded = expandido, onDismissRequest = { expandido = false }) {
                categorias.forEach { categoria ->
                    DropdownMenuItem(
                        text = { Text(categoria.nombre) },
                        onClick = { categoriaId = categoria.id; expandido = false }
                    )
                }
            }
        }

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
        }

        Button(
            onClick = {
                if (titulo.isBlank()) {
                    error = "El título es obligatorio."
                } else {
                    onGuardar(titulo.trim(), descripcion.trim(), categoriaId)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp)
        ) {
            Text("Guardar")
        }
        TextButton(onClick = onCancelar, modifier = Modifier.fillMaxWidth()) {
            Text("Cancelar")
        }
    }
}