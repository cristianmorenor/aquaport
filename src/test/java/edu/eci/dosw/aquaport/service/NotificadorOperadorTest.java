package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneSuperficial;
import edu.eci.dosw.aquaport.domain.Mision;
import edu.eci.dosw.aquaport.domain.TipoCarga;
import edu.eci.dosw.aquaport.domain.ZonaHidrica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class NotificadorOperadorTest {

    private ByteArrayOutputStream salidaCapturada;
    private NotificadorOperador notificador;

    @BeforeEach
    void setUp() {
        salidaCapturada = new ByteArrayOutputStream();
        notificador = new NotificadorOperador(new PrintStream(salidaCapturada, true, StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("La notificación de misión registrada incluye drone, ruta, carga y estado")
    void notificarMisionRegistrada_misionValida_escribeResumen() {
        // Arrange
        Mision mision = new Mision.Builder()
                .id("M-001")
                .drone(new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE))
                .puntoPartida("Embalse Norte")
                .puntoLlegada("Laboratorio Hídrico")
                .tipoCarga(TipoCarga.MUESTRA_AGUA)
                .build();
        // Act
        notificador.notificarMisionRegistrada(mision);
        // Assert
        assertEquals("Misión M-001 registrada: drone AR-01 de Embalse Norte a Laboratorio Hídrico"
                        + " con MUESTRA_AGUA (PENDIENTE)" + System.lineSeparator(),
                salidaCapturada.toString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Los errores se notifican con el prefijo Error")
    void notificarError_mensaje_escribeConPrefijo() {
        // Act
        notificador.notificarError("El drone AR-03 tiene batería insuficiente (18%). Mínimo requerido: 35%.");
        // Assert
        assertEquals("Error: El drone AR-03 tiene batería insuficiente (18%). Mínimo requerido: 35%."
                        + System.lineSeparator(),
                salidaCapturada.toString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Crear el notificador sin salida lanza NullPointerException")
    void constructor_salidaNula_lanzaExcepcion() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> new NotificadorOperador(null));
    }
}