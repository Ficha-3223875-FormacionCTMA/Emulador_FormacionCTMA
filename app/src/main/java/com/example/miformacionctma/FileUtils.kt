package com.example.miformacionctma // O tu paquete correspondiente

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

fun guardarImagenPermanente(context: Context, sourceUri: Uri): String? {
    try {
        val inputStream = context.contentResolver.openInputStream(sourceUri) ?: return null
        val archivoDestino = File(context.filesDir, "evidencia_${System.currentTimeMillis()}.jpg")

        val outputStream = FileOutputStream(archivoDestino)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }
        return archivoDestino.absolutePath
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}