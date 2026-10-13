# Plantilla DOSW — AP-01 Registrar misión de transporte de muestra

## Identificación

| Campo | Valor |
|---|---|
| Código | AP-01 |
| Nombre | Registrar misión de transporte de muestra |
| Actor | Operador Hídrico |
| Descripción | El operador registra una misión de transporte entre dos zonas del campus y le asigna manualmente un drone disponible. |
| Prioridad | Must Have |

## Precondiciones

1. Debe existir al menos un drone disponible con batería ≥ 35%.
2. Las zonas del MVP deben estar configuradas: Embalse Norte, Canal Central, Laguna Sur, Punto Ribereño Este y Laboratorio Hídrico.

## Datos de entrada

| Nombre | Tipo de campo | Reglas | Obligatorio |
|---|---|---|---|
| puntoPartida | `String` | Debe ser una de las 5 zonas del MVP. No puede estar vacío. | Sí |
| puntoLlegada | `String` | Debe ser una de las 5 zonas del MVP. No puede estar vacío. | Sí |
| tipoCarga | `Enum(MUESTRA_AGUA, SENSOR, PAQUETE_LIGERO)` | — | Sí |
| droneAsignado | — | Drone elegido por el operador de la lista de disponibles. | Sí |
| droneAsignado.id | `String` | Formato AR-XX. Debe existir en la flota. | Sí |
| droneAsignado.bateria | `Integer` | Entre 0 y 100. Debe ser ≥ 35. | Sí |
| droneAsignado.disponible | `Boolean` | Debe ser `true`. | Sí |
| droneAsignado.zona | `String` | Una de las 5 zonas del MVP. | Sí |

## Datos de salida

| Nombre | Tipo de campo | Reglas |
|---|---|---|
| dronesDisponibles | `List<DroneAcuatico(id:String, bateria:Integer, zona:String)>` | Solo drones con `disponible = true` y `bateria ≥ 35`, ordenados de mayor a menor batería. Se muestra en el paso 2. |
| mision | — | Misión creada al final del flujo. |
| mision.id | `String` | Código generado por el sistema con formato M-XXX. Único. |
| mision.estado | `Enum(PENDIENTE, EN_TRANSITO, ENTREGADA, FALLIDA)` | Siempre `PENDIENTE` al registrarse. |
| mision.droneAsignado.id | `String` | ID del drone asignado. |

## Flujo básico

1. El Operador Hídrico selecciona la opción "Registrar misión".
2. El sistema muestra `dronesDisponibles` con ID, batería y zona de cada drone.
3. El Operador ingresa `puntoPartida`, `puntoLlegada`, `tipoCarga` y elige un drone de la lista.
4. El sistema valida que el drone esté disponible, que su batería sea ≥ 35% y que ambas zonas pertenezcan al MVP.
5. El sistema genera el código de la misión, la registra en estado `PENDIENTE` y muestra al operador el código generado junto con el drone asignado.

## Flujos alternos

### FA-01 Drone sin batería suficiente

Se activa en el paso 4, cuando el drone elegido tiene batería menor a 35%.

1. El sistema no registra la misión.
2. El sistema muestra: "El drone AR-03 tiene batería insuficiente (18%). Mínimo requerido: 35%."
3. El flujo vuelve al paso 3 para que el operador elija otro drone.

### FA-02 Zona de destino inválida

Se activa en el paso 4, cuando `puntoLlegada` no es una de las 5 zonas del MVP.

1. El sistema no registra la misión.
2. El sistema muestra: "La zona 'Río Bogotá' no pertenece a las zonas del campus."
3. El flujo vuelve al paso 3 para que el operador corrija el destino.

## Reglas de negocio

| Código | Regla |
|---|---|
| RN-01 | Un drone no puede tener más de una misión activa al mismo tiempo. Al registrar la misión, el drone deja de estar disponible hasta que la misión termine o se cancele. |
| RN-02 | Solo se pueden asignar drones con batería ≥ 35%. |

## Postcondiciones

1. La misión queda registrada en estado `PENDIENTE` con su código único.
2. El drone asignado deja de aparecer en `dronesDisponibles`.

## Precondición vs. regla de negocio

- **Precondición 1** ("debe existir al menos un drone disponible con batería ≥ 35%") se verifica **antes** de iniciar: si no se cumple, el caso de uso ni siquiera arranca.
- **RN-02** ("solo drones con batería ≥ 35%") se aplica **durante** la ejecución, sobre el drone concreto que eligió el operador.
