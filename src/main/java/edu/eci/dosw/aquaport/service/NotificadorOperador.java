package edu.eci.dosw.aquaport.service;

import edu.eci.dosw.aquaport.domain.Mision;

import java.io.PrintStream;
import java.util.Objects;

public class NotificadorOperador {

    private final PrintStream salida;

    public NotificadorOperador(PrintStream salida) {
        this.salida = Objects.requireNonNull(salida, "La salida de notificaciones no puede ser nula");
    }

    public void notificarMisionRegistrada(Mision mision) {
        salida.println("Misión " + mision.getId() + " registrada: drone " + mision.getDrone().id()
                + " de " + mision.getPuntoPartida() + " a " + mision.getPuntoLlegada()
                + " con " + mision.getTipoCarga() + " (" + mision.getEstado() + ")");
    }

    public void notificarError(String mensaje) {
        salida.println("Error: " + mensaje);
    }
}