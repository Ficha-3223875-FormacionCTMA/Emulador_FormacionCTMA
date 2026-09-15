package com.example.miformacionctma.data.remote.dto

data class ActividadDto(
    val id: Int,
    val titulo: String,
    val descripcion: String,
    val fecha: String,
    val estado: String,
    val progreso: Int
)
