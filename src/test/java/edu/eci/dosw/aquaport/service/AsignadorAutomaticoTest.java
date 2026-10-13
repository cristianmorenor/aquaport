package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.DroneBuceador;
import edu.eci.dosw.aquaport.domain.DroneSuperficial;
import edu.eci.dosw.aquaport.domain.EstadoDrone;
import edu.eci.dosw.aquaport.domain.EstrategiaSeleccion;
import edu.eci.dosw.aquaport.domain.Mision;
import edu.eci.dosw.aquaport.domain.ObservadorMision;
import edu.eci.dosw.aquaport.domain.Prioridad;
import edu.eci.dosw.aquaport.domain.RepositorioDrones;
import edu.eci.dosw.aquaport.domain.SolicitudMision;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsignadorAutomaticoTest {

    @Mock
    private EstrategiaSeleccion estrategiaMock;

    @Mock
    private RepositorioDrones repositorioMock;

    @Mock
    private ObservadorMision centroControlMock;

    @Mock
    private ObservadorMision tecnicoMock;

    @InjectMocks
    private AsignadorAutomatico asignador;

    private SolicitudMision solicitud;
    private SolicitudMision solicitudCritica;

    @BeforeEach
    void setUp() {
        solicitud = new SolicitudMision("M-040", ZonaHidrica.LAB_HIDRICO, TipoCarga.MUESTRA_AGUA,
                200, Prioridad.NORMAL);
        solicitudCritica = new SolicitudMision("M-050", ZonaHidrica.LAB_HIDRICO, TipoCarga.MUESTRA_AGUA,
                200, Prioridad.CRITICA);
        asignador.registrarObservador(centroControlMock);
        asignador.registrarObservador(tecnicoMock);
    }

    @Test
    @DisplayName("Si la estrategia elige un drone, la misión queda asignada a ese drone")
    void asignar_estrategiaEligeDrone_retornaMisionConEseDrone() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
        when(repositorioMock.listarTodos()).thenReturn(List.of(drone));
        when(estrategiaMock.seleccionar(List.of(drone), solicitud)).thenReturn(Optional.of(drone));
        // Act
        Optional<Mision> mision = asignador.asignar(solicitud);
        // Assert
        assertEquals(Optional.of(drone), mision.map(Mision::getDrone));
    }

    @Test
    @DisplayName("El drone asignado pasa a estado EN_MISION")
    void asignar_estrategiaEligeDrone_droneQuedaEnMision() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
        when(repositorioMock.listarTodos()).thenReturn(List.of(drone));
        when(estrategiaMock.seleccionar(List.of(drone), solicitud)).thenReturn(Optional.of(drone));
        // Act
        asignador.asignar(solicitud);
        // Assert
        assertEquals(EstadoDrone.EN_MISION, drone.getEstado());
    }

    @Test
    @DisplayName("La misión asignada llega a la zona de destino de la solicitud")
    void asignar_estrategiaEligeDrone_misionLlegaAZonaDestino() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
        when(repositorioMock.listarTodos()).thenReturn(List.of(drone));
        when(estrategiaMock.seleccionar(List.of(drone), solicitud)).thenReturn(Optional.of(drone));
        // Act
        Optional<Mision> mision = asignador.asignar(solicitud);
        // Assert
        assertEquals(Optional.of("Laboratorio Hídrico"), mision.map(Mision::getPuntoLlegada));
    }

    @Test
    @DisplayName("Al cambiar la estrategia el asignador usa la nueva estrategia")
    void cambiarEstrategia_nuevaEstrategia_seUsaEnLaAsignacion() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
        EstrategiaSeleccion nuevaEstrategia = mock(EstrategiaSeleccion.class);
        when(repositorioMock.listarTodos()).thenReturn(List.of(drone));
        when(nuevaEstrategia.seleccionar(List.of(drone), solicitud)).thenReturn(Optional.of(drone));
        asignador.cambiarEstrategia(nuevaEstrategia);
        // Act
        asignador.asignar(solicitud);
        // Assert
        verify(estrategiaMock, never()).seleccionar(any(), any());
    }

    @Test
    @DisplayName("Un drone con batería exactamente en 35% sí puede ser asignado")
    void asignar_bateriaEnUmbralMinimo_asignaDrone() {
        // Arrange
        DroneAcuatico droneEnUmbral = new DroneSuperficial("AR-02", 35, ZonaHidrica.CANAL_CENTRAL);
        when(repositorioMock.listarTodos()).thenReturn(List.of(droneEnUmbral));
        when(estrategiaMock.seleccionar(List.of(droneEnUmbral), solicitud)).thenReturn(Optional.of(droneEnUmbral));
        // Act
        Optional<Mision> mision = asignador.asignar(solicitud);
        // Assert
        assertTrue(mision.isPresent());
    }

    @Test
    @DisplayName("Un drone con batería en 34% no llega a la estrategia")
    void asignar_bateriaBajoUmbral_noConsultaEstrategia() {
        // Arrange
        DroneAcuatico droneBajoUmbral = new DroneSuperficial("AR-03", 34, ZonaHidrica.LAGUNA_SUR);
        when(repositorioMock.listarTodos()).thenReturn(List.of(droneBajoUmbral));
        // Act
        asignador.asignar(solicitud);
        // Assert
        verify(estrategiaMock, never()).seleccionar(any(), any());
    }

    @Test
    @DisplayName("Un buceador no se asigna a una carga mayor a 300 gramos")
    void asignar_cargaSuperaCapacidadBuceador_noConsultaEstrategia() {
        // Arrange
        SolicitudMision cargaPesada = new SolicitudMision("M-041", ZonaHidrica.LAB_HIDRICO,
                TipoCarga.EQUIPO_MEDICION, 500, Prioridad.NORMAL);
        when(repositorioMock.listarTodos()).thenReturn(List.of(new DroneBuceador("AR-12", 76, ZonaHidrica.LAGUNA_SUR)));
        // Act
        asignador.asignar(cargaPesada);
        // Assert
        verify(estrategiaMock, never()).seleccionar(any(), any());
    }

    @Test
    @DisplayName("Con la lista de drones vacía no se asigna ninguna misión")
    void asignar_listaDronesVacia_retornaVacio() {
        // Arrange
        when(repositorioMock.listarTodos()).thenReturn(List.of());
        // Act
        Optional<Mision> mision = asignador.asignar(solicitud);
        // Assert
        assertTrue(mision.isEmpty());
    }

    @Test
    @DisplayName("Si la estrategia no elige ningún drone no se asigna la misión")
    void asignar_estrategiaSinSeleccion_retornaVacio() {
        // Arrange
        DroneAcuatico drone = new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE);
        when(repositorioMock.listarTodos()).thenReturn(List.of(drone));
        when(estrategiaMock.seleccionar(List.of(drone), solicitud)).thenReturn(Optional.empty());
        // Act
        Optional<Mision> mision = asignador.asignar(solicitud);
        // Assert
        assertTrue(mision.isEmpty());
    }

    @Test
    @DisplayName("Asignar una solicitud nula lanza NullPointerException")
    void asignar_solicitudNula_lanzaExcepcion() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> asignador.asignar(null));
    }

    @Test
    @DisplayName("Misión CRÍTICA sin drones disponibles notifica a los observadores")
    void asignar_misionCriticaSinDrones_notificaObservadores() {
        // Arrange
        when(repositorioMock.listarTodos()).thenReturn(List.of());
        // Act
        asignador.asignar(solicitudCritica);
        // Assert
        verify(centroControlMock, times(1)).notificarFalloAsignacion(eq(solicitudCritica), any());
    }

    @Test
    @DisplayName("Misión NORMAL sin drones disponibles no genera alerta")
    void asignar_misionNormalSinDrones_noNotificaObservadores() {
        // Arrange
        when(repositorioMock.listarTodos()).thenReturn(List.of());
        // Act
        asignador.asignar(solicitud);
        // Assert
        verify(centroControlMock, never()).notificarFalloAsignacion(any(), any());
    }

    @Test
    @DisplayName("Si la estrategia elige un drone en FALLO se asigna un drone alternativo")
    void asignar_droneEnFallo_asignaDroneAlternativo() {
        // Arrange
        DroneAcuatico droneEnFallo = conEstado(new DroneSuperficial("AR-15", 68, ZonaHidrica.RIBERA_ESTE),
                EstadoDrone.FALLO);
        DroneAcuatico alternativo = new DroneSuperficial("AR-04", 73, ZonaHidrica.RIBERA_ESTE);
        when(repositorioMock.listarTodos()).thenReturn(List.of(alternativo));
        when(estrategiaMock.seleccionar(any(), any()))
                .thenReturn(Optional.of(droneEnFallo))
                .thenReturn(Optional.of(alternativo));
        // Act
        Optional<Mision> mision = asignador.asignar(solicitud);
        // Assert
        assertEquals(Optional.of(alternativo), mision.map(Mision::getDrone));
    }

    @Test
    @DisplayName("Un drone en FALLO durante la asignación notifica a todos los observadores exactamente una vez")
    void asignar_droneEnFallo_notificaATodosLosObservadoresUnaVez() {
        // Arrange
        DroneAcuatico droneEnFallo = conEstado(new DroneSuperficial("AR-15", 68, ZonaHidrica.RIBERA_ESTE),
                EstadoDrone.FALLO);
        DroneAcuatico alternativo = new DroneSuperficial("AR-04", 73, ZonaHidrica.RIBERA_ESTE);
        when(repositorioMock.listarTodos()).thenReturn(List.of(alternativo));
        when(estrategiaMock.seleccionar(any(), any()))
                .thenReturn(Optional.of(droneEnFallo))
                .thenReturn(Optional.of(alternativo));
        // Act
        asignador.asignar(solicitud);
        // Assert
        verify(centroControlMock, times(1)).notificarFalloAsignacion(eq(solicitud), any());
        verify(tecnicoMock, times(1)).notificarFalloAsignacion(eq(solicitud), any());
    }

    @Test
    @DisplayName("Si un drone falla durante la asignación se excluye de los candidatos al buscar el alternativo")
    void asignar_droneFallaDuranteAsignacion_excluyeloDeLosCandidatos() {
        // Arrange
        DroneAcuatico droneQueFalla = new DroneSuperficial("AR-15", 90, ZonaHidrica.RIBERA_ESTE);
        DroneAcuatico alternativo = new DroneSuperficial("AR-04", 73, ZonaHidrica.RIBERA_ESTE);
        when(repositorioMock.listarTodos()).thenReturn(List.of(droneQueFalla, alternativo));
        when(estrategiaMock.seleccionar(any(), any()))
                .thenAnswer(invocacion -> {
                    droneQueFalla.actualizarEstado(EstadoDrone.FALLO);
                    return Optional.of(droneQueFalla);
                })
                .thenReturn(Optional.of(alternativo));
        // Act
        asignador.asignar(solicitud);
        // Assert
        verify(estrategiaMock, times(1)).seleccionar(List.of(alternativo), solicitud);
    }

    @Test
    @DisplayName("Si la estrategia elige un drone con batería crítica no se asigna y se busca un alternativo")
    void asignar_estrategiaEligeDroneConBateriaCritica_asignaDroneAlternativo() {
        // Arrange
        DroneAcuatico bateriaCritica = new DroneSuperficial("AR-03", 18, ZonaHidrica.LAGUNA_SUR);
        DroneAcuatico alternativo = new DroneSuperficial("AR-04", 73, ZonaHidrica.RIBERA_ESTE);
        when(repositorioMock.listarTodos()).thenReturn(List.of(alternativo));
        when(estrategiaMock.seleccionar(any(), any()))
                .thenReturn(Optional.of(bateriaCritica))
                .thenReturn(Optional.of(alternativo));
        // Act
        Optional<Mision> mision = asignador.asignar(solicitud);
        // Assert
        assertEquals(Optional.of(alternativo), mision.map(Mision::getDrone));
    }

    @Test
    @DisplayName("Un observador eliminado ya no recibe alertas")
    void eliminarObservador_observadorEliminado_noEsNotificado() {
        // Arrange
        asignador.eliminarObservador(tecnicoMock);
        when(repositorioMock.listarTodos()).thenReturn(List.of());
        // Act
        asignador.asignar(solicitudCritica);
        // Assert
        verify(tecnicoMock, never()).notificarFalloAsignacion(any(), any());
    }

    @Test
    @DisplayName("Registrar un observador nulo lanza NullPointerException")
    void registrarObservador_observadorNulo_lanzaExcepcion() {
        // Act + Assert
        assertThrows(NullPointerException.class, () -> asignador.registrarObservador(null));
    }

    private static DroneAcuatico conEstado(DroneAcuatico drone, EstadoDrone estado) {
        drone.actualizarEstado(estado);
        return drone;
    }
}
