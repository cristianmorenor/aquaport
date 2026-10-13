package edu.eci.dosw.aquaport.repository;

import edu.eci.dosw.aquaport.domain.Mision;
import edu.eci.dosw.aquaport.domain.RepositorioMisiones;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RepositorioMisionesEnMemoria implements RepositorioMisiones {

    private final Map<String, Mision> misionesPorId = new LinkedHashMap<>();

    @Override
    public void guardar(Mision mision) {
        misionesPorId.put(mision.getId(), mision);
    }

    @Override
    public Optional<Mision> buscarPorId(String id) {
        return Optional.ofNullable(misionesPorId.get(id));
    }

    @Override
    public List<Mision> listarTodas() {
        return List.copyOf(misionesPorId.values());
    }
}