package com.example.miformacionctma.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.miformacionctma.data.local.ActividadEntity

@Entity(
    tableName = "evidencias",
    foreignKeys = [
        ForeignKey(
            entity = ActividadEntity::class,
            parentColumns = ["id"],
            childColumns = ["actividadId"],
            onDelete = CASCADE
        )
    ],
    indices = [Index(value = ["actividadId"])]
)
data class EvidenciaEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val actividadId: Long,
    val uri: String,
    val nombreArchivo: String,
    val tamanioBytes: Long,
    val tipoMime: String,
    val estadoSincronizacion: String
)