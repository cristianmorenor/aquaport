package edu.eci.dosw.aquaport.config;

import edu.eci.dosw.aquaport.domain.DroneBuceador;
import edu.eci.dosw.aquaport.domain.DroneSemisumergido;
import edu.eci.dosw.aquaport.domain.DroneSuperficial;
import edu.eci.dosw.aquaport.domain.FabricaDrones;

public final class ConfiguracionFabricaDrones {

    private ConfiguracionFabricaDrones() {
    }

    public static FabricaDrones crearFabrica() {
        FabricaDrones fabrica = new FabricaDrones();
        fabrica.registrarTipo(DroneSuperficial.TIPO, DroneSuperficial::new);
        fabrica.registrarTipo(DroneSemisumergido.TIPO, DroneSemisumergido::new);
        fabrica.registrarTipo(DroneBuceador.TIPO, DroneBuceador::new);
        return fabrica;
    }
}