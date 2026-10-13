package edu.eci.dosw.aquaport.service.estrategia;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.EstrategiaSeleccion;
import edu.eci.dosw.aquaport.domain.SolicitudMision;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class MayorBateriaStrategy implements EstrategiaSeleccion {

    @Override
    public Optional<DroneAcuatico> seleccionar(List<DroneAcuatico> candidatos, SolicitudMision solicitud) {
        return candidatos.stream()
                .max(Comparator.comparingInt(DroneAcuatico::getBateria));
    }
}