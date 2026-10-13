package edu.eci.dosw.aquaport.repository;

import edu.eci.dosw.aquaport.domain.DroneSuperficial;
import edu.eci.dosw.aquaport.domain.Mision;
import edu.eci.dosw.aquaport.domain.TipoCarga;
import edu.eci.dosw.aquaport.domain.ZonaHidrica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepositorioMisionesEnMemoriaTest {

    private RepositorioMisionesEnMemoria repositorio;
    private Mision primera;
    private Mision segunda;

    @BeforeEach
    void setUp() {
        repositorio = new RepositorioMisionesEnMemoria();
        primera = crearMision("M-001");
        segunda = crearMision("M-002");
    }

    @Test
    @DisplayName("Una misión guardada se puede buscar por su id")
    void buscarPorId_misionGuardada_retornaMision() {
        // Arrange
        repositorio.guardar(primera);
        // Act
        Optional<Mision> encontrada = repositorio.buscarPorId("M-001");
        // Assert
        assertEquals(Optional.of(primera), encontrada);
    }

    @Test
    @DisplayName("Buscar un id que no existe retorna vacío")
    void buscarPorId_idInexistente_retornaVacio() {
        // Act
        Optional<Mision> encontrada = repositorio.buscarPorId("M-999");
        // Assert
        assertTrue(encontrada.isEmpty());
    }

    @Test
    @DisplayName("Las misiones se listan en el orden en que se guardaron")
    void listarTodas_dosMisiones_conservaOrdenDeRegistro() {
        // Arrange
        repositorio.guardar(primera);
        repositorio.guardar(segunda);
        // Act
        List<Mision> misiones = repositorio.listarTodas();
        // Assert
        assertEquals(List.of(primera, segunda), misiones);
    }

    @Test
    @DisplayName("La lista retornada no se puede modificar desde afuera")
    void listarTodas_intentarAgregar_lanzaExcepcion() {
        // Arrange
        List<Mision> misiones = repositorio.listarTodas();
        // Act + Assert
        assertThrows(UnsupportedOperationException.class, () -> misiones.add(segunda));
    }

    private static Mision crearMision(String id) {
        return new Mision.Builder()
                .id(id)
                .drone(new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE))
                .puntoPartida("Embalse Norte")
                .puntoLlegada("Laboratorio Hídrico")
                .tipoCarga(TipoCarga.MUESTRA_AGUA)
                .build();
    }
}