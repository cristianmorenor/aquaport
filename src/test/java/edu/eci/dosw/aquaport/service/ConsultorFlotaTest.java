package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.DroneSuperficial;
import edu.eci.dosw.aquaport.domain.EstadoDrone;
import edu.eci.dosw.aquaport.domain.ZonaHidrica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ConsultorFlotaTest {

    private ConsultorFlota consultor;
    private DroneAcuatico ar01;
    private DroneAcuatico ar02;
    private DroneAcuatico ar04;
    private List<DroneAcuatico> flota;

    @BeforeEach
    void setUp() {
        consultor = new ConsultorFlota();
        ar01 = new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
        ar02 = new DroneSuperficial("AR-02", 45, ZonaHidrica.CANAL_CENTRAL);
        DroneAcuatico ar03 = new DroneSuperficial("AR-03", 18, ZonaHidrica.LAGUNA_SUR);
        ar03.actualizarEstado(EstadoDrone.RECARGANDO);
        ar04 = new DroneSuperficial("AR-04", 73, ZonaHidrica.RIBERA_ESTE);
        flota = List.of(ar01, ar02, ar03, ar04);
    }

    @Test
    @DisplayName("Los drones operativos se ordenan de mayor a menor batería")
    void obtenerDisponiblesOrdenadosPorBateria_flotaEjemplo_ordenaDescendente() {
        // Act
        List<DroneAcuatico> ordenados = consultor.obtenerDisponiblesOrdenadosPorBateria(flota);
        // Assert
        assertEquals(List.of(ar01, ar04, ar02), ordenados);
    }

    @Test
    @DisplayName("Los IDs listados son solo los de drones disponibles")
    void obtenerIdsDisponibles_flotaEjemplo_excluyeRecargando() {
        // Act
        List<String> ids = consultor.obtenerIdsDisponibles(flota);
        // Assert
        assertEquals(List.of("AR-01", "AR-02", "AR-04"), ids);
    }

    @Test
    @DisplayName("Existe un drone operativo cuando alguno está disponible con batería suficiente")
    void hasDroneOperativo_flotaEjemplo_retornaTrue() {
        // Act
        boolean hayOperativo = consultor.hasDroneOperativo(flota);
        // Assert
        assertTrue(hayOperativo);
    }

    @Test
    @DisplayName("Una flota vacía no tiene drones operativos")
    void hasDroneOperativo_flotaVacia_retornaFalse() {
        // Act
        boolean hayOperativo = consultor.hasDroneOperativo(List.of());
        // Assert
        assertFalse(hayOperativo);
    }

    @Test
    @DisplayName("Se cuentan solo los drones disponibles")
    void contarDisponibles_flotaEjemplo_retornaTres() {
        // Act
        long disponibles = consultor.contarDisponibles(flota);
        // Assert
        assertEquals(3L, disponibles);
    }

    @Test
    @DisplayName("El drone con mayor batería de toda la flota es AR-01")
    void obtenerDroneConMayorBateria_flotaEjemplo_retornaAr01() {
        // Act
        Optional<DroneAcuatico> mayor = consultor.obtenerDroneConMayorBateria(flota);
        // Assert
        assertEquals(Optional.of(ar01), mayor);
    }

    @Test
    @DisplayName("Una flota vacía no tiene drone con mayor batería")
    void obtenerDroneConMayorBateria_flotaVacia_retornaVacio() {
        // Act
        Optional<DroneAcuatico> mayor = consultor.obtenerDroneConMayorBateria(List.of());
        // Assert
        assertTrue(mayor.isEmpty());
    }

    @Test
    @DisplayName("Consultar una flota nula lanza NullPointerException")
    void contarDisponibles_flotaNula_lanzaExcepcion() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> consultor.contarDisponibles(null));
    }
}