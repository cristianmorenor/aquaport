package edu.eci.dosw.aquaport.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DroneAcuaticoTest {

    @Test
    @DisplayName("El drone superficial transporta hasta 500 gramos")
    void getCapacidadCargaGramos_droneSuperficial_retorna500() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
        // Act
        int capacidad = drone.getCapacidadCargaGramos();
        // Assert
        assertEquals(500, capacidad);
    }

    @Test
    @DisplayName("El drone semisumergido transporta hasta 1500 gramos")
    void getCapacidadCargaGramos_droneSemisumergido_retorna1500() {
        // Arrange
        DroneAcuatico drone = new DroneSemisumergido("AR-05", 80, ZonaHidrica.CANAL_CENTRAL);
        // Act
        int capacidad = drone.getCapacidadCargaGramos();
        // Assert
        assertEquals(1500, capacidad);
    }

    @Test
    @DisplayName("El drone buceador transporta hasta 300 gramos")
    void getCapacidadCargaGramos_droneBuceador_retorna300() {
        // Arrange
        DroneAcuatico drone = new DroneBuceador("AR-10", 70, ZonaHidrica.LAGUNA_SUR);
        // Act
        int capacidad = drone.getCapacidadCargaGramos();
        // Assert
        assertEquals(300, capacidad);
    }

    @Test
    @DisplayName("El drone buceador se identifica con el tipo BUCEADOR")
    void getTipo_droneBuceador_retornaBuceador() {
        // Arrange
        DroneAcuatico drone = new DroneBuceador("AR-10", 70, ZonaHidrica.LAGUNA_SUR);
        // Act
        String tipo = drone.getTipo();
        // Assert
        assertEquals("BUCEADOR", tipo);
    }

    @Test
    @DisplayName("Un drone recién creado inicia en estado DISPONIBLE")
    void getEstado_droneNuevo_retornaDisponible() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
        // Act
        EstadoDrone estado = drone.getEstado();
        // Assert
        assertEquals(EstadoDrone.DISPONIBLE, estado);
    }

    @Test
    @DisplayName("Un drone que pasa a FALLO deja de estar disponible")
    void isDisponible_droneEnFallo_retornaFalse() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-04", 73, ZonaHidrica.RIBERA_ESTE);
        drone.actualizarEstado(EstadoDrone.FALLO);
        // Act
        boolean disponible = drone.isDisponible();
        // Assert
        assertFalse(disponible);
    }

    @Test
    @DisplayName("Actualizar el estado a nulo lanza NullPointerException")
    void actualizarEstado_estadoNulo_lanzaExcepcion() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
        // Act + Assert
        assertThrows(NullPointerException.class, () -> drone.actualizarEstado(null));
    }

    @Test
    @DisplayName("El buceador puede transportar una carga igual a su capacidad")
    void canTransportar_pesoIgualACapacidad_retornaTrue() {
        // Arrange
        DroneAcuatico drone = new DroneBuceador("AR-10", 70, ZonaHidrica.LAGUNA_SUR);
        // Act
        boolean puedeTransportar = drone.canTransportar(300);
        // Assert
        assertTrue(puedeTransportar);
    }

    @Test
    @DisplayName("El buceador no puede transportar una carga mayor a 300 gramos")
    void canTransportar_pesoMayorACapacidad_retornaFalse() {
        // Arrange
        DroneAcuatico drone = new DroneBuceador("AR-10", 70, ZonaHidrica.LAGUNA_SUR);
        // Act
        boolean puedeTransportar = drone.canTransportar(301);
        // Assert
        assertFalse(puedeTransportar);
    }

    @Test
    @DisplayName("Una carga de 0 gramos lanza IllegalArgumentException")
    void canTransportar_pesoCero_lanzaExcepcion() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> drone.canTransportar(0));
    }

    @Test
    @DisplayName("Crear un drone con batería mayor a 100 lanza IllegalArgumentException")
    void constructor_bateriaMayorA100_lanzaExcepcion() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new DroneSuperficial("AR-01", 101, ZonaHidrica.EMBALSE_NORTE));
    }

    @Test
    @DisplayName("Crear un drone con id vacío lanza IllegalArgumentException")
    void constructor_idVacio_lanzaExcepcion() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new DroneSuperficial("  ", 92, ZonaHidrica.EMBALSE_NORTE));
    }

    @Test
    @DisplayName("Crear un drone sin zona lanza NullPointerException")
    void constructor_zonaNula_lanzaExcepcion() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> new DroneSuperficial("AR-01", 92, null));
    }

    @Test
    @DisplayName("Un drone disponible con batería exactamente en 35% está operativo")
    void isOperativo_disponibleConBateriaEnMinimo_retornaTrue() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-02", 35, ZonaHidrica.CANAL_CENTRAL);
        // Act
        boolean operativo = drone.isOperativo();
        // Assert
        assertTrue(operativo);
    }

    @Test
    @DisplayName("Un drone disponible con batería en 34% no está operativo")
    void isOperativo_disponibleConBateriaBajoMinimo_retornaFalse() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-03", 34, ZonaHidrica.LAGUNA_SUR);
        // Act
        boolean operativo = drone.isOperativo();
        // Assert
        assertFalse(operativo);
    }

    @Test
    @DisplayName("Un drone en misión no está operativo aunque tenga batería")
    void isOperativo_droneEnMision_retornaFalse() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
        drone.actualizarEstado(EstadoDrone.EN_MISION);
        // Act
        boolean operativo = drone.isOperativo();
        // Assert
        assertFalse(operativo);
    }

    @Test
    @DisplayName("Crear un drone con id nulo lanza IllegalArgumentException")
    void constructor_idNulo_lanzaExcepcion() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new DroneSuperficial(null, 92, ZonaHidrica.EMBALSE_NORTE));
    }

    @Test
    @DisplayName("Crear un drone con batería negativa lanza IllegalArgumentException")
    void constructor_bateriaNegativa_lanzaExcepcion() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> new DroneSuperficial("AR-01", -1, ZonaHidrica.EMBALSE_NORTE));
    }
}
