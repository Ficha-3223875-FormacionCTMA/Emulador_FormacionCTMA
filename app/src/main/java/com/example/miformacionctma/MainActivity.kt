package com.example.miformacionctma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.miformacionctma.ui.AppRoot
import com.example.miformacionctma.ui.theme.MiFormacionCTMATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = (application as MiFormacionApp).container
        setContent {
            MiFormacionCTMATheme {
                AppRoot(container = container)
            }
        }
    }
}

@Composable
fun PantallaInicio(nombre: String = "Aprendiz") {
    // Estado de scroll
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState) // habilita desplazamiento vertical
            .padding(24.dp)
    ) {
        Text(
            text = "Mi Formación CTMA",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Hola, $nombre")
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = "Aquí organizarás actividades y evidencias.\n")

        // Texto largo con scroll
        Text(
            text = """
Que es el Manifiesto Ágil: El Manifiesto Ágil es un conjunto de valores y principios creados para desarrollar software de una forma más flexible, rápida y enfocada en las necesidades del cliente.

Los 4 valores del Manifiesto Ágil

1. Personas e interacciones sobre procesos y herramientas
Yo entiendo que lo más importante es que las personas se comuniquen y trabajen bien en equipo. Las herramientas ayudan, pero si no hay buena comunicación, el proyecto no avanza como debería.

2. Software funcionando sobre documentación extensa
Para mí significa que es mejor entregar un programa que realmente funcione, en lugar de pasar demasiado tiempo escribiendo documentos que al final no aportan tanto.

3. Colaboración con el cliente sobre negociación de contratos
Yo lo veo como mantener una buena relación con el cliente y escucharlo durante todo el proyecto. Así se pueden hacer cambios cuando sean necesarios y el resultado será lo que realmente espera.

4. Responder al cambio sobre seguir un plan
Entiendo que un proyecto puede cambiar en cualquier momento, y en vez de aferrarse al plan inicial, es mejor adaptarse para obtener un mejor resultado.

Los 12 principios del Manifiesto Ágil (explicados de forma humana)

1. Satisfacer al cliente entregando valor desde el principio.
Yo entiendo que es importante mostrar resultados lo antes posible para que el cliente vea avances y se sienta satisfecho.

2. Aceptar los cambios, incluso si aparecen tarde.
Para mí significa que los cambios no son un problema, sino una oportunidad para mejorar el producto.

3. Entregar versiones funcionales con frecuencia.
Creo que es mejor ir entregando avances poco a poco que esperar mucho tiempo para mostrar el proyecto completo.

4. Trabajar siempre junto con el cliente.
Entiendo que el cliente debe participar durante todo el desarrollo para asegurarse de que todo vaya por el camino correcto.

5. Confiar en las personas del equipo.
Yo pienso que cuando cada integrante tiene confianza y apoyo, trabaja con más motivación y el proyecto sale mejor.

6. Hablar directamente cuando sea posible.
Para mí, conversar cara a cara o por videollamada evita malos entendidos y hace que las decisiones sean más rápidas.

7. Lo más importante es que el software funcione.
Entiendo que el verdadero progreso se demuestra cuando el programa hace lo que debe hacer, no solo con documentos o planes.

8. Mantener un ritmo de trabajo constante.
Yo creo que no sirve trabajar al máximo unos días y luego agotarse. Es mejor avanzar de forma constante para mantener la calidad.

9. Buscar siempre la calidad.
Para mí es importante hacer bien las cosas desde el principio para evitar errores y problemas más adelante.

10. Mantener las cosas simples.
Entiendo que no hay que complicar el proyecto con funciones o procesos innecesarios. Lo simple suele funcionar mejor.

11. Dejar que el equipo se organice.
Yo pienso que quienes hacen el trabajo saben cómo organizarse mejor para cumplir los objetivos de la forma más eficiente.

12. Mejorar constantemente.
Para mí significa que, al terminar cada etapa, el equipo debe analizar qué hizo bien, qué salió mal y qué puede hacer mejor la próxima vez.
            """.trimIndent()
        )
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Scrum",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Scrum es un marco de trabajo ágil que ayuda a los equipos a generar valor colaborativo mediante soluciones adaptables para problemas complejos. Se basa en el empirismo y la mejora continua."
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Los 3 Roles de Scrum",
            style = MaterialTheme.typography.titleMedium
        )

        Text("• Product Owner")
        Text("Define qué se debe desarrollar y maximiza el valor del producto administrando el Product Backlog.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("• Scrum Master")
        Text("Guía al equipo, elimina impedimentos y asegura la correcta aplicación de Scrum.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("• Developers")
        Text("Desarrollan el producto de forma autoorganizada y entregan valor en cada Sprint.")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Los 3 Artefactos de Scrum",
            style = MaterialTheme.typography.titleMedium
        )

        Text("• Product Backlog")
        Text("Lista ordenada de todo el trabajo necesario para desarrollar y mejorar el producto.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("• Sprint Backlog")
        Text("Conjunto de tareas seleccionadas para desarrollarse durante el Sprint actual.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("• Incremento")
        Text("Resultado de todo el trabajo completado durante el Sprint que ya funciona y aporta valor.")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Las 5 Ceremonias (Eventos) de Scrum",
            style = MaterialTheme.typography.titleMedium
        )

        Text("1. Sprint")
        Text("Periodo de trabajo de un mes o menos donde se desarrolla el producto.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("2. Sprint Planning")
        Text("Reunión para definir qué se realizará y cómo se cumplirá el objetivo del Sprint.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("3. Daily Scrum")
        Text("Reunión diaria de 15 minutos para revisar el avance del equipo.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("4. Sprint Review")
        Text("Presentación del incremento terminado a los interesados para recibir retroalimentación.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("5. Sprint Retrospective")
        Text("Reunión donde el equipo analiza su trabajo y propone mejoras para el siguiente Sprint.")
        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Pruebas de Software",
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "Las pruebas de software son el proceso técnico para verificar que una aplicación funcione correctamente y cumpla con los requisitos esperados antes de llegar al usuario final. Su objetivo es detectar errores a tiempo, reducir costos y garantizar la calidad del producto."
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "1. Pruebas Funcionales",
            style = MaterialTheme.typography.titleMedium
        )

        Text("• Unitarias")
        Text("Verifican componentes individuales del código, como una función o una clase.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("• Integración")
        Text("Comprueban que los diferentes módulos del software funcionen correctamente al trabajar juntos.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("• Sistema (E2E)")
        Text("Evalúan la aplicación completa simulando el comportamiento real del usuario.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("• Aceptación (UAT)")
        Text("Los usuarios finales validan que el sistema cumpla con las necesidades del negocio.")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "2. Pruebas No Funcionales",
            style = MaterialTheme.typography.titleMedium
        )

        Text("• Rendimiento")
        Text("Evalúan la velocidad, estabilidad y capacidad de respuesta del sistema.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("• Estrés")
        Text("Llevan la aplicación al límite para comprobar cómo responde y se recupera.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("• Seguridad")
        Text("Buscan vulnerabilidades para proteger la información y evitar accesos no autorizados.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("• Usabilidad")
        Text("Miden qué tan fácil e intuitiva resulta la aplicación para los usuarios.")

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "3. Pruebas Relacionadas con el Cambio",
            style = MaterialTheme.typography.titleMedium
        )

        Text("• Regresión")
        Text("Verifican que los cambios recientes no afecten funciones que ya trabajaban correctamente.")

        Spacer(modifier = Modifier.height(8.dp))

        Text("• Humo (Smoke Test)")
        Text("Son pruebas rápidas para comprobar que las funciones principales de la aplicación operan correctamente antes de realizar pruebas más profundas.")
    }
}

@Preview(showBackground = true)
@Composable
fun PantallaInicioPreview() {
    MiFormacionCTMATheme {
        PantallaInicio("Aprendiz")
    }
}