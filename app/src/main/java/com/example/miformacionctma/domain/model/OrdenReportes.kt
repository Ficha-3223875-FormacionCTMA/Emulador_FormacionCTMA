package com.example.miformacionctma.domain.model

/**
 * Preferencia de orden que el usuario elige para la lista de reportes.
 * Se guarda en DataStore y se conserva entre reinicios (PA-05).
 */
enum class OrdenReportes {
    FECHA_DESC,
    FECHA_ASC,
    TITULO_ASC;

    companion object {
        fun fromNombre(nombre: String?): OrdenReportes =
            entries.firstOrNull { it.name == nombre } ?: FECHA_DESC
    }
}

/** Agrupa las preferencias persistentes que interesan a la pantalla de reportes. */
data class PreferenciasUsuario(
    val orden: OrdenReportes = OrdenReportes.FECHA_DESC,
    val categoriaFiltroId: Long? = null,
    val actividadesFiltroEstado: String? = null,
    val actividadesOrdenDesc: Boolean = true
)