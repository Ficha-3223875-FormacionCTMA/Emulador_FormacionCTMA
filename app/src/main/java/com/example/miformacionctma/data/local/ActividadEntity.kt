package com.example.miformacionctma.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "actividades")
data class ActividadEntity(
    @PrimaryKey val id: Long, // Cambiado a Long si tus ID son numéricos (como el 1, 2, 3 de tu lista)
    val titulo: String,
    val descripcion: String,
    val competencia: String,
    val fechaEntrega: String,
    val progreso: Int,        // Cambiado de String a Int para manejar el porcentaje correctamente
    val estado: String
)