package com.example.miformacionctma.data.network

import com.example.miformacionctma.data.dto.ActividadDto
import retrofit2.Response

class RemoteActividadDataSource(private val apiService: ApiService) {
    suspend fun fetchActividades(): Response<List<ActividadDto>> {
        return apiService.getActividades()
    }
}