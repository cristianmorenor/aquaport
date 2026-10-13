package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.ZonaHidrica;

import java.util.Arrays;
import java.util.Objects;

public class ValidadorMision {

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
        return Arrays.stream(ZonaHidrica.values())
                .map(ZonaHidrica::getNombre)
                .anyMatch(nombre -> nombre.equals(zona));
    }

    private static DroneAcuatico validarDrone(DroneAcuatico drone) {
        return Objects.requireNonNull(drone, "El drone a validar no puede ser nulo");
    }
}