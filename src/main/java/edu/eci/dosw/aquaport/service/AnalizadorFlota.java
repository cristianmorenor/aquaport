package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.EstadoDrone;
import edu.eci.dosw.aquaport.domain.ZonaHidrica;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class AnalizadorFlota {

    public Map<String, List<DroneAcuatico>> agruparDisponiblesPorTipo(List<DroneAcuatico> flota) {
        return validarFlota(flota).stream()
                .filter(DroneAcuatico::isDisponible)
                .collect(Collectors.groupingBy(DroneAcuatico::getTipo));
    }

    public Optional<DroneAcuatico> obtenerDroneOptimo(List<DroneAcuatico> flota, ZonaHidrica zona,
                                                      Set<String> tiposCompatibles) {
        Objects.requireNonNull(zona, "La zona para buscar el drone óptimo no puede ser nula");
        Objects.requireNonNull(tiposCompatibles, "Los tipos compatibles no pueden ser nulos");
        return validarFlota(flota).stream()
                .filter(DroneAcuatico::isOperativo)
                .filter(drone -> drone.getZona() == zona)
                .filter(drone -> tiposCompatibles.contains(drone.getTipo()))
                .max(Comparator.comparingInt(DroneAcuatico::getBateria));
    }

    public Map<String, Double> calcularPromedioBateriaPorTipo(List<DroneAcuatico> flota) {
        return validarFlota(flota).stream()
                .collect(Collectors.groupingBy(DroneAcuatico::getTipo,
                        Collectors.averagingInt(DroneAcuatico::getBateria)));
    }

    public List<ZonaHidrica> obtenerZonasCubiertasPorFlotaActiva(List<DroneAcuatico> flota) {
        return validarFlota(flota).stream()
                .filter(AnalizadorFlota::isActivo)
                .map(DroneAcuatico::getZona)
                .distinct()
                .sorted()
                .toList();
    }

    private static boolean isActivo(DroneAcuatico drone) {
        return drone.getEstado() != EstadoDrone.MANTENIMIENTO && drone.getEstado() != EstadoDrone.FALLO;
    }

    private static List<DroneAcuatico> validarFlota(List<DroneAcuatico> flota) {
        return Objects.requireNonNull(flota, "La flota de drones no puede ser nula");
    }
}