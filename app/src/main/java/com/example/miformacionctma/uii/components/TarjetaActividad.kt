package com.example.miformacionctma.uii.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding

import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp

import com.example.miformacionctma.model.ActividadFormativa


@Composable
fun TarjetaActividad(
    actividad: ActividadFormativa
) {

    // Guarda si la tarjeta está abierta o cerrada
    var expandida by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                // Se agrega la fecha a la descripción accesible para TalkBack (CP-03)
                contentDescription =
                    "Actividad ${actividad.titulo}, " +
                            "fecha de entrega ${actividad.fecha}, " +
                            "estado ${actividad.estado}, " +
                            "progreso ${actividad.progreso} por ciento"
            }
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    // Abrir o cerrar la tarjeta
                    expandida = !expandida
                }
                .padding(16.dp)
        ) {

            // ─────────────────────────────
            // TÍTULO + FLECHA
            // ─────────────────────────────

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = actividad.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = if (expandida) "▲" else "▼",
                    style = MaterialTheme.typography.titleMedium
                )
            }


            Spacer(
                modifier = Modifier.height(8.dp)
            )


            // ─────────────────────────────
            // DESCRIPCIÓN
            // ─────────────────────────────

            Text(
                text = actividad.descripcion,
                style = MaterialTheme.typography.bodyMedium
            )


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            // ─────────────────────────────
            // FECHA + ESTADO (CUMPLIMIENTO HU)
            // ─────────────────────────────

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Fecha: ${actividad.fecha}",
                    style = MaterialTheme.typography.bodyMedium
                )

                Text(
                    text = actividad.estado,
                    style = MaterialTheme.typography.labelLarge
                )
            }


            Spacer(
                modifier = Modifier.height(12.dp)
            )


            // ─────────────────────────────
            // PROGRESO
            // ─────────────────────────────

            Text(
                text = "Progreso: ${actividad.progreso}%"
            )


            Spacer(
                modifier = Modifier.height(6.dp)
            )


            LinearProgressIndicator(
                progress = {
                    actividad.progreso / 100f
                },
                modifier = Modifier.fillMaxWidth()
            )


            // ─────────────────────────────
            // CONTENIDO DESPLEGABLE
            // ─────────────────────────────

            AnimatedVisibility(
                visible = expandida,

                enter =
                    expandVertically() +
                            fadeIn(),

                exit =
                    shrinkVertically() +
                            fadeOut()
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 14.dp)
                ) {

                    HorizontalDivider()

                    Spacer(
                        modifier = Modifier.height(14.dp)
                    )


                    Text(
                        text = "Detalles de la actividad",
                        style = MaterialTheme.typography.titleMedium
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


                    Text(
                        text = "📅 Fecha: ${actividad.fecha}"
                    )


                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )


                    Text(
                        text = when (actividad.estado) {

                            "Completada" ->
                                "✅ Estado: Actividad completada"

                            "En proceso" ->
                                "🟡 Estado: Actividad en proceso"

                            else ->
                                "⚪ Estado: Actividad pendiente"
                        }
                    )


                    Spacer(
                        modifier = Modifier.height(6.dp)
                    )


                    Text(
                        text = "📊 Progreso actual: ${actividad.progreso}%"
                    )
                }
            }
        }
    }
}