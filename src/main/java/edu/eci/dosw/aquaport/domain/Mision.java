package edu.eci.dosw.aquaport.domain;

public final class Mision {

    private final String id;
    private final DroneAcuatico drone;
    private final String puntoPartida;
    private final String puntoLlegada;
    private final TipoCarga tipoCarga;
    private final EstadoMision estado;
    private final Prioridad prioridad;

    private Mision(Builder builder) {
        this.id = builder.id;
        this.drone = builder.drone;
        this.puntoPartida = builder.puntoPartida;
        this.puntoLlegada = builder.puntoLlegada;
        this.tipoCarga = builder.tipoCarga;
        this.estado = builder.estado;
        this.prioridad = builder.prioridad;
    }

    public String getId() {
        return id;
    }

    public DroneAcuatico getDrone() {
        return drone;
    }

    public String getPuntoPartida() {
        return puntoPartida;
    }

    public String getPuntoLlegada() {
        return puntoLlegada;
    }

    public TipoCarga getTipoCarga() {
        return tipoCarga;
    }

    public EstadoMision getEstado() {
        return estado;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public static final class Builder {

        private String id;
        private DroneAcuatico drone;
        private String puntoPartida;
        private String puntoLlegada;
        private TipoCarga tipoCarga;
        private EstadoMision estado = EstadoMision.PENDIENTE;
        private Prioridad prioridad = Prioridad.NORMAL;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder drone(DroneAcuatico drone) {
            this.drone = drone;
            return this;
        }

        public Builder puntoPartida(String puntoPartida) {
            this.puntoPartida = puntoPartida;
            return this;
        }

        public Builder puntoLlegada(String puntoLlegada) {
            this.puntoLlegada = puntoLlegada;
            return this;
        }

        public Builder tipoCarga(TipoCarga tipoCarga) {
            this.tipoCarga = tipoCarga;
            return this;
        }

        public Builder estado(EstadoMision estado) {
            this.estado = estado;
            return this;
        }

        public Builder prioridad(Prioridad prioridad) {
            this.prioridad = prioridad;
            return this;
        }

        public Mision build() {
            validarTextoObligatorio(id, "id");
            validarTextoObligatorio(puntoPartida, "puntoPartida");
            validarTextoObligatorio(puntoLlegada, "puntoLlegada");
            validarDrone();
            validarObligatorio(tipoCarga, "tipoCarga");
            validarObligatorio(estado, "estado");
            validarObligatorio(prioridad, "prioridad");
            return new Mision(this);
        }

        private void validarDrone() {
            validarObligatorio(drone, "drone");
            if (!drone.isDisponible()) {
                throw new IllegalStateException(
                        "El drone " + drone.getId() + " no está disponible para asignarle una misión");
            }
        }

        private static void validarTextoObligatorio(String valor, String nombreCampo) {
            if (valor == null || valor.isBlank()) {
                throw new IllegalStateException(
                        "El campo " + nombreCampo + " de la misión es obligatorio y no puede estar vacío");
            }
        }

        private static void validarObligatorio(Object valor, String nombreCampo) {
            if (valor == null) {
                throw new IllegalStateException(
                        "El campo " + nombreCampo + " de la misión es obligatorio");
            }
        }
    }
}