package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidadorMisionTest {

    private static final String MODELO = "Aqua-Ranger 100";

    private ValidadorMision validador;

    @BeforeEach
    void setUp() {
        validador = new ValidadorMision();
    }

    @Test
    @DisplayName("Drone con batería por encima del 35% tiene batería suficiente")
    void hasBateriaSuficiente_bateriaMayorAlMinimo_retornaTrue() {
        // Arrange
        DroneAcuatico drone = new DroneAcuatico("AR-01", MODELO, 85, true, "Embalse Norte");
        // Act
        boolean resultado = validador.hasBateriaSuficiente(drone);
        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Drone con batería exactamente en 35% tiene batería suficiente")
    void hasBateriaSuficiente_bateriaIgualAlMinimo_retornaTrue() {
        // Arrange
        DroneAcuatico drone = new DroneAcuatico("AR-02", MODELO, 35, true, "Canal Central");
        // Act
        boolean resultado = validador.hasBateriaSuficiente(drone);
        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Drone con batería por debajo del 35% no tiene batería suficiente")
    void hasBateriaSuficiente_bateriaMenorAlMinimo_retornaFalse() {
        // Arrange
        DroneAcuatico drone = new DroneAcuatico("AR-03", MODELO, 18, true, "Laguna Sur");
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
        DroneAcuatico drone = new DroneAcuatico("AR-04", MODELO, 73, true, "Punto Ribereño Este");
        // Act
        boolean resultado = validador.isDroneDisponible(drone);
        // Assert
        assertTrue(resultado);
    }

    @Test
    @DisplayName("Drone marcado como no disponible no puede ser asignado")
    void isDroneDisponible_droneNoDisponible_retornaFalse() {
        // Arrange
        DroneAcuatico drone = new DroneAcuatico("AR-01", MODELO, 92, false, "Embalse Norte");
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
}