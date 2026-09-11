package com.example.miformacionctma.uii.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
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
                style = MaterialTheme.typography.titleMedium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(6.dp)
            )

            Text(
                text = actividad.descripcion,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            EtiquetaEstado(
                estado = actividad.estado
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Fecha: ${actividad.fecha}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = "Progreso: ${actividad.progreso}%",
                style = MaterialTheme.typography.bodySmall
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


/**
 * Badge de color según el estado de la actividad.
 */
@Composable
private fun EtiquetaEstado(
    estado: String
) {

    val (colorFondo, colorTexto) = when (estado) {

        "Completada" -> Color(0xFFDFF5E1) to Color(0xFF1E7B34)

        "En proceso" -> Color(0xFFFFF3CD) to Color(0xFF8A6D00)

        "Pendiente" -> Color(0xFFF0F0F0) to Color(0xFF616161)

        else -> MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
    }

    Surface(
        color = colorFondo,
        shape = RoundedCornerShape(50),
        modifier = Modifier
    ) {

        Text(
            text = estado,
            style = MaterialTheme.typography.labelMedium,
            color = colorTexto,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(
                horizontal = 10.dp,
                vertical = 4.dp
            )
        )
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