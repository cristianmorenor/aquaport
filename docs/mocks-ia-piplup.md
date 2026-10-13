# Mocks con IA — Panel de monitoreo de la flota (Piplup)

Mocks generados con IA a partir de la identidad definida en `identidad-ux-piplup.md` y de los datos del RF AP-01 (`plantilla-ap01-registrar-mision.md`).

| Estado | Imagen |
|---|---|
| 1. Normal | `docs/mock-panel-normal-piplup.png` |
| 2. Alerta | `docs/mock-panel-alerta-piplup.png` |
| 3. Vacío | `docs/mock-panel-vacio-piplup.png` |

## Proceso seguido

1. **Estilo:** se tomaron la paleta, la tipografía y los colores de estado del manual de identidad (reto 08).
2. **Datos del RF:** del RF AP-01 se extrajo lo que el operador necesita para decidir por cada drone: ID (formato AR-XX), batería en %, estado y zona actual. También se usaron la regla RN-02 (batería ≥ 35%) y el mensaje de error del flujo alterno FA-01.
3. **Prompt con contexto completo:** se usó la plantilla del curso, llenada con la identidad y los datos de AquaPort.

## Prompt utilizado

```
Actúa como diseñador UX/UI senior de sistemas de monitoreo ambiental.

SISTEMA: AquaPort — Panel de control de flota de drones acuáticos ECI
PANTALLA: Panel de monitoreo de la flota (vista principal del operador)
ESTILO: Fondo oscuro tipo dashboard técnico hídrico (#0B1F2E), tarjetas #12304A,
  primario Azul laguna #1A7FA3, acento Cian sensor #1FB6C9, texto #E6F1F5 y
  secundario #9FB7C3. Tipografía Inter para la interfaz y JetBrains Mono para
  IDs de drones y códigos de misión. Título "AquaPort" grande y centrado en el
  encabezado, con ilustraciones hídricas simples (olas, gota, dron sobre el agua).
COLORES DE ESTADO: DISPONIBLE #2EB872, EN_MISION #4C8DF6, RECARGANDO #F5A524,
  MANTENIMIENTO #8A9AA6, FALLO #F0565B. El estado siempre se muestra con color,
  ícono y texto, nunca solo con color.
ACTOR: Operador Hídrico — necesita tomar decisiones rápidas sobre la flota
DATOS A MOSTRAR POR DRONE: ID (formato AR-XX), batería en %, estado
  (DISPONIBLE/EN_MISION/RECARGANDO/MANTENIMIENTO/FALLO), zona actual
FLOTA: 4 drones Aqua-Ranger 100 en zonas fijas del campus: Embalse Norte,
  Canal Central, Laguna Sur, Punto Ribereño Este y Laboratorio Hídrico.
ACCIONES DEL OPERADOR: Seleccionar drone, Asignar a misión, Ver detalle,
  Cancelar misión en estado PENDIENTE
REGLAS: los drones con batería < 35% muestran el botón "Asignar a misión"
  deshabilitado y el mensaje "El drone AR-03 tiene batería insuficiente (18%).
  Mínimo requerido: 35%."
ESTADOS DE LA PANTALLA:
  1. Normal: flota con drones en distintos estados
  2. Alerta: un drone en estado FALLO (destacado visualmente)
  3. Vacío: todos los drones en misión simultáneamente
Nielsen: visibilidad del estado (#1), control del usuario (#3), prevención de
  errores (#5), minimalismo (#8), mensajes de error claros (#9)
```

## Heurísticas de Nielsen que cumplen los mocks

| Heurística | Dónde se cumple |
|---|---|
| **#1 Visibilidad del estado del sistema** | Cada tarjeta muestra el estado con color, ícono y texto. El contador superior resume la flota (disponibles, en misión, recargando, mantenimiento, fallo), el mapa ubica cada drone con el color de su estado y el encabezado indica "En vivo · actualizado hace 5 s" |
| **#3 Control y libertad del usuario** | La misión M-001 en estado PENDIENTE tiene el botón "Cancelar misión" (estados normal y alerta). En el estado vacío, la misión M-004 pendiente también puede cancelarse |
| **#5 Prevención de errores** | AR-03 tiene 18% de batería, así que su botón "Asignar a misión" aparece deshabilitado antes de que el operador lo intente. En el estado alerta, AR-04 con FALLO no ofrece asignar, solo "Ver detalle del fallo" |
| **#8 Diseño estético y minimalista** | Cada tarjeta muestra solo ID, batería, estado, zona y una acción. Los datos secundarios, como el tipo de carga, aparecen solo en la lista de misiones |
| **#9 Ayudar a reconocer y recuperarse de errores** | El mensaje de AR-03 dice el dato exacto y el mínimo requerido. El banner de alerta explica qué pasó con AR-04, qué consecuencia tiene y qué hacer ("Ver detalle del fallo") |
| **#4 Consistencia y estándares** | Los mismos colores de estado se usan en las tarjetas, el contador, el mapa y la leyenda. Los IDs y códigos usan siempre la fuente monoespaciada |

## Qué muestra cada estado

| Estado | Contenido |
|---|---|
| **Normal** | AR-01 disponible y seleccionado (92%), AR-02 en misión con M-001 pendiente, AR-03 recargando con 18% y asignación bloqueada, AR-04 disponible (73%) |
| **Alerta** | AR-04 en FALLO, destacado con borde y brillo rojo, más un banner superior que explica el problema y ofrece la acción. El resto de la flota sigue visible y operativa |
| **Vacío** | Los 4 drones en misión. Un aviso explica que no hay drones disponibles y cuándo se podrá registrar una nueva misión, con la acción "Ver misiones en curso" |
