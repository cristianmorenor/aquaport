package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.Mision;
import edu.eci.dosw.aquaport.domain.RepositorioMisiones;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class RegistradorMisiones {

    private final RepositorioMisiones repositorio;

    public RegistradorMisiones(RepositorioMisiones repositorio) {
        this.repositorio = Objects.requireNonNull(repositorio, "El repositorio de misiones no puede ser nulo");
    }

    public void registrar(Mision mision) {
        Objects.requireNonNull(mision, "La misión a registrar no puede ser nula");
        if (repositorio.buscarPorId(mision.getId()).isPresent()) {
            throw new IllegalStateException("Ya existe una misión registrada con el id " + mision.getId());
        }
        repositorio.guardar(mision);
    }

    public Optional<Mision> buscarPorId(String id) {
        return repositorio.buscarPorId(id);
    }

    public List<Mision> listarMisiones() {
        return repositorio.listarTodas();
    }
}