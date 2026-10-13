package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.EstadoDrone;
import edu.eci.dosw.aquaport.domain.EstrategiaSeleccion;
import edu.eci.dosw.aquaport.domain.Mision;
import edu.eci.dosw.aquaport.domain.RepositorioDrones;
import edu.eci.dosw.aquaport.domain.SolicitudMision;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AsignadorAutomatico {

    private final RepositorioDrones repositorioDrones;
    private EstrategiaSeleccion estrategia;

    public AsignadorAutomatico(EstrategiaSeleccion estrategia, RepositorioDrones repositorioDrones) {
        this.estrategia = Objects.requireNonNull(estrategia, "La estrategia de selección no puede ser nula");
        this.repositorioDrones = Objects.requireNonNull(repositorioDrones,
                "El repositorio de drones no puede ser nulo");
    }

    public void cambiarEstrategia(EstrategiaSeleccion nuevaEstrategia) {
        this.estrategia = Objects.requireNonNull(nuevaEstrategia, "La nueva estrategia no puede ser nula");
    }

    public Optional<Mision> asignar(SolicitudMision solicitud) {
        Objects.requireNonNull(solicitud, "La solicitud de misión no puede ser nula");
        List<DroneAcuatico> candidatos = filtrarAptos(solicitud);
        if (candidatos.isEmpty()) {
            return Optional.empty();
        }
        return estrategia.seleccionar(candidatos, solicitud)
                .map(drone -> asignarDrone(solicitud, drone));
    }

    private List<DroneAcuatico> filtrarAptos(SolicitudMision solicitud) {
        return repositorioDrones.listarTodos().stream()
                .filter(DroneAcuatico::isOperativo)
                .filter(drone -> drone.canTransportar(solicitud.pesoGramos()))
                .toList();
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