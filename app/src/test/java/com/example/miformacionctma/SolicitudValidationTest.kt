package com.example.miformacionctma

import com.example.miformacionctma.domain.model.Solicitud
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class SolicitudValidationTest {

    @Test
    fun verificarProposito_menosDe10Caracteres_esInvalida() {
        val solicitud = Solicitud(equipoId = 1, proposito = "Breve", duracionHoras = 5, destino = "Sena")
        assertFalse("Propósito de < 10 caracteres debe ser inválido", solicitud.esValida())
    }

    @Test
    fun verificarProposito_entre10y180Caracteres_esValida() {
        val solicitud = Solicitud(equipoId = 1, proposito = "Este es un propósito válido de más de 10 caracteres", duracionHoras = 5, destino = "Sena")
        assertTrue("Propósito válido debe ser aceptado", solicitud.esValida())
    }

    @Test
    fun verificarDuracion_entre1y8Horas_esValida() {
        val solicitudMin = Solicitud(equipoId = 1, proposito = "Propósito válido largo", duracionHoras = 1, destino = "Sena")
        val solicitudMax = Solicitud(equipoId = 1, proposito = "Propósito válido largo", duracionHoras = 8, destino = "Sena")
        assertTrue("1 hora debe ser válida", solicitudMin.esValida())
        assertTrue("8 horas debe ser válida", solicitudMax.esValida())
    }

    @Test
    fun verificarDuracion_fueraDeRango_esInvalida() {
        val solicitudCero = Solicitud(equipoId = 1, proposito = "Propósito válido largo", duracionHoras = 0, destino = "Sena")
        val solicitudNueve = Solicitud(equipoId = 1, proposito = "Propósito válido largo", duracionHoras = 9, destino = "Sena")
        assertFalse("0 horas debe ser inválida", solicitudCero.esValida())
        assertFalse("9 horas debe ser inválida", solicitudNueve.esValida())
    }

    @Test
    fun verificarDestino_vacio_esInvalida() {
        val solicitud = Solicitud(equipoId = 1, proposito = "Propósito válido largo", duracionHoras = 5, destino = "")
        assertFalse("Destino obligatorio", solicitud.esValida())
    }
}
