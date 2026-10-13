package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.DroneBuceador;
import edu.eci.dosw.aquaport.domain.DroneSemisumergido;
import edu.eci.dosw.aquaport.domain.DroneSuperficial;
import edu.eci.dosw.aquaport.domain.EstadoDrone;
import edu.eci.dosw.aquaport.domain.ZonaHidrica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AnalizadorFlotaTest {

    private AnalizadorFlota analizador;
    private DroneAcuatico superficialCanal;
    private DroneAcuatico semisumergidoCanal;
    private List<DroneAcuatico> flota;

    @BeforeEach
    void setUp() {
        analizador = new AnalizadorFlota();
        superficialCanal = new DroneSuperficial("AR-02", 40, ZonaHidrica.CANAL_CENTRAL);
        semisumergidoCanal = new DroneSemisumergido("AR-05", 80, ZonaHidrica.CANAL_CENTRAL);
        flota = List.of(
                new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE),
                superficialCanal,
                semisumergidoCanal,
                conEstado(new DroneSemisumergido("AR-06", 60, ZonaHidrica.CANAL_CENTRAL), EstadoDrone.EN_MISION),
                new DroneBuceador("AR-10", 30, ZonaHidrica.LAGUNA_SUR),
                conEstado(new DroneBuceador("AR-11", 70, ZonaHidrica.LAGUNA_SUR), EstadoDrone.FALLO),
                conEstado(new DroneBuceador("AR-12", 50, ZonaHidrica.EMBALSE_NORTE), EstadoDrone.MANTENIMIENTO),
                conEstado(new DroneSuperficial("AR-13", 90, ZonaHidrica.RIBERA_ESTE), EstadoDrone.FALLO)
        );
    }

    @Test
    @DisplayName("Agrupar por tipo deja en cada grupo solo los drones disponibles")
    void agruparDisponiblesPorTipo_flotaMixta_agrupaSoloDisponibles() {
        // Act
        Map<String, List<DroneAcuatico>> porTipo = analizador.agruparDisponiblesPorTipo(flota);
        // Assert
        assertEquals(List.of(semisumergidoCanal), porTipo.get(DroneSemisumergido.TIPO));
    }

    @Test
    @DisplayName("Agrupar una flota vacía retorna un mapa vacío")
    void agruparDisponiblesPorTipo_flotaVacia_retornaMapaVacio() {
        // Act
        Map<String, List<DroneAcuatico>> porTipo = analizador.agruparDisponiblesPorTipo(List.of());
        // Assert
        assertTrue(porTipo.isEmpty());
    }

    @Test
    @DisplayName("El drone óptimo es el compatible con mayor batería en la zona")
    void obtenerDroneOptimo_variosCompatiblesEnZona_retornaMayorBateria() {
        // Arrange
        Set<String> tipos = Set.of(DroneSuperficial.TIPO, DroneSemisumergido.TIPO);
        // Act
        Optional<DroneAcuatico> optimo = analizador.obtenerDroneOptimo(flota, ZonaHidrica.CANAL_CENTRAL, tipos);
        // Assert
        assertEquals(Optional.of(semisumergidoCanal), optimo);
    }

    @Test
    @DisplayName("Si ningún drone de la zona es de un tipo compatible no hay óptimo")
    void obtenerDroneOptimo_tipoNoCompatible_retornaVacio() {
        // Arrange
        Set<String> tipos = Set.of(DroneBuceador.TIPO);
        // Act
        Optional<DroneAcuatico> optimo = analizador.obtenerDroneOptimo(flota, ZonaHidrica.CANAL_CENTRAL, tipos);
        // Assert
        assertTrue(optimo.isEmpty());
    }

    @Test
    @DisplayName("Un drone con batería bajo el 35% o en fallo no puede ser el óptimo")
    void obtenerDroneOptimo_bateriaBajaOFallo_retornaVacio() {
        // Arrange
        Set<String> tipos = Set.of(DroneBuceador.TIPO);
        // Act
        Optional<DroneAcuatico> optimo = analizador.obtenerDroneOptimo(flota, ZonaHidrica.LAGUNA_SUR, tipos);
        // Assert
        assertTrue(optimo.isEmpty());
    }

    @Test
    @DisplayName("Buscar el drone óptimo sin zona lanza NullPointerException")
    void obtenerDroneOptimo_zonaNula_lanzaExcepcion() {
        // Arrange
        Set<String> tipos = Set.of(DroneBuceador.TIPO);
        // Act + Assert
        assertThrows(NullPointerException.class, () -> analizador.obtenerDroneOptimo(flota, null, tipos));
    }

    @Test
    @DisplayName("El promedio de batería se calcula por tipo sobre toda la flota")
    void calcularPromedioBateriaPorTipo_flotaMixta_promediaBuceadores() {
        // Act
        Map<String, Double> promedios = analizador.calcularPromedioBateriaPorTipo(flota);
        // Assert
        assertEquals(50.0, promedios.get(DroneBuceador.TIPO));
    }

    @Test
    @DisplayName("Las zonas cubiertas excluyen las que solo tienen drones en fallo o mantenimiento")
    void obtenerZonasCubiertasPorFlotaActiva_flotaMixta_excluyeFalloYMantenimiento() {
        // Act
        List<ZonaHidrica> zonas = analizador.obtenerZonasCubiertasPorFlotaActiva(flota);
        // Assert
        assertEquals(List.of(ZonaHidrica.EMBALSE_NORTE, ZonaHidrica.CANAL_CENTRAL, ZonaHidrica.LAGUNA_SUR), zonas);
    }

    @Test
    @DisplayName("Analizar una flota nula lanza NullPointerException")
    void calcularPromedioBateriaPorTipo_flotaNula_lanzaExcepcion() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> analizador.calcularPromedioBateriaPorTipo(null));
    }

    private static DroneAcuatico conEstado(DroneAcuatico drone, EstadoDrone estado) {
        drone.actualizarEstado(estado);
        return drone;
    }
}