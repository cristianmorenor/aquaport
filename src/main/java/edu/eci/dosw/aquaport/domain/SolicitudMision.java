package edu.eci.dosw.aquaport.domain;

import java.util.Objects;

public record SolicitudMision(
        String id,
        ZonaHidrica zonaDestino,
        TipoCarga tipoCarga,
        int pesoGramos,
        Prioridad prioridad
) {
    private static final int PESO_MINIMO_GRAMOS = 1;
    private static final int PESO_MAXIMO_GRAMOS = 1500;

    public SolicitudMision {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id de la solicitud no puede ser nulo ni vacío");
        }
        Objects.requireNonNull(zonaDestino, "La zona de destino de la solicitud " + id + " no puede ser nula");
        Objects.requireNonNull(tipoCarga, "El tipo de carga de la solicitud " + id + " no puede ser nulo");
        Objects.requireNonNull(prioridad, "La prioridad de la solicitud " + id + " no puede ser nula");
        if (pesoGramos < PESO_MINIMO_GRAMOS || pesoGramos > PESO_MAXIMO_GRAMOS) {
            throw new IllegalArgumentException("El peso de la carga de la solicitud " + id
                    + " debe estar entre 1 y 1500 gramos, pero fue " + pesoGramos);
        }
    }

    public boolean isCritica() {
        return prioridad == Prioridad.CRITICA;
    }
}