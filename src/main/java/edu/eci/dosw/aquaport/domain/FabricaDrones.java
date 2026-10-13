package edu.eci.dosw.aquaport.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class FabricaDrones {

    private final Map<String, CreadorDrone> creadoresPorTipo = new HashMap<>();

    public void registrarTipo(String tipo, CreadorDrone creador) {
        if (tipo == null || tipo.isBlank()) {
            throw new IllegalArgumentException("El tipo de drone a registrar no puede ser nulo ni vacío");
        }
        creadoresPorTipo.put(tipo, Objects.requireNonNull(creador, "El creador del tipo " + tipo + " no puede ser nulo"));
    }

    public DroneAcuatico crear(String tipo, String id, int bateria, ZonaHidrica zona) {
        CreadorDrone creador = Optional.ofNullable(creadoresPorTipo.get(tipo))
                .orElseThrow(() -> new IllegalArgumentException(
                        "No existe un tipo de drone registrado como " + tipo));
        return creador.crear(id, bateria, zona);
    }
}