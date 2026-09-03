# 📱 Mi Formación CTMA

Aplicación móvil desarrollada en **Android Studio** con **Kotlin y Jetpack Compose**, creada para consultar y llevar el seguimiento de las actividades formativas.

## 🎯 Objetivo

Permitir visualizar de forma sencilla las actividades de formación, mostrando su descripción, fecha, estado y porcentaje de progreso.

## ✨ Funcionalidades

* 📋 Visualización de actividades formativas.
* 📅 Fecha de cada actividad.
* 🔄 Estado: completada, en proceso o pendiente.
* 📊 Barra de progreso.
* 📂 Tarjetas desplegables para mostrar detalles adicionales.
* 📱 Interfaz adaptable a diferentes tamaños de pantalla.

## 🛠️ Tecnologías

* **Kotlin**
* **Android Studio**
* **Jetpack Compose**
* **Git**
* **GitHub**

## 📁 Estructura principal

```text
app/
└── src/main/java/com/example/miformacionctma/
    ├── model/
    │   └── ActividadFormativa.kt
    ├── ui/
    │   ├── components/
    │   │   └── TarjetaActividad.kt
    │   ├── screens/
    │   │   └── PantallaActividades.kt
    │   └── theme/
    └── MainActivity.kt
```

## 📊 Actividades

La aplicación contiene actividades relacionadas con temas como:

* Manifiesto Ágil
* Scrum
* Pruebas de software
* Jetpack Compose
* Proyecto Mi Formación CTMA

Cada actividad muestra su respectivo progreso y estado.

## 🧪 Pruebas

La aplicación fue probada en **Android Emulator – Pixel 7 API 34**, verificando la visualización de las actividades y el funcionamiento de las tarjetas desplegables.

## 🔀 Control de versiones

El proyecto utiliza **Git y GitHub** para registrar y compartir los cambios realizados durante el desarrollo.

## 👨‍💻 Autor

**Andrés Castañeda**

Proyecto académico desarrollado para el programa **Análisis y Desarrollo de Software (ADSO) – SENA CTMA**.

## 📌 Estado

🟢 En desarrollo.


## GUIA 1 -- preguntas de comprension
¿Qué diferencia práctica encuentras entre una aplicación móvil y una página web?
Respuesta: Una aplicación móvil se instala en el celular y está diseñada especialmente para usarse desde ahí, mientras que una página web se abre desde un navegador.

¿Qué función cumple un sistema operativo como Android?
Respuesta: Es el sistema que permite que el celular funcione y que las aplicaciones puedan ejecutarse y utilizar los recursos del dispositivo.

¿Qué es una variable? Escribe un ejemplo relacionado con una actividad formativa.
Respuesta: Una variable es un espacio donde podemos guardar un dato que puede cambiar. Por ejemplo, una variable llamada progreso puede guardar el porcentaje de avance de una actividad.

¿Qué estructura usarías para decidir si una actividad está vencida?
Respuesta: Usaría una estructura if, porque permite comparar la fecha de la actividad con la fecha actual y decidir si ya está vencida.

¿Qué resultado esperas de una lista que almacena actividades?
Respuesta: Espero que me permita guardar varias actividades y poder mostrarlas, consultarlas y recorrerlas fácilmente.

¿Para qué sirve un sistema de control de versiones?
Respuesta: Sirve para guardar los cambios que se hacen en un proyecto y poder saber qué se modificó. También permite volver a una versión anterior si es necesario.

¿Qué información nunca debería subirse a un repositorio público?
Respuesta: No debería subir contraseñas, claves, datos personales, tokens ni información privada de los usuarios.

¿Qué harías primero si una aplicación se cierra inesperadamente?
Respuesta: Primero revisaría qué estaba haciendo cuando se cerró y buscaría el error en los mensajes o registros de la aplicación para saber qué lo causó.

Explica con tus palabras qué significa “probar” una aplicación.
Respuesta: Probar una aplicación significa usarla y revisar que sus funciones trabajen correctamente y que no tenga errores que afecten al usuario.

Identifica dos riesgos de privacidad en una app que almacena datos de aprendices.
Respuesta: Un riesgo sería que alguien pueda acceder a los datos sin autorización. Otro sería que la información personal de los aprendices se filtre o se comparta sin permiso.


