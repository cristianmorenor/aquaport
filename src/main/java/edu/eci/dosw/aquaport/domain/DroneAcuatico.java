package edu.eci.dosw.aquaport.domain;

import java.util.Objects;

public abstract class DroneAcuatico {

    public static final int BATERIA_MINIMA_OPERACION = 35;
    private static final int BATERIA_MINIMA = 0;
    private static final int BATERIA_MAXIMA = 100;

    private final String id;
    private final String modelo;
    private final int bateria;
    private final ZonaHidrica zona;
    private EstadoDrone estado;

    protected DroneAcuatico(String id, String modelo, int bateria, ZonaHidrica zona) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del drone no puede ser nulo ni vacío");
        }
        if (bateria < BATERIA_MINIMA || bateria > BATERIA_MAXIMA) {
            throw new IllegalArgumentException(
                    "La batería del drone " + id + " debe estar entre 0 y 100, pero fue " + bateria);
        }
        this.id = id;
        this.modelo = modelo;
        this.bateria = bateria;
        this.zona = Objects.requireNonNull(zona, "La zona del drone " + id + " no puede ser nula");
        this.estado = EstadoDrone.DISPONIBLE;
    }

    public abstract String getTipo();

    public abstract int getCapacidadCargaGramos();

    public boolean isDisponible() {
        return estado == EstadoDrone.DISPONIBLE;
    }

    public boolean canTransportar(int pesoGramos) {
        if (pesoGramos <= 0) {
            throw new IllegalArgumentException("El peso de la carga debe ser mayor a 0 gramos, pero fue " + pesoGramos);
        }
        return pesoGramos <= getCapacidadCargaGramos();
    }

    public void actualizarEstado(EstadoDrone nuevoEstado) {
        this.estado = Objects.requireNonNull(nuevoEstado, "El nuevo estado del drone " + id + " no puede ser nulo");
    }

    public String getId() {
        return id;
    }

    public String getModelo() {
        return modelo;
    }

    public int getBateria() {
        return bateria;
    }

    public ZonaHidrica getZona() {
        return zona;
    }

    public EstadoDrone getEstado() {
        return estado;
    }
}