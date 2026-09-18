package com.example.miformacionctma.data.repository

import com.example.miformacionctma.data.local.EvidenciaDao
import com.example.miformacionctma.data.local.entity.EvidenciaEntity
import kotlinx.coroutines.flow.Flow

class EvidenciaRepository(
    private val evidenciaDao: EvidenciaDao
) {
    fun obtenerEvidenciaPorActividad(actividadId: Long): Flow<EvidenciaEntity?> {
        return evidenciaDao.obtenerEvidenciaPorActividad(actividadId)
    }

    suspend fun guardarEvidencia(evidencia: EvidenciaEntity): Long {
        return evidenciaDao.insertarOActualizarEvidencia(evidencia)
    }

    suspend fun eliminarEvidencia(actividadId: Long) {
        evidenciaDao.eliminarEvidenciaPorActividad(actividadId)
    }
}