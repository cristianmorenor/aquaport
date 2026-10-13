package edu.eci.dosw.aquaport.domain;

@FunctionalInterface
public interface CreadorDrone {

    DroneAcuatico crear(String id, int bateria, ZonaHidrica zona);
}