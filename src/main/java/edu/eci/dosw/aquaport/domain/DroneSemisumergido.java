package edu.eci.dosw.aquaport.domain;

public class DroneSemisumergido extends DroneAcuatico {

    private static final String MODELO = "Aqua-Ranger 200";
    private static final int CAPACIDAD_CARGA_GRAMOS = 1500;

    public DroneSemisumergido(String id, int bateria, ZonaHidrica zona) {
        super(id, MODELO, bateria, zona);
    }

    @Override
    public String getTipo() {
        return "SEMISUMERGIDO";
    }

    @Override
    public int getCapacidadCargaGramos() {
        return CAPACIDAD_CARGA_GRAMOS;
    }
}