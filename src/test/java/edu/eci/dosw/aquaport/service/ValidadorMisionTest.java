package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.DroneSuperficial;
import edu.eci.dosw.aquaport.domain.EstadoDrone;
import edu.eci.dosw.aquaport.domain.ZonaHidrica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidadorMisionTest {

    private ValidadorMision validador;

    @BeforeEach
    void setUp() {
        validador = new ValidadorMision();
    }

    @Test
    @DisplayName("Drone con batería por encima del 35% tiene batería suficiente")
    void hasBateriaSuficiente_bateriaMayorAlMinimo_retornaTrue() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-01", 85, ZonaHidrica.EMBALSE_NORTE);
        // Act
        boolean resultado = validador.hasBateriaSuficiente(drone);
        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Drone con batería exactamente en 35% tiene batería suficiente")
    void hasBateriaSuficiente_bateriaIgualAlMinimo_retornaTrue() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-02", 35, ZonaHidrica.CANAL_CENTRAL);
        // Act
        boolean resultado = validador.hasBateriaSuficiente(drone);
        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Drone con batería por debajo del 35% no tiene batería suficiente")
    void hasBateriaSuficiente_bateriaMenorAlMinimo_retornaFalse() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-03", 18, ZonaHidrica.LAGUNA_SUR);
        // Act
        boolean resultado = validador.hasBateriaSuficiente(drone);
        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Validar batería de un drone nulo lanza NullPointerException")
    void hasBateriaSuficiente_droneNulo_lanzaExcepcion() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> validador.hasBateriaSuficiente(null));
    }

    @Test
    @DisplayName("Drone marcado como disponible es reconocido como disponible")
    void isDroneDisponible_droneDisponible_retornaTrue() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-04", 73, ZonaHidrica.RIBERA_ESTE);
        // Act
        boolean resultado = validador.isDroneDisponible(drone);
        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Drone marcado como no disponible no puede ser asignado")
    void isDroneDisponible_droneNoDisponible_retornaFalse() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
        drone.actualizarEstado(EstadoDrone.MANTENIMIENTO);
        // Act
        boolean resultado = validador.isDroneDisponible(drone);
        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Punto de llegada nulo lanza IllegalArgumentException")
    void validarPuntoLlegada_nulo_lanzaExcepcion() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> validador.validarPuntoLlegada(null));
    }

    @Test
    @DisplayName("Punto de llegada vacío lanza IllegalArgumentException")
    void validarPuntoLlegada_vacio_lanzaExcepcion() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> validador.validarPuntoLlegada("   "));
    }

    @Test
    @DisplayName("Punto de llegada con valor no lanza excepción")
    void validarPuntoLlegada_conValor_noLanzaExcepcion() {
        // Act + Assert
        assertDoesNotThrow(() -> validador.validarPuntoLlegada("Laboratorio Hídrico"));
    }

    @Test
    @DisplayName("Zona del campus registrada en el MVP es válida")
    void isZonaValida_zonaDelCampus_retornaTrue() {
        // Act
        boolean resultado = validador.isZonaValida("Laboratorio Hídrico");
        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Zona que no pertenece al campus no es válida")
    void isZonaValida_zonaDesconocida_retornaFalse() {
        // Act
        boolean resultado = validador.isZonaValida("Río Bogotá");
        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Zona nula no es válida")
    void isZonaValida_zonaNula_retornaFalse() {
        // Act
        boolean resultado = validador.isZonaValida(null);
        // Assert
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Zona escrita con mayúsculas distintas no es válida")
    void isZonaValida_zonaConMayusculasDistintas_retornaFalse() {
        // Act
        boolean resultado = validador.isZonaValida("embalse norte");
        // Assert
        assertFalse(resultado);
    }
}