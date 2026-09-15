package com.example.miformacionctma.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ActividadDto(
    @SerialName("id")
    val id: String,
    @SerialName("titulo")
    val titulo: String,
    @SerialName("descripcion")
    val descripcion: String? = null,
    @SerialName("competencia")
    val competencia: String? = null,
    @SerialName("fecha_entrega")
    val fechaEntrega: String? = null
)


