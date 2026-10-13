package edu.eci.dosw.aquaport.service.observador;

import edu.eci.dosw.aquaport.domain.Prioridad;
import edu.eci.dosw.aquaport.domain.SolicitudMision;
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

class CentroControlObserverTest {

    private ByteArrayOutputStream salidaCapturada;
    private CentroControlObserver observador;

    @BeforeEach
    void setUp() {
        salidaCapturada = new ByteArrayOutputStream();
        observador = new CentroControlObserver(new PrintStream(salidaCapturada, true, StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("El centro de control registra la misión, su prioridad y el motivo de la alerta")
    void notificarFalloAsignacion_misionCritica_escribeAlertaConPrioridad() {
        // Arrange
        SolicitudMision solicitud = new SolicitudMision("M-050", ZonaHidrica.LAB_HIDRICO,
                TipoCarga.MUESTRA_AGUA, 200, Prioridad.CRITICA);
        // Act
        observador.notificarFalloAsignacion(solicitud, "No hay drones disponibles");
        // Assert
        assertEquals("[Centro de Control] Misión M-050 (CRITICA): No hay drones disponibles" + System.lineSeparator(),
                salidaCapturada.toString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Crear el observador sin salida lanza NullPointerException")
    void constructor_salidaNula_lanzaExcepcion() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> new CentroControlObserver(null));
    }
}