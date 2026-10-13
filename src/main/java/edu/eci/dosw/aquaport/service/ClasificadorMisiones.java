package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.Mision;
import edu.eci.dosw.aquaport.domain.Prioridad;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClasificadorMisiones {

    public Map<Boolean, List<Mision>> separarCriticas(List<Mision> misiones) {
        return Objects.requireNonNull(misiones, "La lista de misiones no puede ser nula").stream()
                .collect(Collectors.partitioningBy(mision -> mision.getPrioridad() == Prioridad.CRITICA));
    }
}