package edu.eci.dosw.aquaport.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FabricaDronesTest {

    private FabricaDrones fabrica;

    @BeforeEach
    void setUp() {
        fabrica = new FabricaDrones();
        fabrica.registrarTipo(DroneBuceador.TIPO, DroneBuceador::new);
    }

    @Test
    @DisplayName("Crear un tipo registrado retorna una instancia de esa subclase")
    void crear_tipoRegistrado_retornaInstanciaDelTipo() {
        // Act
        DroneAcuatico drone = fabrica.crear(DroneBuceador.TIPO, "AR-10", 70, ZonaHidrica.LAGUNA_SUR);
        // Assert
        assertInstanceOf(DroneBuceador.class, drone);
    }

    @Test
    @DisplayName("El drone creado conserva el id recibido")
    void crear_tipoRegistrado_conservaIdDelDrone() {
        // Act
        DroneAcuatico drone = fabrica.crear(DroneBuceador.TIPO, "AR-10", 70, ZonaHidrica.LAGUNA_SUR);
        // Assert
        assertEquals("AR-10", drone.getId());
    }

    @Test
    @DisplayName("Crear un tipo que no fue registrado lanza IllegalArgumentException")
    void crear_tipoNoRegistrado_lanzaExcepcion() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> fabrica.crear("SUMERGIBLE_PROFUNDO", "AR-20", 80, ZonaHidrica.LAGUNA_SUR));
    }

    @Test
    @DisplayName("Registrar un tipo vacío lanza IllegalArgumentException")
    void registrarTipo_tipoVacio_lanzaExcepcion() {
        // Act + Assert
        assertThrows(IllegalArgumentException.class, () -> fabrica.registrarTipo(" ", DroneBuceador::new));
    }

    @Test
    @DisplayName("Registrar un tipo sin creador lanza NullPointerException")
    void registrarTipo_creadorNulo_lanzaExcepcion() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> fabrica.registrarTipo("SUMERGIBLE_PROFUNDO", null));
    }
}