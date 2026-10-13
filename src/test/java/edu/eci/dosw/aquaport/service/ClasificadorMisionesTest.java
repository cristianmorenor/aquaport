package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneSuperficial;
import edu.eci.dosw.aquaport.domain.Mision;
import edu.eci.dosw.aquaport.domain.Prioridad;
import edu.eci.dosw.aquaport.domain.TipoCarga;
import edu.eci.dosw.aquaport.domain.ZonaHidrica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ClasificadorMisionesTest {

    private ClasificadorMisiones clasificador;
    private Mision misionCritica;
    private Mision misionNormal;
    private Mision misionBaja;
    private List<Mision> misiones;

    @BeforeEach
    void setUp() {
        clasificador = new ClasificadorMisiones();
        misionCritica = crearMision("M-001", Prioridad.CRITICA);
        misionNormal = crearMision("M-002", Prioridad.NORMAL);
        misionBaja = crearMision("M-003", Prioridad.BAJA);
        misiones = List.of(misionCritica, misionNormal, misionBaja);
    }

    @Test
    @DisplayName("Las misiones CRITICAS quedan en la partición true")
    void separarCriticas_misionesMixtas_criticasEnTrue() {
        // Act
        Map<Boolean, List<Mision>> particion = clasificador.separarCriticas(misiones);
        // Assert
        assertEquals(List.of(misionCritica), particion.get(true));
    }

    @Test
    @DisplayName("Las misiones no críticas quedan en la partición false")
    void separarCriticas_misionesMixtas_restoEnFalse() {
        // Act
        Map<Boolean, List<Mision>> particion = clasificador.separarCriticas(misiones);
        // Assert
        assertEquals(List.of(misionNormal, misionBaja), particion.get(false));
    }

    @Test
    @DisplayName("Sin misiones, la partición de críticas queda vacía")
    void separarCriticas_listaVacia_criticasVacias() {
        // Act
        Map<Boolean, List<Mision>> particion = clasificador.separarCriticas(List.of());
        // Assert
        assertTrue(particion.get(true).isEmpty());
    }

    @Test
    @DisplayName("Clasificar una lista nula lanza NullPointerException")
    void separarCriticas_listaNula_lanzaExcepcion() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> clasificador.separarCriticas(null));
    }

    private static Mision crearMision(String id, Prioridad prioridad) {
        return new Mision.Builder()
                .id(id)
                .drone(new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE))
                .puntoPartida("Embalse Norte")
                .puntoLlegada("Laboratorio Hídrico")
                .tipoCarga(TipoCarga.MUESTRA_AGUA)
                .prioridad(prioridad)
                .build();
    }
}