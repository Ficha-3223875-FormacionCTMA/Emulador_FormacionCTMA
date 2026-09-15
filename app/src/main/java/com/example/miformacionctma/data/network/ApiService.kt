package com.example.miformacionctma.data.network

import com.example.miformacionctma.data.dto.ActividadDto
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("v1/actividades")
    suspend fun getActividades(): Response<List<ActividadDto>>
}