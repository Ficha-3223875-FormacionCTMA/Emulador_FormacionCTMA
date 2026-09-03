package com.example.miformacionctma.uii.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.miformacionctma.model.ActividadFormativa
import com.example.miformacionctma.ui.theme.MiFormacionCTMATheme

@Composable
fun TarjetaActividad(
    actividad: ActividadFormativa
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {

                contentDescription =
                    "Actividad ${actividad.titulo}, " +
                            "fecha ${actividad.fecha}, " +
                            "estado ${actividad.estado}, " +
                            "progreso ${actividad.progreso} por ciento"
            }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = actividad.titulo,
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = actividad.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Fecha: ${actividad.fecha}"
                )

                Text(
                    text = actividad.estado,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Progreso: ${actividad.progreso}%"
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            LinearProgressIndicator(
                progress = {
                    actividad.progreso.coerceIn(0, 100) / 100f
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun TarjetaActividadPreview() {

    MiFormacionCTMATheme {

        TarjetaActividad(

            actividad = ActividadFormativa(
                id = 1,
                titulo = "Actividad de ejemplo",
                descripcion = "Esta es una actividad para probar la tarjeta.",
                fecha = "18 de agosto",
                estado = "En proceso",
                progreso = 60
            )
        )
    }
}