package com.example.miformacionctma.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

/**
 * Resultado de la consulta transaccional reporte–categoría (punto 5 del
 * laboratorio incremental). Room resuelve el @Relation con una segunda
 * consulta dentro de la misma transacción cuando el DAO usa @Transaction.
 */
data class ReporteConCategoriaEntity(
    @Embedded
    val reporte: ReporteEntity,
    @Relation(
        parentColumn = "categoriaId",
        entityColumn = "id"
    )
    val categoria: CategoriaEntity?
)