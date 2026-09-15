# Mi Formación CTMA - Guía #8

## Arquitectura del Proyecto
La aplicación sigue una arquitectura **MVVM (Model-View-ViewModel)** con el patrón **Repository**, implementando una estrategia **Offline-first**.

### Flujo de Datos
1.  **Fuente de Verdad Única (SSoT):** La base de datos local **Room** es la única fuente de datos para la interfaz de usuario.
2.  **Sincronización:** El repositorio coordina la descarga de datos desde una API REST (vía **Retrofit**) y actualiza Room de forma atómica.
3.  **Resiliencia:** Si la red falla o hay un timeout, la aplicación captura la excepción y mantiene los datos locales intactos, informando al usuario a través de estados de UI.

## Configuración Técnica
-   **Timeouts:** OkHttp está configurado con un interceptor de logs para monitorear el tráfico. Los tiempos de espera son los predeterminados de Retrofit (10s).
-   **Caché:** Se utiliza Room como caché persistente. Los datos no se borran si la actualización remota falla.
-   **Serialización:** Se utiliza **GSON** para transformar el JSON de la API en objetos DTO (`ActividadDto`).
-   **Mapeo:** Existe una capa de mapeo (`ReporteMapper`) que transforma DTOs a Entidades de Room, manteniendo el desacoplamiento.

## Estados de Sincronización
La UI gestiona los siguientes estados a través de `SyncState`:
-   `Cargando`: Sincronización en curso.
-   `Exito`: Datos actualizados correctamente.
-   `Error`: Fallo de red (se ofrece botón de reintento).

## Pruebas
Se incluye una suite de pruebas unitarias en `SincronizacionResilienciaTest` que utiliza:
-   **MockWebServer:** Para simular respuestas del servidor (200 OK, 500 Error).
-   **Room In-Memory:** Para verificar la persistencia sin afectar la base de datos real.
-   **Robolectric:** Para simular el entorno de Android en tests locales.

## Seguridad
-   No se incluyen tokens reales ni credenciales.
-   Se utiliza `https` para todas las comunicaciones (configurado en el `NetworkModule`).
