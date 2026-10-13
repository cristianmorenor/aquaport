package edu.eci.dosw.aquaport.domain;

public enum ZonaHidrica {
    EMBALSE_NORTE("Embalse Norte"),
    CANAL_CENTRAL("Canal Central"),
    LAGUNA_SUR("Laguna Sur"),
    RIBERA_ESTE("Punto Ribereño Este"),
    LAB_HIDRICO("Laboratorio Hídrico");

    private final String nombre;

    ZonaHidrica(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}