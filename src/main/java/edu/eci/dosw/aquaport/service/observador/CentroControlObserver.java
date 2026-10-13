package edu.eci.dosw.aquaport.service.observador;

import edu.eci.dosw.aquaport.domain.ObservadorMision;
import edu.eci.dosw.aquaport.domain.SolicitudMision;

import java.io.PrintStream;
import java.util.Objects;

public class CentroControlObserver implements ObservadorMision {

    private final PrintStream salida;

    public CentroControlObserver(PrintStream salida) {
        this.salida = Objects.requireNonNull(salida, "La salida del centro de control no puede ser nula");
    }

    @Override
    public void notificarFalloAsignacion(SolicitudMision solicitud, String motivo) {
        salida.println("[Centro de Control] Misión " + solicitud.id() + " (" + solicitud.prioridad() + "): " + motivo);
    }
}