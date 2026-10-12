package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ConsultorFlota {

    private static final int BATERIA_MINIMA_OPERACION = 35;

    public List<DroneAcuatico> obtenerDisponiblesOrdenadosPorBateria(List<DroneAcuatico> flota) {
        return validarFlota(flota).stream()
                .filter(ConsultorFlota::isOperativo)
                .sorted(Comparator.comparingInt(DroneAcuatico::bateria).reversed())
                .toList();
    }

    public List<String> obtenerIdsDisponibles(List<DroneAcuatico> flota) {
        return validarFlota(flota).stream()
                .filter(DroneAcuatico::disponible)
                .map(DroneAcuatico::id)
                .toList();
    }

    public boolean hasDroneOperativo(List<DroneAcuatico> flota) {
        return validarFlota(flota).stream()
                .anyMatch(ConsultorFlota::isOperativo);
    }

    public long contarDisponibles(List<DroneAcuatico> flota) {
        return validarFlota(flota).stream()
                .filter(DroneAcuatico::disponible)
                .count();
    }

    public Optional<DroneAcuatico> obtenerDroneConMayorBateria(List<DroneAcuatico> flota) {
        return validarFlota(flota).stream()
                .max(Comparator.comparingInt(DroneAcuatico::bateria));
    }

    private static boolean isOperativo(DroneAcuatico drone) {
        return drone.disponible() && drone.bateria() >= BATERIA_MINIMA_OPERACION;
    }

    private static List<DroneAcuatico> validarFlota(List<DroneAcuatico> flota) {
        return Objects.requireNonNull(flota, "La flota de drones no puede ser nula");
    }
}