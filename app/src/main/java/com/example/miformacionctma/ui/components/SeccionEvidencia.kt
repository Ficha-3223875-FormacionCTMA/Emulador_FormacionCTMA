package com.example.miformacionctma.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.miformacionctma.data.local.entity.EvidenciaEntity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

@Composable
fun SeccionEvidencia(
    evidencia: EvidenciaEntity?,
    onGuardarEvidencia: (Uri) -> Unit,
    onEliminarEvidencia: () -> Unit
) {
    val context = LocalContext.current
    var tempCameraFile by remember { mutableStateOf<File?>(null) }

    // Copia cualquier archivo de origen a la memoria permanente definitiva de la app
    fun persistirArchivo(sourceUri: Uri): Uri? {
        return try {
            val inputStream = context.contentResolver.openInputStream(sourceUri) ?: return null
            val archivoDestino = File(context.filesDir, "evidencia_${System.currentTimeMillis()}.jpg")

            FileOutputStream(archivoDestino).use { output ->
                inputStream.use { input ->
                    input.copyTo(output)
                }
            }
            Uri.fromFile(archivoDestino)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Photo Picker (Galería)
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { sourceUri ->
            val uriPermanente = persistirArchivo(sourceUri)
            if (uriPermanente != null) {
                onGuardarEvidencia(uriPermanente)
            }
        }
    }

    // Cámara: Toma la foto en caché y la copia inmediatamente a almacenamiento permanente
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { exito ->
        if (exito && tempCameraFile != null && tempCameraFile!!.exists()) {
            try {
                val archivoPermanente = File(context.filesDir, "evidencia_${System.currentTimeMillis()}.jpg")
                tempCameraFile!!.copyTo(archivoPermanente, overwrite = true)

                val uriPermanente = Uri.fromFile(archivoPermanente)
                onGuardarEvidencia(uriPermanente)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun prepararArchivoCamara(): Uri {
        // Usamos cacheDir temporalmente para que la cámara pueda escribir sin restricciones del FileProvider
        val file = File(context.cacheDir, "temp_camera_${System.currentTimeMillis()}.jpg")
        tempCameraFile = file
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(text = "Evidencia Fotográfica", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        if (evidencia != null) {
            AsyncImage(
                model = evidencia.uri,
                contentDescription = "Vista previa de evidencia",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Estado: Guardado permanente",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(onClick = {
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) {
                    Text("Reemplazar")
                }
                Button(
                    onClick = { onEliminarEvidencia() },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            }
        } else {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                OutlinedButton(onClick = {
                    photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) {
                    Text("Galería")
                }
                OutlinedButton(onClick = {
                    val uri = prepararArchivoCamara()
                    cameraLauncher.launch(uri)
                }) {
                    Text("Cámara")
                }
            }
        }
    }
}