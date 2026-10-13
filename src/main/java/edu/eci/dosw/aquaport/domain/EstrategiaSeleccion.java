package edu.eci.dosw.aquaport.domain;

import java.util.List;
import java.util.Optional;

public interface EstrategiaSeleccion {

    Optional<DroneAcuatico> seleccionar(List<DroneAcuatico> candidatos, SolicitudMision solicitud);
}