package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;

import java.util.Objects;
import java.util.Set;
public class ValidadorMision {

    private static final Set<String> ZONAS_CAMPUS = Set.of(
            "Embalse Norte",
            "Canal Central",
            "Laguna Sur",
            "Punto Ribereño Este",
            "Laboratorio Hídrico"
    );

    public boolean hasBateriaSuficiente(DroneAcuatico drone) {
        return validarDrone(drone).getBateria() >= DroneAcuatico.BATERIA_MINIMA_OPERACION;
    }

    public boolean isDroneDisponible(DroneAcuatico drone) {
        return validarDrone(drone).isDisponible();
    }


    public void validarPuntoLlegada(String puntoLlegada) {
        if (puntoLlegada == null || puntoLlegada.isBlank()) {
            throw new IllegalArgumentException("El punto de llegada de la misión no puede ser nulo ni vacío");
        }
    }

    public boolean isZonaValida(String zona) {
        return zona != null && ZONAS_CAMPUS.contains(zona);
    }
    private static DroneAcuatico validarDrone(DroneAcuatico drone) {
        return Objects.requireNonNull(drone, "El drone a validar no puede ser nulo");
    }
}