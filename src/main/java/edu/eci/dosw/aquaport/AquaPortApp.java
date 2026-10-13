package edu.eci.dosw.aquaport;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.DroneSuperficial;
import edu.eci.dosw.aquaport.domain.EstadoDrone;
import edu.eci.dosw.aquaport.domain.ZonaHidrica;
import edu.eci.dosw.aquaport.service.ConsultorFlota;

import java.util.List;

public class AquaPortApp {

    public static void main(String[] args) {
        List<DroneAcuatico> flota = crearFlotaDeEjemplo();
        ConsultorFlota consultor = new ConsultorFlota();

        System.out.println("1. Drones disponibles con batería >= 35% (mayor a menor): "
                + describirDrones(consultor.obtenerDisponiblesOrdenadosPorBateria(flota)));
        System.out.println("2. IDs de drones disponibles: "
                + consultor.obtenerIdsDisponibles(flota));
        System.out.println("3. ¿Existe algún drone disponible con batería >= 35%?: "
                + (consultor.hasDroneOperativo(flota) ? "Sí" : "No"));
        System.out.println("4. Cantidad de drones disponibles: "
                + consultor.contarDisponibles(flota));
        System.out.println("5. Drone con mayor batería de la flota: "
                + consultor.obtenerDroneConMayorBateria(flota)
                .map(AquaPortApp::describirDrone)
                .orElse("La flota está vacía"));
    }

    private static List<DroneAcuatico> crearFlotaDeEjemplo() {
        DroneAcuatico droneRecargando = new DroneSuperficial("AR-03", 18, ZonaHidrica.LAGUNA_SUR);
        droneRecargando.actualizarEstado(EstadoDrone.RECARGANDO);
        return List.of(
                new DroneSuperficial("AR-01", 92, ZonaHidrica.EMBALSE_NORTE),
                new DroneSuperficial("AR-02", 45, ZonaHidrica.CANAL_CENTRAL),
                droneRecargando,
                new DroneSuperficial("AR-04", 73, ZonaHidrica.RIBERA_ESTE)
        );
    }

    private static List<String> describirDrones(List<DroneAcuatico> drones) {
        return drones.stream()
                .map(AquaPortApp::describirDrone)
                .toList();
    }

    private static String describirDrone(DroneAcuatico drone) {
        return drone.getId() + " (" + drone.getBateria() + "%)";
    }
}