package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.DroneBuceador;
import edu.eci.dosw.aquaport.domain.DroneSuperficial;
import edu.eci.dosw.aquaport.domain.EstadoDrone;
import edu.eci.dosw.aquaport.domain.EstrategiaSeleccion;
import edu.eci.dosw.aquaport.domain.Mision;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsignadorAutomaticoTest {

    @Mock
    private EstrategiaSeleccion estrategiaMock;

    @Mock
    private RepositorioDrones repositorioMock;

    @InjectMocks
    private AsignadorAutomatico asignador;

    private SolicitudMision solicitud;

    @BeforeEach
    void setUp() {
        solicitud = new SolicitudMision("M-040", ZonaHidrica.LAB_HIDRICO, TipoCarga.MUESTRA_AGUA,
                200, Prioridad.NORMAL);
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
}