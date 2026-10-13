# Plantilla DOSW — AP-07 Asignar automáticamente drone a misión hídrica

## Identificación

| Campo | Valor |
|---|---|
| Código | AP-07 |
| Nombre | Asignar automáticamente drone a misión hídrica |
| Actor principal | Sistema AquaPort (a partir de la solicitud del Operador Hídrico) |
| Actores secundarios | API Condiciones Hídricas, Centro de Control ECI, Técnico de Mantenimiento |
| Descripción | El sistema recibe una solicitud de transporte, consulta las condiciones del agua, selecciona el drone más adecuado con la estrategia activa, lo valida y lo asigna, notificando a los observadores cuando hay un fallo. |
| Prioridad | Must Have |

## Precondiciones

1. La flota tiene al menos un drone registrado.
2. Hay una estrategia de selección activa: `MayorBateriaStrategy` o `ZonaCercanaStrategy`.
3. El centro de control y el técnico de mantenimiento están registrados como observadores.

## Datos de entrada

| Nombre | Tipo de campo | Reglas | Obligatorio |
|---|---|---|---|
| solicitud | — | Solicitud de transporte. | Sí |
| solicitud.id | `String` | Formato M-XXX. No puede estar vacío. | Sí |
| carga | — | — | Sí |
| carga.peso | `Integer` | Entre 1 y 1500 gramos. | Sí |
| carga.tipo | `Enum(MUESTRA_AGUA, SENSOR, PAQUETE_LIGERO, EQUIPO_MEDICION)` | — | Sí |
| carga.prioridad | `Enum(CRITICA, ALTA, NORMAL, BAJA)` | — | Sí |
| zonaDestino | `Enum(EMBALSE_NORTE, CANAL_CENTRAL, LAGUNA_SUR, RIBERA_ESTE, LAB_HIDRICO)` | — | Sí |

## Datos intermedios (consultados por el sistema)

| Nombre | Tipo de campo | Reglas |
|---|---|---|
| condiciones | — | Respuesta de la API Condiciones Hídricas para la zona de destino. |
| condiciones.nivelAgitacion | `Enum(BAJO, MEDIO, ALTO)` | ALTO excluye a los drones superficiales. |
| condiciones.profundidadRequeridaMetros | `Integer` | Mayor a 0 exige un drone buceador. |
| candidatos | `List<DroneAcuatico(id:String, tipo:String, bateria:Integer, estado:EstadoDrone, zona:ZonaHidrica, capacidadCargaGramos:Integer)>` | Drones operativos, con capacidad para la carga y aptos para las condiciones. |

## Datos de salida

| Nombre | Tipo de campo | Reglas | Obligatorio |
|---|---|---|---|
| droneAsignado | — | Calculado por el sistema según la estrategia activa. | No (salida) |
| droneAsignado.id | `String` | Formato AR-XX. | No (salida) |
| droneAsignado.tipo | `String` | SUPERFICIAL / SEMISUMERGIDO / BUCEADOR. | No (salida) |
| droneAsignado.estado | `Enum(EstadoDrone)` | Queda en EN_MISION. | No (salida) |
| mision | — | Misión creada. | No (salida) |
| mision.id | `String` | Igual al id de la solicitud. | No (salida) |
| mision.estado | `Enum(PENDIENTE, EN_TRANSITO, ENTREGADA, FALLIDA)` | PENDIENTE al crearse. | No (salida) |
| alerta | — | Solo si hay un fallo. | No (salida) |
| alerta.motivo | `String` | Mensaje en español con el id del drone o de la misión. | No (salida) |

## Flujo básico

1. El sistema recibe la solicitud de transporte del Operador Hídrico.
2. El sistema consulta a la API Condiciones Hídricas el nivel de agitación y la profundidad de la zona de destino.
3. El sistema filtra los drones aptos para la zona: operativos (DISPONIBLE y batería ≥ 35%), con capacidad para `carga.peso` y de un tipo compatible con las condiciones.
4. El sistema aplica la estrategia activa: `MayorBateriaStrategy` para misiones NORMAL, ALTA y BAJA, o `ZonaCercanaStrategy` para misiones CRÍTICAS.
5. El sistema valida el drone elegido: batería, capacidad de carga y tipo compatible.
6. El sistema asigna el drone, crea la misión en estado PENDIENTE y cambia el drone a EN_MISION.
7. El sistema notifica a los observadores registrados cuando se presentó algún fallo durante el proceso.

## Flujos alternos

### FA-01 Sin drones disponibles

Se activa en el paso 3, cuando no queda ningún candidato.

1. El sistema no crea la misión.
2. Si la prioridad es CRÍTICA, el sistema notifica a todos los observadores: "No hay drones disponibles para la misión crítica M-050".
3. Si no es crítica, la solicitud queda sin asignar y el operador ve el motivo.

### FA-02 Condiciones hídricas adversas

Se activa en el paso 2, cuando las condiciones no permiten operar ningún tipo de drone (por ejemplo, agitación ALTA y profundidad que exige un buceador sin buceadores operativos).

1. El sistema no consulta la estrategia.
2. El sistema rechaza la solicitud con el mensaje: "Las condiciones de Laguna Sur (agitación ALTA) no permiten operar ningún drone disponible".
3. Si la misión es CRÍTICA, se notifica a los observadores.

### FA-03 La carga supera la capacidad del tipo

Se activa en el paso 3, cuando `carga.peso` supera la capacidad de un tipo de drone.

1. Los drones de ese tipo se excluyen de los candidatos. Por ejemplo, una carga de 500 g excluye a todos los buceadores (300 g).
2. Si ningún tipo puede llevar la carga, se continúa con FA-01.

### FA-04 El drone elegido no está operativo

Se activa en el paso 5, cuando el drone elegido entró en FALLO o tiene batería crítica.

1. El sistema notifica a los observadores: "El drone AR-15 no está operativo (estado FALLO, batería 68%); se busca un drone alternativo".
2. El sistema excluye ese drone de los candidatos y vuelve al paso 4.

## Reglas de negocio

| Código | Regla |
|---|---|
| RN-01 | Solo se asignan drones con batería ≥ 35%. |
| RN-02 | El DroneBuceador no se asigna para cargas mayores a 300 g. |
| RN-03 | Un drone no puede tener más de una misión activa al mismo tiempo. |
| RN-04 | Las misiones CRÍTICAS tienen precedencia absoluta y usan el criterio de zona (AP-08). |

## Postcondiciones

1. Si hubo asignación: la misión queda en PENDIENTE y el drone en EN_MISION.
2. Si hubo un fallo relevante: cada observador registrado recibió exactamente una alerta por ese fallo.