## GUIA 2 - preguntas para validar aprendizaje
¿Por qué elegiste val o var en un dato específico?
Respuesta: Usé val cuando el dato no necesitaba cambiar, por ejemplo el título o la fecha de una actividad. Usé var cuando el dato podía cambiar durante el funcionamiento de la aplicación, como el estado de una tarjeta desplegable.

¿Qué pasaría si la lista estuviera vacía?
Respuesta: No se mostrarían actividades. Lo ideal es controlar ese caso y mostrar un mensaje diciendo que no hay actividades registradas, en vez de dejar la pantalla vacía.

¿Dónde podría aparecer null y cómo lo controlaste?
Respuesta: null podría aparecer cuando un dato no tenga ningún valor. Lo controlaría comprobando primero si el dato existe antes de utilizarlo, para evitar que la aplicación se cierre por un error.

¿Por qué una regla no debería estar duplicada dentro del Composable?
Respuesta: Porque si la misma regla está repetida, el código se vuelve más difícil de mantener y modificar. Es mejor tenerla en un solo lugar para que sea más fácil cambiarla cuando sea necesario.

Modifica el criterio de urgencia de dos a tres días y demuestra el resultado.
Respuesta: Cambiaría la condición que considera una actividad como urgente para que tenga en cuenta las actividades cuya fecha de entrega esté a tres días o menos. De esta manera, una actividad que antes no aparecía como urgente por estar a tres días, ahora sí sería marcada como urgente.

# Incremento Semana 3 - Mi Formación CTMA

## PantallaActividades.kt
- Se construyó un **Scaffold** con `TopAppBar` para el título principal.
- Se implementó un **estado vacío** con mensaje y botón de acción.
- Se agregó un **LazyColumn** con encabezado y lista de actividades usando clave estable (`id`).
- Se integró el componente **TarjetaActividad** para mostrar cada actividad.
- Se definió una lista de **10 actividades de ejemplo** con diferentes estados y progreso.
- Se aplicó **MaterialTheme** para tipografía y estilos.
- Se crearon **Previews** para la pantalla principal y el estado vacío.

## Adaptación y Previews adicionales
- Se implementó `ContenidoAdaptable` con `BoxWithConstraints` para cambiar entre `LazyColumn` y `LazyVerticalGrid` según el ancho disponible.
- Se añadieron dos previews extra:
  - **Fuente grande** (`fontScale = 1.5f`).
  - **Ancho ampliado** (`widthDp = 700`).
- Esto permite validar accesibilidad y diseño adaptable en diferentes configuraciones.

# Pruebas y Scrum

## 📖 Semana 2 – Pruebas de software y SCRUM

### Historias de usuario
- **HU-01 Reserva de actividad**  
  *Como aprendiz, quiero reservar una actividad formativa con fecha y hora, para asegurar mi participación.*

- **HU-02 Finalización con evidencia**  
  *Como aprendiz, quiero marcar una actividad como completada con evidencia, para que el sistema registre mi progreso.*

### Criterios de aceptación
- **HU-01**
  - Dado que selecciono una actividad y una fecha disponible, cuando confirmo la reserva, entonces el sistema guarda la reserva y muestra confirmación.
  - Dado que intento reservar en una fecha ocupada, cuando confirmo, entonces el sistema rechaza y muestra mensaje de error.
  - Dado que cancelo una reserva antes de la fecha, cuando confirmo la cancelación, entonces el sistema libera el cupo.

- **HU-02**
  - Dado que adjunto evidencia válida, cuando marco la actividad como completada, entonces el sistema cambia estado a COMPLETADA.
  - Dado que intento finalizar sin evidencia, cuando confirmo, entonces el sistema rechaza la acción y muestra mensaje.
  - Dado que intento finalizar una actividad no asignada, cuando confirmo, entonces el sistema bloquea el acceso.

### Requisito no funcional
- *El 95% de las reservas debe responder en máximo 2 segundos con 100 usuarios concurrentes.*

### Matriz de riesgos
| Riesgo | Prob. | Impacto | Exposición | Prioridad |
|--------|-------|---------|------------|-----------|
| Reserva duplicada | 3 | 4 | 12 | Alta |
| Finalización sin evidencia | 4 | 5 | 20 | Muy alta |
| Acceso a actividades no asignadas | 4 | 5 | 20 | Muy alta |
| Texto desalineado | 2 | 1 | 2 | Baja |

