package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;

import java.util.Objects;

public class ValidadorMision {

    public boolean hasBateriaSuficiente(DroneAcuatico drone) {
        Objects.requireNonNull(drone, "El drone a validar no puede ser nulo");
        return drone.bateria() >= 35;
    }

    public boolean isDroneDisponible(DroneAcuatico drone) {
        Objects.requireNonNull(drone, "El drone a validar no puede ser nulo");
        return drone.disponible();
    }

    public void validarPuntoLlegada(String puntoLlegada) {
        if (puntoLlegada == null || puntoLlegada.isBlank()) {
            throw new IllegalArgumentException("El punto de llegada de la misión no puede ser nulo ni vacío");
        }
    }
}