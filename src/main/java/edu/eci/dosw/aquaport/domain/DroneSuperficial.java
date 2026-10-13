package edu.eci.dosw.aquaport.domain;

public class DroneSuperficial extends DroneAcuatico {

    private static final String MODELO = "Aqua-Ranger 100";
    private static final int CAPACIDAD_CARGA_GRAMOS = 500;

    public DroneSuperficial(String id, int bateria, ZonaHidrica zona) {
        super(id, MODELO, bateria, zona);
    }

    @Override
    public String getTipo() {
        return "SUPERFICIAL";
    }

    @Override
    public int getCapacidadCargaGramos() {
        return CAPACIDAD_CARGA_GRAMOS;
    }
}