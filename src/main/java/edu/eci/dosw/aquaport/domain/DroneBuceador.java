package edu.eci.dosw.aquaport.domain;

public class DroneBuceador extends DroneAcuatico {

    private static final String MODELO = "Aqua-Ranger 300";
    private static final int CAPACIDAD_CARGA_GRAMOS = 300;

    public DroneBuceador(String id, int bateria, ZonaHidrica zona) {
        super(id, MODELO, bateria, zona);
    }

    @Override
    public String getTipo() {
        return "BUCEADOR";
    }

    @Override
    public int getCapacidadCargaGramos() {
        return CAPACIDAD_CARGA_GRAMOS;
    }
}