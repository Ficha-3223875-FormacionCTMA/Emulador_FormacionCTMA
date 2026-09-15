# Mi Formación CTMA - Semana 8: Servicios Web y Resiliencia

## 1. Arquitectura del Sistema
La aplicación sigue el patrón **Repository** con una arquitectura de capas limpia:
- **UI (Compose)**: Observa un `StateFlow` único desde el ViewModel.
- **ViewModel**: Coordina la lógica de negocio y los estados de la interfaz (`Cargando`, `Contenido`, `Error`).
- **Repository**: Actúa como mediador y Fuente Única de Verdad (SSOT). Room es la base local canónica.
- **Remote (Retrofit)**: Fuente de datos externa transformada mediante DTOs.

## 2. Política de Caché
- **Offline-First**: La aplicación siempre muestra primero los datos almacenados en Room.
- **Sincronización**: Al presionar "Actualizar", se consultan los datos remotos. Si la red es exitosa, Room se actualiza de forma consistente.
- **Resiliencia**: Si la red falla (timeout o sin conexión), la base de datos local **no se borra**. El usuario recibe un mensaje de error amigable y sigue viendo sus datos previos.

## 3. Timeouts y Red
- Se ha configurado Retrofit para manejar tiempos de espera estándar (Default 10s).
- El manejo de errores incluye `IOException` para falta de internet y errores HTTP genéricos.

## 4. Autenticación Conceptual
Aunque este incremento usa una URL abierta para demostración, el sistema está diseñado para integrar:
- **Interceptores de OkHttp**: Para adjuntar un `Bearer Token` en el encabezado `Authorization`.
- **Flujo de Token**: Persistencia segura del token en un DataStore cifrado.

## 5. Límites y Consideraciones
- La sincronización actual es de lectura (GET).
- Las operaciones locales (POST/DELETE locales) se guardan en Room inmediatamente para persistencia garantizada.
