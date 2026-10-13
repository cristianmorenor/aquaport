package edu.eci.dosw.aquaport.service.estrategia;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.DroneBuceador;
import edu.eci.dosw.aquaport.domain.DroneSuperficial;
import edu.eci.dosw.aquaport.domain.Prioridad;
import edu.eci.dosw.aquaport.domain.SolicitudMision;
import edu.eci.dosw.aquaport.domain.TipoCarga;
import edu.eci.dosw.aquaport.domain.ZonaHidrica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MayorBateriaStrategyTest {

    private MayorBateriaStrategy estrategia;
    private SolicitudMision solicitud;

    @BeforeEach
    void setUp() {
        estrategia = new MayorBateriaStrategy();
        solicitud = new SolicitudMision("M-020", ZonaHidrica.LAB_HIDRICO, TipoCarga.SENSOR, 200, Prioridad.NORMAL);
    }

    @Test
    @DisplayName("Selecciona el candidato con mayor batería sin importar la zona")
    void seleccionar_variosCandidatos_retornaMayorBateria() {
        // Arrange
        DroneAcuatico conMasBateria = new DroneBuceador("AR-12", 76, ZonaHidrica.LAGUNA_SUR);
        List<DroneAcuatico> candidatos = List.of(
                new DroneSuperficial("AR-05", 60, ZonaHidrica.LAB_HIDRICO), conMasBateria);
        // Act
        Optional<DroneAcuatico> seleccionado = estrategia.seleccionar(candidatos, solicitud);
        // Assert
        assertEquals(Optional.of(conMasBateria), seleccionado);
    }

    @Test
    @DisplayName("Sin candidatos no selecciona ningún drone")
    void seleccionar_sinCandidatos_retornaVacio() {
        // Act
        Optional<DroneAcuatico> seleccionado = estrategia.seleccionar(List.of(), solicitud);
        // Assert
        assertTrue(seleccionado.isEmpty());
    }
}