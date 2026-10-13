package edu.eci.dosw.aquaport.service.estrategia;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.DroneBuceador;
import edu.eci.dosw.aquaport.domain.DroneSemisumergido;
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

class ZonaCercanaStrategyTest {

    private ZonaCercanaStrategy estrategia;
    private SolicitudMision solicitudLaguna;

    @BeforeEach
    void setUp() {
        estrategia = new ZonaCercanaStrategy();
        solicitudLaguna = new SolicitudMision("M-030", ZonaHidrica.LAGUNA_SUR, TipoCarga.MUESTRA_AGUA,
                200, Prioridad.NORMAL);
    }

    @Test
    @DisplayName("Prefiere un drone en la zona de destino aunque otro tenga más batería")
    void seleccionar_droneEnZonaDestino_loPrefiereSobreMayorBateria() {
        // Arrange
        DroneAcuatico enLaguna = new DroneBuceador("AR-14", 40, ZonaHidrica.LAGUNA_SUR);
        List<DroneAcuatico> candidatos = List.of(
                new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE), enLaguna);
        // Act
        Optional<DroneAcuatico> seleccionado = estrategia.seleccionar(candidatos, solicitudLaguna);
        // Assert
        assertEquals(Optional.of(enLaguna), seleccionado);
    }

    @Test
    @DisplayName("Entre varios drones de la zona de destino elige el de mayor batería")
    void seleccionar_variosEnZonaDestino_retornaMayorBateria() {
        // Arrange
        DroneAcuatico mejorEnLaguna = new DroneSemisumergido("AR-08", 57, ZonaHidrica.LAGUNA_SUR);
        List<DroneAcuatico> candidatos = List.of(
                new DroneBuceador("AR-14", 40, ZonaHidrica.LAGUNA_SUR), mejorEnLaguna);
        // Act
        Optional<DroneAcuatico> seleccionado = estrategia.seleccionar(candidatos, solicitudLaguna);
        // Assert
        assertEquals(Optional.of(mejorEnLaguna), seleccionado);
    }

    @Test
    @DisplayName("Si ningún drone está en la zona de destino elige el de mayor batería")
    void seleccionar_ningunoEnZonaDestino_retornaMayorBateria() {
        // Arrange
        DroneAcuatico conMasBateria = new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
        List<DroneAcuatico> candidatos = List.of(
                new DroneSuperficial("AR-02", 45, ZonaHidrica.CANAL_CENTRAL), conMasBateria);
        // Act
        Optional<DroneAcuatico> seleccionado = estrategia.seleccionar(candidatos, solicitudLaguna);
        // Assert
        assertEquals(Optional.of(conMasBateria), seleccionado);
    }
}