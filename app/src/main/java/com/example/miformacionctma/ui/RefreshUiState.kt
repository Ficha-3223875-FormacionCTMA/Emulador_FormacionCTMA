package com.example.miformacionctma.ui

sealed interface RefreshUiState {
    data object Inactivo : RefreshUiState
    data object Sincronizando : RefreshUiState
    data class Exitoso(val timestamp: Long = System.currentTimeMillis()) : RefreshUiState
    data class Error(val mensaje: String) : RefreshUiState
}