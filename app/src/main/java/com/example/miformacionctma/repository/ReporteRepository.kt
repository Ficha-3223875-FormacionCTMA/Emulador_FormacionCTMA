package com.example.miformacionctma.repository

import com.example.miformacionctma.model.Reporte
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface ReporteRepository {
    val reportes: StateFlow<List<Reporte>>
    fun agregar(reporte: Reporte)
}

class ReporteRepositoryImpl : ReporteRepository {

    private val _reportes = MutableStateFlow<List<Reporte>>(emptyList())

    override val reportes: StateFlow<List<Reporte>> =
        _reportes.asStateFlow()

    override fun agregar(reporte: Reporte) {
        _reportes.value = _reportes.value + reporte
    }
}
