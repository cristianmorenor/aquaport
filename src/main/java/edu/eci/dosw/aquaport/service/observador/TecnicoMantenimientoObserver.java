package edu.eci.dosw.aquaport.service.observador;

import edu.eci.dosw.aquaport.domain.ObservadorMision;
import edu.eci.dosw.aquaport.domain.SolicitudMision;

import java.io.PrintStream;
import java.util.Objects;

public class TecnicoMantenimientoObserver implements ObservadorMision {

    private final PrintStream salida;

    public TecnicoMantenimientoObserver(PrintStream salida) {
        this.salida = Objects.requireNonNull(salida, "La salida del técnico de mantenimiento no puede ser nula");
    }

    @Override
    public void notificarFalloAsignacion(SolicitudMision solicitud, String motivo) {
        salida.println("[Técnico de Mantenimiento] Revisar la flota para la misión "
                + solicitud.id() + ": " + motivo);
    }
}