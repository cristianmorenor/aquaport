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

class TecnicoMantenimientoObserverTest {

    private ByteArrayOutputStream salidaCapturada;
    private TecnicoMantenimientoObserver observador;

    @BeforeEach
    void setUp() {
        salidaCapturada = new ByteArrayOutputStream();
        observador = new TecnicoMantenimientoObserver(new PrintStream(salidaCapturada, true, StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("El técnico recibe la misión afectada y el motivo para revisar la flota")
    void notificarFalloAsignacion_droneEnFallo_escribeOrdenDeRevision() {
        // Arrange
        SolicitudMision solicitud = new SolicitudMision("M-040", ZonaHidrica.LAB_HIDRICO,
                TipoCarga.SENSOR, 200, Prioridad.NORMAL);
        // Act
        observador.notificarFalloAsignacion(solicitud, "El drone AR-15 está en FALLO");
        // Assert
        assertEquals("[Técnico de Mantenimiento] Revisar la flota para la misión M-040: El drone AR-15 está en FALLO"
                        + System.lineSeparator(),
                salidaCapturada.toString(StandardCharsets.UTF_8));
    }

    @Test
    @DisplayName("Crear el observador sin salida lanza NullPointerException")
    void constructor_salidaNula_lanzaExcepcion() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> new TecnicoMantenimientoObserver(null));
    }
}