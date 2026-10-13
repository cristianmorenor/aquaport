package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.EstadoDrone;
import edu.eci.dosw.aquaport.domain.EstrategiaSeleccion;
import edu.eci.dosw.aquaport.domain.Mision;
import edu.eci.dosw.aquaport.domain.ObservadorMision;
import edu.eci.dosw.aquaport.domain.RepositorioDrones;
import edu.eci.dosw.aquaport.domain.SolicitudMision;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AsignadorAutomatico {

    private final RepositorioDrones repositorioDrones;
    private final List<ObservadorMision> observadores = new ArrayList<>();
    private EstrategiaSeleccion estrategia;

    public AsignadorAutomatico(EstrategiaSeleccion estrategia, RepositorioDrones repositorioDrones) {
        this.estrategia = Objects.requireNonNull(estrategia, "La estrategia de selección no puede ser nula");
        this.repositorioDrones = Objects.requireNonNull(repositorioDrones,
                "El repositorio de drones no puede ser nulo");
    }

    public void cambiarEstrategia(EstrategiaSeleccion nuevaEstrategia) {
        this.estrategia = Objects.requireNonNull(nuevaEstrategia, "La nueva estrategia no puede ser nula");
    }

    public void registrarObservador(ObservadorMision observador) {
        observadores.add(Objects.requireNonNull(observador, "El observador a registrar no puede ser nulo"));
    }

    public void eliminarObservador(ObservadorMision observador) {
        observadores.remove(observador);
    }

    public Optional<Mision> asignar(SolicitudMision solicitud) {
        Objects.requireNonNull(solicitud, "La solicitud de misión no puede ser nula");
        Optional<Mision> mision = seleccionarDroneOperativo(filtrarAptos(solicitud), solicitud)
                .map(drone -> asignarDrone(solicitud, drone));
        if (mision.isEmpty() && solicitud.isCritica()) {
            notificarObservadores(solicitud, "No hay drones disponibles para la misión crítica " + solicitud.id());
        }
        return mision;
    }

    private List<DroneAcuatico> filtrarAptos(SolicitudMision solicitud) {
        return repositorioDrones.listarTodos().stream()
                .filter(DroneAcuatico::isOperativo)
                .filter(drone -> drone.canTransportar(solicitud.pesoGramos()))
                .toList();
    }

    private Optional<DroneAcuatico> seleccionarDroneOperativo(List<DroneAcuatico> candidatos,
                                                             SolicitudMision solicitud) {
        if (candidatos.isEmpty()) {
            return Optional.empty();
        }
        return estrategia.seleccionar(candidatos, solicitud)
                .flatMap(drone -> descartarSiNoEstaOperativo(drone, candidatos, solicitud));
    }

    private Optional<DroneAcuatico> descartarSiNoEstaOperativo(DroneAcuatico drone, List<DroneAcuatico> candidatos,
                                                              SolicitudMision solicitud) {
        if (drone.isOperativo()) {
            return Optional.of(drone);
        }
        notificarObservadores(solicitud, "El drone " + drone.getId() + " no está operativo (estado "
                + drone.getEstado() + ", batería " + drone.getBateria() + "%); se busca un drone alternativo");
        List<DroneAcuatico> sinDroneDescartado = candidatos.stream()
                .filter(candidato -> candidato != drone)
                .toList();
        return seleccionarDroneOperativo(sinDroneDescartado, solicitud);
    }

    private void notificarObservadores(SolicitudMision solicitud, String motivo) {
        observadores.forEach(observador -> observador.notificarFalloAsignacion(solicitud, motivo));
    }

    private Mision asignarDrone(SolicitudMision solicitud, DroneAcuatico drone) {
        Mision mision = new Mision.Builder()
                .id(solicitud.id())
                .drone(drone)
                .puntoPartida(drone.getZona().getNombre())
                .puntoLlegada(solicitud.zonaDestino().getNombre())
                .tipoCarga(solicitud.tipoCarga())
                .prioridad(solicitud.prioridad())
                .build();
        drone.actualizarEstado(EstadoDrone.EN_MISION);
        return mision;
    }
}
