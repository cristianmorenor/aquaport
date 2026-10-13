package edu.eci.dosw.aquaport;

import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.service.ConsultorFlota;

import java.util.List;

public class AquaPortApp {

    private static final String MODELO_FLOTA = "Aqua-Ranger 100";

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
        return List.of(
                new DroneAcuatico("AR-01", MODELO_FLOTA, 92, true, "Embalse Norte"),
                new DroneAcuatico("AR-02", MODELO_FLOTA, 45, true, "Canal Central"),
                new DroneAcuatico("AR-03", MODELO_FLOTA, 18, false, "Laguna Sur"),
                new DroneAcuatico("AR-04", MODELO_FLOTA, 73, true, "Punto Ribereño Este")
        );
    }

    private static List<String> describirDrones(List<DroneAcuatico> drones) {
        return drones.stream()
                .map(AquaPortApp::describirDrone)
                .toList();
    }

    private static String describirDrone(DroneAcuatico drone) {
        return drone.id() + " (" + drone.bateria() + "%)";
    }
}