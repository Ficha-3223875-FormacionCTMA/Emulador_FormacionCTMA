package com.example.miformacionctma.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.miformacionctma.viewmodel.CrearReporteViewModel
import com.example.miformacionctma.viewmodel.OperacionEstado

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearReporteScreen(
    viewModel: CrearReporteViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.operacion) {
        if (uiState.operacion is OperacionEstado.Exitosa) {
            onVolver()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo Reporte") },
                navigationIcon = {
                    Button(onClick = onVolver) {
                        Text("Volver")
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.titulo,
                onValueChange = { viewModel.actualizarTitulo(it) },
                label = { Text("Título de la actividad") },
                isError = uiState.errorTitulo != null,
                supportingText = {
                    uiState.errorTitulo?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            if (uiState.operacion is OperacionEstado.Fallida) {
                Text(
                    text = (uiState.operacion as OperacionEstado.Fallida).mensaje,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.guardar() },
                enabled = uiState.operacion !is OperacionEstado.EnCurso,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.operacion is OperacionEstado.EnCurso) {
                    CircularProgressIndicator()
                } else {
                    Text("Guardar Reporte")
                }
            }
        }
    }
}
