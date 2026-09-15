package com.example.miformacionctma.data.remote.api

import com.example.miformacionctma.data.remote.dto.ActividadDto
import retrofit2.http.GET

interface ActividadApiService {
    @GET("actividades")
    suspend fun obtenerActividades(): List<ActividadDto>
}
