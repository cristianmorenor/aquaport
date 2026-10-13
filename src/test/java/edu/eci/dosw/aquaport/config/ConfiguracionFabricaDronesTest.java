package edu.eci.dosw.aquaport.config;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.DroneBuceador;
import edu.eci.dosw.aquaport.domain.DroneSemisumergido;
import edu.eci.dosw.aquaport.domain.DroneSuperficial;
import edu.eci.dosw.aquaport.domain.FabricaDrones;
import edu.eci.dosw.aquaport.domain.ZonaHidrica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

class ConfiguracionFabricaDronesTest {

    private FabricaDrones fabrica;

    @BeforeEach
    void setUp() {
        fabrica = ConfiguracionFabricaDrones.crearFabrica();
    }

    @Test
    @DisplayName("La fábrica configurada crea drones superficiales")
    void crearFabrica_tipoSuperficial_creaDroneSuperficial() {
        // Act
        DroneAcuatico drone = fabrica.crear(DroneSuperficial.TIPO, "AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
        // Assert
        assertInstanceOf(DroneSuperficial.class, drone);
    }

    @Test
    @DisplayName("La fábrica configurada crea drones semisumergidos")
    void crearFabrica_tipoSemisumergido_creaDroneSemisumergido() {
        // Act
        DroneAcuatico drone = fabrica.crear(DroneSemisumergido.TIPO, "AR-05", 80, ZonaHidrica.CANAL_CENTRAL);
        // Assert
        assertInstanceOf(DroneSemisumergido.class, drone);
    }

    @Test
    @DisplayName("La fábrica configurada crea drones buceadores")
    void crearFabrica_tipoBuceador_creaDroneBuceador() {
        // Act
        DroneAcuatico drone = fabrica.crear(DroneBuceador.TIPO, "AR-10", 70, ZonaHidrica.LAGUNA_SUR);
        // Assert
        assertInstanceOf(DroneBuceador.class, drone);
    }
}