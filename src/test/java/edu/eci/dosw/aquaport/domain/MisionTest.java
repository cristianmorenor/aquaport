package edu.eci.dosw.aquaport.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MisionTest {

    private DroneAcuatico droneDisponible;

    @BeforeEach
    void setUp() {
        droneDisponible = new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
    }

    @Test
    @DisplayName("Una misión con todos los datos se crea en estado PENDIENTE")
    void build_datosCompletos_estadoPendiente() {
        // Act
        Mision mision = builderCompleto().build();
        // Assert
        assertEquals(EstadoMision.PENDIENTE, mision.getEstado());
    }

    @Test
    @DisplayName("Una misión sin prioridad explícita queda con prioridad NORMAL")
    void build_sinPrioridad_prioridadNormal() {
        // Act
        Mision mision = builderCompleto().build();
        // Assert
        assertEquals(Prioridad.NORMAL, mision.getPrioridad());
    }

    @Test
    @DisplayName("Una misión creada como CRITICA conserva esa prioridad")
    void build_prioridadCritica_conservaPrioridad() {
        // Act
        Mision mision = builderCompleto().prioridad(Prioridad.CRITICA).build();
        // Assert
        assertEquals(Prioridad.CRITICA, mision.getPrioridad());
    }

    @Test
    @DisplayName("Una misión con prioridad nula lanza IllegalStateException")
    void build_prioridadNula_lanzaExcepcion() {
        // Arrange
        Mision.Builder builder = builderCompleto().prioridad(null);
        // Act + Assert
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("Una misión sin id lanza IllegalStateException")
    void build_sinId_lanzaExcepcion() {
        // Arrange
        Mision.Builder builder = builderCompleto().id(null);
        // Act + Assert
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("Una misión con punto de llegada en blanco lanza IllegalStateException")
    void build_puntoLlegadaEnBlanco_lanzaExcepcion() {
        // Arrange
        Mision.Builder builder = builderCompleto().puntoLlegada("  ");
        // Act + Assert
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("Una misión sin drone lanza IllegalStateException")
    void build_sinDrone_lanzaExcepcion() {
        // Arrange
        Mision.Builder builder = builderCompleto().drone(null);
        // Act + Assert
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("Una misión con un drone no disponible lanza IllegalStateException")
    void build_droneNoDisponible_lanzaExcepcion() {
        // Arrange
        droneDisponible.actualizarEstado(EstadoDrone.RECARGANDO);
        Mision.Builder builder = builderCompleto();
        // Act + Assert
        assertThrows(IllegalStateException.class, builder::build);
    }

    @Test
    @DisplayName("Una misión sin tipo de carga lanza IllegalStateException")
    void build_sinTipoCarga_lanzaExcepcion() {
        // Arrange
        Mision.Builder builder = builderCompleto().tipoCarga(null);
        // Act + Assert
        assertThrows(IllegalStateException.class, builder::build);
    }

    private Mision.Builder builderCompleto() {
        return new Mision.Builder()
                .id("M-001")
                .drone(droneDisponible)
                .puntoPartida("Embalse Norte")
                .puntoLlegada("Laboratorio Hídrico")
                .tipoCarga(TipoCarga.MUESTRA_AGUA);
    }
}