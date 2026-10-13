package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;

import java.util.Objects;

public class ValidadorMision {

    private static final int BATERIA_MINIMA_ASIGNACION = 35;

    public boolean hasBateriaSuficiente(DroneAcuatico drone) {
        return validarDrone(drone).bateria() >= BATERIA_MINIMA_ASIGNACION;
    }

    public boolean isDroneDisponible(DroneAcuatico drone) {
        return validarDrone(drone).disponible();
    }

    public void validarPuntoLlegada(String puntoLlegada) {
        if (puntoLlegada == null || puntoLlegada.isBlank()) {
            throw new IllegalArgumentException("El punto de llegada de la misión no puede ser nulo ni vacío");
        }
    }

    private static DroneAcuatico validarDrone(DroneAcuatico drone) {
        return Objects.requireNonNull(drone, "El drone a validar no puede ser nulo");
    }
}