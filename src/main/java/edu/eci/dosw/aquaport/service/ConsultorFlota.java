package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ConsultorFlota {

    public List<DroneAcuatico> obtenerDisponiblesOrdenadosPorBateria(List<DroneAcuatico> flota) {
        return validarFlota(flota).stream()
                .filter(ConsultorFlota::isOperativo)
                .sorted(Comparator.comparingInt(DroneAcuatico::getBateria).reversed())
                .toList();
    }

    public List<String> obtenerIdsDisponibles(List<DroneAcuatico> flota) {
        return validarFlota(flota).stream()
                .filter(DroneAcuatico::isDisponible)
                .map(DroneAcuatico::getId)
                .toList();
    }

    public boolean hasDroneOperativo(List<DroneAcuatico> flota) {
        return validarFlota(flota).stream()
                .anyMatch(ConsultorFlota::isOperativo);
    }

    public long contarDisponibles(List<DroneAcuatico> flota) {
        return validarFlota(flota).stream()
                .filter(DroneAcuatico::isDisponible)
                .count();
    }

    public Optional<DroneAcuatico> obtenerDroneConMayorBateria(List<DroneAcuatico> flota) {
        return validarFlota(flota).stream()
                .max(Comparator.comparingInt(DroneAcuatico::getBateria));
    }

    private static boolean isOperativo(DroneAcuatico drone) {
        return drone.isDisponible() && drone.getBateria() >= DroneAcuatico.BATERIA_MINIMA_OPERACION;
    }

    private static List<DroneAcuatico> validarFlota(List<DroneAcuatico> flota) {
        return Objects.requireNonNull(flota, "La flota de drones no puede ser nula");
    }
}