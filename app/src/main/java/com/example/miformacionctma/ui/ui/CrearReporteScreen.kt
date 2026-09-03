package com.example.miformacionctma.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.miformacionctma.viewmodel.CrearReporteViewModel

@Composable
fun CrearReporteRoute(
    viewModel: CrearReporteViewModel,
    onGuardado: () -> Unit
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.value.guardadoId) {
        if (uiState.value.guardadoId != null) {
            onGuardado()
        }
    }

    CrearReporteContent(
        uiState = uiState.value,
        onTituloChange = viewModel::actualizarTitulo,
        onGuardar = viewModel::guardar
    )
}

@Composable
fun CrearReporteContent(
    uiState: com.example.miformacionctma.viewmodel.CrearUiState,
    onTituloChange: (String) -> Unit,
    onGuardar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text = "Crear reporte"
        )

        OutlinedTextField(
            value = uiState.titulo,
            onValueChange = onTituloChange,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text("Título")
            },
            isError = uiState.errorTitulo != null,
            supportingText = {
                if (uiState.errorTitulo != null) {
                    Text(uiState.errorTitulo)
                }
            },
            singleLine = true
        )

        Text(
            text = "${uiState.titulo.length}/80"
        )

        Button(
            onClick = onGuardar,
            enabled = !uiState.guardando,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (uiState.guardando) {
                    "Guardando..."
                } else {
                    "Guardar"
                }
            )
        }
    }
}