### Plan de pruebas v1
- **Objetivo:** Validar reservas y finalización con evidencia.
- **Alcance incluido:** Reservas, cancelaciones, finalización con evidencia.
- **Fuera de alcance:** Pagos y facturación.
- **Base de prueba:** Historias, criterios, prototipo de PantallaActividades.
- **Riesgos:** duplicidad, evidencia faltante, acceso indebido.
- **Enfoque:** pruebas funcionales, de integración y de aceptación.
- **Ambiente:** App Android con datos sintéticos.
- **Roles:** Tester, desarrollador, PO.
- **Criterios de entrada:** Historias revisadas, ambiente desplegado.
- **Criterios de salida:** 100% de casos críticos ejecutados, cero defectos críticos abiertos.

---

## 🧪 Semana 3 – Diseño sistemático de casos y gestión de defectos

### Casos de prueba
- **Positivos:** CP-01 Reserva válida, CP-02 Finalización con evidencia, CP-03 Cancelación antes de fecha.
- **Negativos:** CP-04 Reserva ocupada, CP-05 Finalización sin evidencia, CP-06 Finalización no asignada, CP-07 Cancelación tardía.
- **Partición de equivalencia:** CP-FOTO-01 Formato válido, CP-FOTO-02 Formato inválido, CP-FOTO-03 Tamaño > 5MB.
- **Valores límite:** CP-TXT-01 Observación con 9 caracteres (FAIL), CP-TXT-02 Observación con 10 caracteres (PASS).
- **Tabla de decisión:** 4 reglas (asignada+foto+receptor → ENTREGADA; no asignada → denegado; foto inválida → solicitar foto; receptor no identificado → solicitar receptor).
- **Transiciones de estado:** CREAR→ASIGNAR→EN_RUTA→ENTREGADA (válida), EN_RUTA→NO_ENTREGADA (válida), CANCELADA→ENTREGADA (inválida), ENTREGADA→EN_RUTA (inválida).

### Registro de defectos
| ID | Caso origen | Título | Severidad | Prioridad | Estado | Ref. |
|----|-------------|--------|-----------|-----------|--------|------|
| BUG-001 | CP-05 | Finalización sin evidencia aceptada | Crítica | P1 | Nuevo | HU-02 / CA-01 |
| BUG-002 | CP-04 | Reserva duplicada en fecha ocupada | Alta | P1 | Nuevo | HU-01 / CA-02 |

### Reporte reproducible
- **ID:** BUG-001
- **Título:** Finalización sin evidencia aceptada.
- **Ambiente:** Staging v0.3.2; Pixel 7; datos sintéticos.
- **Referencia:** HU-02 / CA-01 / CP-05.
- **Precondición:** Actividad asignada al aprendiz.
- **Pasos:** 1) Abrir actividad asignada. 2) Intentar finalizar sin evidencia.
- **Resultado esperado:** Rechazo con mensaje.
- **Resultado real:** El sistema acepta y marca COMPLETADA.
- **Severidad/Prioridad:** Crítica / P1.
- **Estado inicial:** Nuevo.

### Matriz de trazabilidad
| Historia | Criterio | Riesgo | Caso | Resultado | Defecto |
|----------|----------|--------|------|-----------|---------|
| HU-01 | CA-01 Reserva válida | Duplicidad | CP-01 | PASS | — |
| HU-01 | CA-02 Fecha ocupada | Duplicidad | CP-04 | FAIL | BUG-002 |
| HU-02 | CA-01 Evidencia obligatoria | Evidencia faltante | CP-02 | PASS | — |
| HU-02 | CA-01 Evidencia obligatoria | Evidencia faltante | CP-05 | FAIL | BUG-001 |
| HU-02 | CA-02 Solo asignada | Acceso indebido | CP-06 | FAIL | — |

---

## 📋 Checklist UX / Accesibilidad

| Criterio | Evidencia en el proyecto |
|----------|--------------------------|
| Contraste | Tipografía y colores Material 3 garantizan contraste. |
| Orden de lectura | Jerarquía clara: título → subtítulo → lista. |
| Escalado de fuente | Preview con `fontScale = 1.5f` muestra adaptación. |
| Zonas táctiles | Botón “Actualizar” y tarjetas con padding ≥48dp. |
| Diseño adaptable | `BoxWithConstraints` alterna lista y grid según ancho. |
| Estado vacío | Mensaje y acción clara cuando no hay actividades. |
| Evidencia visual | Capturas: lista, estado vacío, fuente grande, grid. |

---



