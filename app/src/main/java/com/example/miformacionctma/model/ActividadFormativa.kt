package com.example.miformacionctma.model

data class ActividadFormativa(
    val id: Int,
    val titulo: String,
    val descripcion: String = "",
    val fecha: String,
    val estado: String,
    val progreso: Int
)