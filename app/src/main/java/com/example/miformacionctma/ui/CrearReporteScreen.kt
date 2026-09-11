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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.miformacionctma.viewmodel.CrearReporteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearReporteScreen(
    viewModel: CrearReporteViewModel,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.guardadoId) {
        if (uiState.guardadoId != null) {
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

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { viewModel.guardar() },
                enabled = !uiState.guardando,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (uiState.guardando) {
                    CircularProgressIndicator()
                } else {
                    Text("Guardar Reporte")
                }
            }
        }
    }
}