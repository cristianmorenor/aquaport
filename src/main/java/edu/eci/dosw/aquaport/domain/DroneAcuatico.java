package edu.eci.dosw.aquaport.domain;

public record DroneAcuatico(
        String id,
        String modelo,
        int bateria,
        boolean disponible,
        String zona
) {

    public static final int BATERIA_MINIMA_OPERACION = 35;
    private static final int BATERIA_MINIMA = 0;
    private static final int BATERIA_MAXIMA = 100;

    public DroneAcuatico {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("El id del drone no puede ser nulo ni vacío");
        }
        if (bateria < BATERIA_MINIMA || bateria > BATERIA_MAXIMA) {
            throw new IllegalArgumentException(
                    "La batería del drone " + id + " debe estar entre 0 y 100, pero fue " + bateria);
        }
    }
}