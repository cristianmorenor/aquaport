package edu.eci.dosw.aquaport.domain;

public interface ObservadorMision {

    void notificarFalloAsignacion(SolicitudMision solicitud, String motivo);
}