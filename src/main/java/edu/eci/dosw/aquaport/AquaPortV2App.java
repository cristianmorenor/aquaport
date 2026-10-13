package edu.eci.dosw.aquaport;

import edu.eci.dosw.aquaport.config.ConfiguracionFabricaDrones;
import edu.eci.dosw.aquaport.domain.DroneAcuatico;
import edu.eci.dosw.aquaport.domain.DroneBuceador;
import edu.eci.dosw.aquaport.domain.DroneSemisumergido;
import edu.eci.dosw.aquaport.domain.DroneSuperficial;
import edu.eci.dosw.aquaport.domain.EstadoDrone;
import edu.eci.dosw.aquaport.domain.FabricaDrones;
import edu.eci.dosw.aquaport.domain.Mision;
import edu.eci.dosw.aquaport.domain.Prioridad;
import edu.eci.dosw.aquaport.domain.TipoCarga;
import edu.eci.dosw.aquaport.domain.ZonaHidrica;
import edu.eci.dosw.aquaport.service.AnalizadorFlota;
import edu.eci.dosw.aquaport.service.ClasificadorMisiones;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AquaPortV2App {

    private static final FabricaDrones FABRICA = ConfiguracionFabricaDrones.crearFabrica();

    public static void main(String[] args) {
        List<DroneAcuatico> flota = crearFlotaDeEjemplo();
        AnalizadorFlota analizador = new AnalizadorFlota();
        Set<String> tiposLaguna = Set.of(DroneSemisumergido.TIPO, DroneBuceador.TIPO);

        System.out.println("Flota de " + flota.size() + " drones");
        System.out.println("1. Disponibles por tipo: "
                + describirPorTipo(analizador.agruparDisponiblesPorTipo(flota)));
        System.out.println("2. Drone óptimo en Laguna Sur (semisumergido o buceador): "
                + analizador.obtenerDroneOptimo(flota, ZonaHidrica.LAGUNA_SUR, tiposLaguna)
                .map(AquaPortV2App::describirDrone)
                .orElse("No hay drone apto"));
        System.out.println("3. Promedio de batería por tipo: "
                + describirPromedios(analizador.calcularPromedioBateriaPorTipo(flota)));
        System.out.println("4. Misiones separadas por criticidad: "
                + describirParticion(new ClasificadorMisiones().separarCriticas(crearMisionesDeEjemplo(flota))));
        System.out.println("5. Zonas cubiertas por la flota activa: "
                + analizador.obtenerZonasCubiertasPorFlotaActiva(flota));
    }

    private static List<DroneAcuatico> crearFlotaDeEjemplo() {
        return Stream.of(crearSuperficiales(), crearSemisumergidos(), crearBuceadores())
                .flatMap(List::stream)
                .toList();
    }

    private static List<DroneAcuatico> crearSuperficiales() {
        return List.of(
                crear(DroneSuperficial.TIPO, "AR-01", 92, ZonaHidrica.EMBALSE_NORTE, EstadoDrone.DISPONIBLE),
                crear(DroneSuperficial.TIPO, "AR-02", 45, ZonaHidrica.CANAL_CENTRAL, EstadoDrone.DISPONIBLE),
                crear(DroneSuperficial.TIPO, "AR-03", 18, ZonaHidrica.LAGUNA_SUR, EstadoDrone.RECARGANDO),
                crear(DroneSuperficial.TIPO, "AR-04", 73, ZonaHidrica.RIBERA_ESTE, EstadoDrone.DISPONIBLE),
                crear(DroneSuperficial.TIPO, "AR-05", 88, ZonaHidrica.LAB_HIDRICO, EstadoDrone.DISPONIBLE),
                crear(DroneSuperficial.TIPO, "AR-06", 64, ZonaHidrica.CANAL_CENTRAL, EstadoDrone.EN_MISION));
    }

    private static List<DroneAcuatico> crearSemisumergidos() {
        return List.of(
                crear(DroneSemisumergido.TIPO, "AR-07", 81, ZonaHidrica.CANAL_CENTRAL, EstadoDrone.DISPONIBLE),
                crear(DroneSemisumergido.TIPO, "AR-08", 57, ZonaHidrica.LAGUNA_SUR, EstadoDrone.DISPONIBLE),
                crear(DroneSemisumergido.TIPO, "AR-09", 39, ZonaHidrica.RIBERA_ESTE, EstadoDrone.DISPONIBLE),
                crear(DroneSemisumergido.TIPO, "AR-10", 95, ZonaHidrica.EMBALSE_NORTE, EstadoDrone.MANTENIMIENTO),
                crear(DroneSemisumergido.TIPO, "AR-11", 70, ZonaHidrica.LAGUNA_SUR, EstadoDrone.EN_MISION));
    }

    private static List<DroneAcuatico> crearBuceadores() {
        return List.of(
                crear(DroneBuceador.TIPO, "AR-12", 76, ZonaHidrica.LAGUNA_SUR, EstadoDrone.DISPONIBLE),
                crear(DroneBuceador.TIPO, "AR-13", 52, ZonaHidrica.EMBALSE_NORTE, EstadoDrone.DISPONIBLE),
                crear(DroneBuceador.TIPO, "AR-14", 33, ZonaHidrica.LAGUNA_SUR, EstadoDrone.DISPONIBLE),
                crear(DroneBuceador.TIPO, "AR-15", 68, ZonaHidrica.RIBERA_ESTE, EstadoDrone.FALLO));
    }

    private static DroneAcuatico crear(String tipo, String id, int bateria, ZonaHidrica zona, EstadoDrone estado) {
        DroneAcuatico drone = FABRICA.crear(tipo, id, bateria, zona);
        drone.actualizarEstado(estado);
        return drone;
    }

    private static List<Mision> crearMisionesDeEjemplo(List<DroneAcuatico> flota) {
        return List.of(
                crearMision("M-001", buscarDrone(flota, "AR-12"), Prioridad.CRITICA),
                crearMision("M-002", buscarDrone(flota, "AR-01"), Prioridad.NORMAL),
                crearMision("M-003", buscarDrone(flota, "AR-07"), Prioridad.ALTA),
                crearMision("M-004", buscarDrone(flota, "AR-05"), Prioridad.CRITICA));
    }

    private static DroneAcuatico buscarDrone(List<DroneAcuatico> flota, String id) {
        return flota.stream()
                .filter(drone -> drone.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No existe el drone " + id + " en la flota"));
    }

    private static Mision crearMision(String id, DroneAcuatico drone, Prioridad prioridad) {
        return new Mision.Builder()
                .id(id)
                .drone(drone)
                .puntoPartida(drone.getZona().getNombre())
                .puntoLlegada(ZonaHidrica.LAB_HIDRICO.getNombre())
                .tipoCarga(TipoCarga.MUESTRA_AGUA)
                .prioridad(prioridad)
                .build();
    }

    private static String describirPorTipo(Map<String, List<DroneAcuatico>> porTipo) {
        return porTipo.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(grupo -> grupo.getKey() + "=" + grupo.getValue().stream().map(DroneAcuatico::getId).toList())
                .collect(Collectors.joining(", "));
    }

    private static String describirPromedios(Map<String, Double> promedios) {
        return promedios.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(promedio -> promedio.getKey() + "=" + String.format("%.1f%%", promedio.getValue()))
                .collect(Collectors.joining(", "));
    }

    private static String describirParticion(Map<Boolean, List<Mision>> particion) {
        return "críticas=" + idsDeMisiones(particion.get(true)) + ", demás=" + idsDeMisiones(particion.get(false));
    }

    private static List<String> idsDeMisiones(List<Mision> misiones) {
        return misiones.stream().map(Mision::getId).toList();
    }

    private static String describirDrone(DroneAcuatico drone) {
        return drone.getId() + " " + drone.getTipo() + " (" + drone.getBateria() + "%)";
    }
}