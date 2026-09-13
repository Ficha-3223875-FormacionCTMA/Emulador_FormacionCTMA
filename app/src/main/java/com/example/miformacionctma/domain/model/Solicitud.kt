package com.example.miformacionctma.domain.model

data class Solicitud(
    val id: Long = 0L,
    val equipoId: Long,
    val proposito: String,
    val duracionHoras: Int,
    val destino: String,
    val estado: String = "SOLICITADA"
) {
    fun esValida(): Boolean {
        return proposito.length in 10..180 &&
               duracionHoras in 1..8 &&
               destino.isNotBlank()
    }
}
