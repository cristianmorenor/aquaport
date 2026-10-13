package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneSuperficial;
import edu.eci.dosw.aquaport.domain.Mision;
import edu.eci.dosw.aquaport.domain.RepositorioMisiones;
import edu.eci.dosw.aquaport.domain.TipoCarga;
import edu.eci.dosw.aquaport.domain.ZonaHidrica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistradorMisionesTest {

    @Mock
    private RepositorioMisiones repositorioMock;

    @InjectMocks
    private RegistradorMisiones registrador;

    private Mision mision;

    @BeforeEach
    void setUp() {
        mision = new Mision.Builder()
                .id("M-001")
                .drone(new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE))
                .puntoPartida("Embalse Norte")
                .puntoLlegada("Laboratorio Hídrico")
                .tipoCarga(TipoCarga.MUESTRA_AGUA)
                .build();
    }

    @Test
    @DisplayName("Registrar una misión nueva la guarda en el repositorio")
    void registrar_misionNueva_guardaEnRepositorio() {
        // Arrange
        when(repositorioMock.buscarPorId("M-001")).thenReturn(Optional.empty());
        // Act
        registrador.registrar(mision);
        // Assert
        verify(repositorioMock, times(1)).guardar(mision);
    }

    @Test
    @DisplayName("Registrar una misión con id repetido lanza IllegalStateException")
    void registrar_idDuplicado_lanzaExcepcion() {
        // Arrange
        when(repositorioMock.buscarPorId("M-001")).thenReturn(Optional.of(mision));
        // Act + Assert
        assertThrows(IllegalStateException.class, () -> registrador.registrar(mision));
    }

    @Test
    @DisplayName("Una misión con id repetido no se guarda")
    void registrar_idDuplicado_noGuarda() {
        // Arrange
        when(repositorioMock.buscarPorId("M-001")).thenReturn(Optional.of(mision));
        // Act
        assertThrows(IllegalStateException.class, () -> registrador.registrar(mision));
        // Assert
        verify(repositorioMock, never()).guardar(any());
    }

    @Test
    @DisplayName("Registrar una misión nula lanza NullPointerException")
    void registrar_misionNula_lanzaExcepcion() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> registrador.registrar(null));
    }

    @Test
    @DisplayName("Buscar por id delega en el repositorio")
    void buscarPorId_misionExistente_retornaMision() {
        // Arrange
        when(repositorioMock.buscarPorId("M-001")).thenReturn(Optional.of(mision));
        // Act
        Optional<Mision> encontrada = registrador.buscarPorId("M-001");
        // Assert
        assertEquals(Optional.of(mision), encontrada);
    }

    @Test
    @DisplayName("Listar misiones retorna las misiones del repositorio")
    void listarMisiones_repositorioConMision_retornaLista() {
        // Arrange
        when(repositorioMock.listarTodas()).thenReturn(List.of(mision));
        // Act
        List<Mision> misiones = registrador.listarMisiones();
        // Assert
        assertEquals(List.of(mision), misiones);
    }

    @Test
    @DisplayName("Crear el registrador sin repositorio lanza NullPointerException")
    void constructor_repositorioNulo_lanzaExcepcion() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> new RegistradorMisiones(null));
    }
}