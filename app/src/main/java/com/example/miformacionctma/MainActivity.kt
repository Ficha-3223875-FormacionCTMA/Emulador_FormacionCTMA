package com.example.miformacionctma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.example.miformacionctma.model.AppDatabase
import com.example.miformacionctma.ui.TareasScreen
import com.example.miformacionctma.ui.theme.MiFormacionCTMATheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "tareas_db"
        ).build()

        val repository = TareaRepository(db.tareaDao())
        val viewModelFactory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return TareaViewModel(repository) as T
            }
        }
        val viewModel = ViewModelProvider(this, viewModelFactory)[TareaViewModel::class.java]

        setContent {
            MiFormacionCTMATheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val listaTareas by viewModel.tareas.collectAsState()

                    // Ajustado: Se coloca el padding dentro de un Box contenedor
                    Box(modifier = Modifier.padding(innerPadding)) {
                        TareasScreen(
                            listaTareas = listaTareas,
                            onAgregarTarea = { titulo, descripcion ->
                                viewModel.agregarTarea(titulo, descripcion)
                            },
                            onEliminarTarea = { tarea ->
                                viewModel.eliminarTarea(tarea)
                            }
                        )
                    }
                }
            }
        }
    }
}


// --- COMPONENTES VISUALES SECUNDARIOS (Formación CTMA) ---

data class ActividadFormativa(
    val id: Int,
    val titulo: String,
    val fecha: String,
    val estado: String,
    val progreso: Int
)

val actividades = listOf(
    ActividadFormativa(1, "Manifiesto Ágil", "11 de agosto", "Completada", 100),
    ActividadFormativa(2, "Valores del Manifiesto Ágil", "12 de agosto", "Completada", 100),
    ActividadFormativa(3, "Principios Ágiles", "13 de agosto", "Completada", 100),
    ActividadFormativa(4, "Introducción a Scrum", "14 de agosto", "En proceso", 75),
    ActividadFormativa(5, "Roles de Scrum", "15 de agosto", "En proceso", 60),
    ActividadFormativa(6, "Artefactos de Scrum", "16 de agosto", "En proceso", 50),
    ActividadFormativa(7, "Pruebas de software", "17 de agosto", "Pendiente", 0),
    ActividadFormativa(8, "Tipos de pruebas", "18 de agosto", "Pendiente", 0),
    ActividadFormativa(9, "Jetpack Compose", "19 de agosto", "Pendiente", 0),
    ActividadFormativa(10, "Proyecto Mi Formación CTMA", "20 de agosto", "Pendiente", 0)
)

@Composable
fun PantallaFormacion(
    lista: List<ActividadFormativa> = actividades
) {
    Surface(
        modifier = Modifier.fillMaxSize()
    ) {
        if (lista.isEmpty()) {
            MensajeSinActividades()
        } else {
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                // Se lee explícitamente maxWidth del scope de BoxWithConstraints
                val anchoGrande = this.maxWidth >= 600.dp

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp)
                ) {
                    Encabezado(
                        cantidad = lista.size
                    )

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    if (anchoGrande) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = lista,
                                key = { actividad -> actividad.id }
                            ) { actividad ->
                                Tarjeta(
                                    actividad = actividad
                                )
                            }
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(
                                items = lista,
                                key = { actividad -> actividad.id }
                            ) { actividad ->
                                Tarjeta(
                                    actividad = actividad
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Encabezado(cantidad: Int) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = "Mi Formación CTMA", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "Seguimiento de actividades formativas", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(10.dp))
        Text(text = "$cantidad actividades registradas", style = MaterialTheme.typography.labelLarge)
    }
}

@Composable
fun Tarjeta(actividad: ActividadFormativa) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .semantics {
                contentDescription = "${actividad.titulo}, fecha ${actividad.fecha}, estado ${actividad.estado}, progreso ${actividad.progreso} por ciento"
            }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = actividad.titulo, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Fecha")
                Text(text = actividad.fecha)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "Estado")
                Text(text = actividad.estado)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(text = "Progreso: ${actividad.progreso}%")
            Spacer(modifier = Modifier.height(5.dp))
            LinearProgressIndicator(
                progress = { actividad.progreso / 100f },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun MensajeSinActividades() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(24.dp)) {
            Text(text = "No hay actividades", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Todavía no tienes actividades registradas.")
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { }) {
                Text(text = "Actualizar")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VistaFormacion() {
    MiFormacionCTMATheme {
        PantallaFormacion()
    }
}