# Mi Formación CTMA - Persistencia de Datos con Room 💾📱

Este módulo/rama implementa la **persistencia de datos local** para la aplicación **Mi Formación CTMA** utilizando la librería oficial de Jetpack: **Room Database**.

Permite almacenar, consultar, actualizar y eliminar el listado de actividades formativas en la base de datos SQLite del dispositivo, garantizando que la información se conserve incluso al cerrar la aplicación o reiniciar el dispositivo.

---

## 🚀 Características Implementadas

- **Persistencia Local con Room**: Migración de datos en memoria a una base de datos local SQLite.
- **Entidades de Datos (Entities)**: Estructura de la tabla `actividades` mapeada a la clase `ActividadFormativa`.
- **Acceso a Datos (DAO)**: Operaciones CRUD (Crear, Leer, Actualizar, Eliminar) utilizando consultas reactivas con `Flow` de Kotlin.
- **Patrón Repository**: Capa de abstracción que conecta la base de datos local con la interfaz de usuario.
- **Inyección / Instancia de Base de Datos**: Configuración del `RoomDatabase` Singleton con migración / creación inicial de datos.

---

## 🛠️ Tecnologías y Librerías Utilizadas

- **Kotlin** & **Jetpack Compose** (UI Reactiva)
- **Room Persistence Library**:
    - `androidx.room:room-runtime`
    - `androidx.room:room-ktx` (Soporte para Corrutinas y Flow)
    - `ksp` (Kotlin Symbol Processing para la generación de código de Room)
- **Kotlin Coroutines & Flow**: Manejo de operaciones en segundo plano de forma asíncrona.
- **ViewModel & StateFlow**: Gestión de estado reactivo y ciclo de vida de la UI.

---

## 📁 Estructura del Módulo de Persistencia

```text
com.example.miformacionctma/
├── data/
│   ├── local/
│   │   ├── ActividadDao.kt          # Interfaz de acceso a la base de datos (DAO)
│   │   └── AppDatabase.kt           # Clase abstracta RoomDatabase
│   └── repository/
│       └── ActividadesRepository.kt # Repositorio para gestionar la fuente de datos
├── model/
│   └── ActividadFormativa.kt        # Entidad @Entity de Room
└── uii/
    ├── screens/
    │   └── PantallaActividades.kt   # Pantalla principal adaptada a StateFlow
    └── viewmodel/
        └── ActividadesViewModel.kt  # ViewModel con comunicación al Repository