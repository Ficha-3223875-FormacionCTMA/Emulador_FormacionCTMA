package com.example.miformacionctma.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.miformacionctma.data.local.entity.EvidenciaEntity
import com.example.miformacionctma.data.preferences.PreferenciasRepository
import com.example.miformacionctma.data.repository.ActividadRepository
import com.example.miformacionctma.model.ActividadFormativa
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class ActividadesViewModel(
    private val repository: ActividadRepository,
    private val preferenciasRepository: PreferenciasRepository
) : ViewModel() {
    private val _busquedaQuery = MutableStateFlow("")
    val busquedaQuery: StateFlow<String> = _busquedaQuery.asStateFlow()

    private val _operacionState = MutableStateFlow<OperacionUiState>(OperacionUiState.Inactiva)
    val operacionState: StateFlow<OperacionUiState> = _operacionState.asStateFlow()

    // Estado para controlar el proceso de sincronización con la API
    private val _refreshState = MutableStateFlow<RefreshUiState>(RefreshUiState.Inactivo)
    val refreshState: StateFlow<RefreshUiState> = _refreshState.asStateFlow()

    private var refreshJob: Job? = null

    // Evento de refresco/reintento para la UI
    private val _refrescarTrigger = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val uiState: StateFlow<ListadoUiState> = combine(
        preferenciasRepository.obtenerFiltroCompetencia(),
        preferenciasRepository.obtenerOrdenamiento(),
        _busquedaQuery,
        _refrescarTrigger.onStart { emit(Unit) }
    ) { filtro, orden, query, _ ->
        Triple(filtro, orden, query)
    }.flatMapLatest { (filtro, orden, query) ->
        // Búsqueda cancelable automática
        repository.obtenerActividades(filtro, orden, query)
    }.map { lista ->
        if (lista.isEmpty()) ListadoUiState.Vacio else ListadoUiState.Contenido(lista)
    }.catch { throwable ->
        if (throwable is CancellationException) throw throwable
        emit(ListadoUiState.Error(throwable.localizedMessage ?: "Error inesperado"))
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ListadoUiState.Cargando
    )

    init {
        sincronizarConServidor()
    }

    fun onBusquedaChanged(nuevaBusqueda: String) {
        _busquedaQuery.value = nuevaBusqueda
    }

    // Sincronización con el servidor y respaldo local automático
    fun sincronizarConServidor() {
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            _refreshState.value = RefreshUiState.Sincronizando
            val result = repository.refreshActividades()
            result.fold(
                onSuccess = {
                    _refreshState.value = RefreshUiState.Exitoso()
                },
                onFailure = { error ->
                    _refreshState.value = RefreshUiState.Error(error.localizedMessage ?: "Error de sincronización")
                    // Si falla la red (sin conexión o error), insertamos las 10 actividades por defecto
                    precargarActividadesLocalesSiEsNecesario()
                }
            )
        }
    }

    // Método privado para insertar las 10 actividades predeterminadas si la base de datos está vacía
    private fun precargarActividadesLocalesSiEsNecesario() {
        viewModelScope.launch {
            try {
                val actividadesPorDefecto = listOf(
                    ActividadFormativa(id = 1, titulo = "Manifiesto Ágil", fecha = "11 de agosto", estado = "Completada", progreso = 100),
                    ActividadFormativa(id = 2, titulo = "Valores del Manifiesto Ágil", fecha = "12 de agosto", estado = "Completada", progreso = 100),
                    ActividadFormativa(id = 3, titulo = "Principios Ágiles", fecha = "13 de agosto", estado = "Completada", progreso = 100),
                    ActividadFormativa(id = 4, titulo = "Introducción a Scrum", fecha = "14 de agosto", estado = "En proceso", progreso = 75),
                    ActividadFormativa(id = 5, titulo = "Roles de Scrum", fecha = "15 de agosto", estado = "En proceso", progreso = 60),
                    ActividadFormativa(id = 6, titulo = "Artefactos de Scrum", fecha = "16 de agosto", estado = "En proceso", progreso = 50),
                    ActividadFormativa(id = 7, titulo = "Pruebas de software", fecha = "17 de agosto", estado = "Pendiente", progreso = 0),
                    ActividadFormativa(id = 8, titulo = "Tipos de pruebas", fecha = "18 de agosto", estado = "Pendiente", progreso = 0),
                    ActividadFormativa(id = 9, titulo = "Jetpack Compose", fecha = "19 de agosto", estado = "Pendiente", progreso = 0),
                    ActividadFormativa(id = 10, titulo = "Proyecto Mi Formación CTMA", fecha = "20 de agosto", estado = "Pendiente", progreso = 0)
                )
                for (actividad in actividadesPorDefecto) {
                    repository.insertarActividad(actividad)
                }
            } catch (e: Exception) {
                if (e is CancellationException) throw e
            }
        }
    }

    fun reintentar() {
        _refrescarTrigger.tryEmit(Unit)
        sincronizarConServidor()
    }

    fun guardarActividad(actividad: ActividadFormativa) {
        viewModelScope.launch {
            _operacionState.value = OperacionUiState.EnCurso
            try {
                repository.insertarActividad(actividad)
                _operacionState.value = OperacionUiState.Exitosa
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _operacionState.value = OperacionUiState.Fallida(e.localizedMessage ?: "Error al guardar")
            }
        }
    }

    fun eliminarActividad(actividad: ActividadFormativa) {
        viewModelScope.launch {
            _operacionState.value = OperacionUiState.EnCurso
            try {
                repository.eliminarActividad(actividad)
                _operacionState.value = OperacionUiState.Exitosa
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _operacionState.value = OperacionUiState.Fallida(e.localizedMessage ?: "Error al eliminar")
            }
        }
    }

    // =========================================================================
    // Métodos para la gestión de Evidencias Fotográficas (Guía 9)
    // =========================================================================

    fun obtenerEvidencia(actividadId: Long): Flow<EvidenciaEntity?> {
        return repository.obtenerEvidenciaPorActividad(actividadId)
    }

    fun guardarEvidencia(
        actividadId: Long,
        uri: String,
        nombreArchivo: String,
        tamanioBytes: Long,
        tipoMime: String
    ) {
        viewModelScope.launch {
            try {
                val evidencia = EvidenciaEntity(
                    actividadId = actividadId,
                    uri = uri,
                    nombreArchivo = nombreArchivo,
                    tamanioBytes = tamanioBytes,
                    tipoMime = tipoMime,
                    estadoSincronizacion = "PENDIENTE"
                )
                repository.guardarEvidencia(evidencia)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _operacionState.value = OperacionUiState.Fallida(e.localizedMessage ?: "Error al guardar evidencia")
            }
        }
    }

    fun eliminarEvidencia(actividadId: Long) {
        viewModelScope.launch {
            try {
                repository.eliminarEvidenciaPorActividad(actividadId)
            } catch (e: Exception) {
                if (e is CancellationException) throw e
                _operacionState.value = OperacionUiState.Fallida(e.localizedMessage ?: "Error al eliminar evidencia")
            }
        }
    }

    fun resetOperacionState() {
        _operacionState.value = OperacionUiState.Inactiva
    }

    fun resetRefreshState() {
        _refreshState.value = RefreshUiState.Inactivo
    }
}