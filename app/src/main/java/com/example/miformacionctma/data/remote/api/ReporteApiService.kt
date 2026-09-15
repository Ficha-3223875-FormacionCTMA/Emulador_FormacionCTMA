package com.example.miformacionctma.data.remote.api

import com.example.miformacionctma.data.remote.dto.ActividadDto
import retrofit2.http.GET

interface ReporteApiService {
    @GET("actividades") // URL ficticia para la guía
    suspend fun obtenerActividades(): List<ActividadDto>
}
