package edu.eci.dosw.aquaport.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SolicitudMisionTest {

    @Test
    @DisplayName("Una solicitud con prioridad CRITICA se reconoce como crítica")
    void isCritica_prioridadCritica_retornaTrue() {
        // Arrange
        SolicitudMision solicitud = new SolicitudMision("M-010", ZonaHidrica.LAB_HIDRICO,
                TipoCarga.MUESTRA_AGUA, 200, Prioridad.CRITICA);
        // Act
        boolean critica = solicitud.isCritica();
        // Assert
        assertTrue(critica);
    }

    @Test
    @DisplayName("Una solicitud con prioridad ALTA no es crítica")
    void isCritica_prioridadAlta_retornaFalse() {
        // Arrange
        SolicitudMision solicitud = new SolicitudMision("M-011", ZonaHidrica.LAB_HIDRICO,
                TipoCarga.SENSOR, 200, Prioridad.ALTA);
        // Act
        boolean critica = solicitud.isCritica();
        // Assert
        assertFalse(critica);
    }

    @Test
    @DisplayName("Una carga de 0 gramos lanza IllegalArgumentException")
    void constructor_pesoCero_lanzaExcepcion() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new SolicitudMision("M-012",
                ZonaHidrica.LAB_HIDRICO, TipoCarga.SENSOR, 0, Prioridad.NORMAL));
    }

    @Test
    @DisplayName("Una carga de 1501 gramos lanza IllegalArgumentException")
    void constructor_pesoMayorA1500_lanzaExcepcion() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new SolicitudMision("M-013",
                ZonaHidrica.LAB_HIDRICO, TipoCarga.EQUIPO_MEDICION, 1501, Prioridad.NORMAL));
    }

    @Test
    @DisplayName("Una solicitud con id vacío lanza IllegalArgumentException")
    void constructor_idVacio_lanzaExcepcion() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new SolicitudMision(" ",
                ZonaHidrica.LAB_HIDRICO, TipoCarga.SENSOR, 200, Prioridad.NORMAL));
    }

    @Test
    @DisplayName("Una solicitud sin zona de destino lanza NullPointerException")
    void constructor_zonaDestinoNula_lanzaExcepcion() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> new SolicitudMision("M-014",
                null, TipoCarga.SENSOR, 200, Prioridad.NORMAL));
    }

    @Test
    @DisplayName("Una solicitud con id nulo lanza IllegalArgumentException")
    void constructor_idNulo_lanzaExcepcion() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> new SolicitudMision(null,
                ZonaHidrica.LAB_HIDRICO, TipoCarga.SENSOR, 200, Prioridad.NORMAL));
    }
}
