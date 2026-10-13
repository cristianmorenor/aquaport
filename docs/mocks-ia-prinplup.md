# Mocks con IA: flujo de asignación automática (Prinplup)

Los mocks se generaron con IA a partir del sistema de diseño v2 (`identidad-ux-prinplup.md`) y de los datos del RF AP-07 (`plantilla-ap07-asignar-automaticamente.md`).

| Pantalla | Imagen |
|---|---|
| 1. Panel de flota | `docs/mock-v2-pantalla1-panel-flota-prinplup.png` |
| 2. Detalle de misión | `docs/mock-v2-pantalla2-detalle-mision-prinplup.png` |
| 3. Confirmación | `docs/mock-v2-pantalla3-confirmacion-prinplup.png` |
| Alerta: misión CRÍTICA sin drone | `docs/mock-v2-alerta-critica-sin-drone-prinplup.png` |
| Componentes | `docs/mock-v2-componentes-prinplup.png` |

## Proceso seguido

1. **Identidad:** paleta, tipografía, colores de los 6 estados y componentes del reto 08.
2. **Datos del RF AP-07:** solicitud con zona, tipo de carga, peso y prioridad; filtros de batería ≥ 35% (RN-01) y capacidad por tipo (RN-02); estrategia según la prioridad; notificación a los observadores; flujo alterno FA-01 sin drones disponibles.
3. **Un prompt por pantalla**, con el mismo bloque de estilo para que las 4 se vean como la misma aplicación.

Bloque de estilo común (se pegó al inicio de los 4 prompts):

```
SISTEMA: AquaPort v2 — asignación automática de drones acuáticos ECI
ESTILO: dashboard oscuro hídrico. Fondo #0B1F2E, tarjetas #12304A, primario
  #1A7FA3, acento #1FB6C9, texto #E6F1F5 y secundario #9FB7C3. Inter para la
  interfaz y JetBrains Mono para IDs (AR-XX) y misiones (M-XXX). Logo de gota y
  ondas suaves en el borde inferior.
ESTADOS DEL DRONE (color + ícono + texto): DISPONIBLE #2EB872, EN_MISION
  #4C8DF6, RECARGANDO #F5A524, MANTENIMIENTO #8A9AA6, FALLO #F0565B,
  SUMERGIDO #7C8CFF.
BATERÍA: barra + porcentaje. ≥60% verde, 35–59% amarillo, <35% rojo con ícono
  de advertencia.
FIJO EN TODAS LAS PANTALLAS: botón rojo grande "DETENER ASIGNACIONES" en la
  esquina inferior derecha (Ley de Fitts) y un indicador de pasos
  1 Panel de flota → 2 Detalle de misión → 3 Confirmación.
```

## Prompt 1: Panel de flota

```
Actúa como diseñador UX/UI senior de sistemas de monitoreo ambiental.
[bloque de estilo común]
PANTALLA: Panel de flota (paso 1 activo).
ACTOR: Operador Hídrico.
CONTENIDO:
- Banner "Nueva solicitud": M-050 · Muestra de agua · 200 g → Laguna Sur,
  etiqueta CRÍTICA en rojo y botón primario "Asignar automáticamente".
- 15 drones agrupados en 3 columnas por tipo (Ley de Hick):
  Superficial (6 drones, hasta 500 g), Semisumergido (5, hasta 1500 g) y
  Buceador (4, hasta 300 g). Cada grupo muestra cuántos están operativos.
- Cada tarjeta: ID, zona, batería y estado. Incluir al menos un drone en cada
  uno de los 6 estados, un drone con 18% (AR-03) y otro con 33% (AR-14) con la
  advertencia roja.
- Zonas: Embalse Norte, Canal Central, Laguna Sur, Punto Ribereño Este,
  Laboratorio Hídrico.
Nielsen: visibilidad del estado (#1), consistencia (#4), prevención de
  errores (#5), minimalismo (#8).
```

## Prompt 2: Detalle de misión

```
Actúa como diseñador UX/UI senior de sistemas de monitoreo ambiental.
[bloque de estilo común]
PANTALLA: Detalle de misión (paso 1 completado, paso 2 activo).
DOS COLUMNAS:
- Izquierda, la misión: M-050, CRÍTICA, carga Muestra de agua 200 g, destino
  Laguna Sur, condiciones Agitación baja · 2 m de profundidad, estrategia
  activa ZonaCercanaStrategy, candidatos aptos 9 de 15.
- Derecha, con borde cian, "Drone recomendado por el sistema": AR-12, Buceador
  Aqua-Ranger 300, capacidad 300 g, DISPONIBLE, batería 76%, zona Laguna Sur.
- Bloque "Razón de la selección": la misión es CRÍTICA, así que se usa
  ZonaCercana; AR-12 ya está en Laguna Sur, tiene la mayor batería de la zona
  y puede llevar 200 g; AR-14 se descartó por batería crítica (33%) y AR-11
  está sumergido.
- Acciones: "Confirmar asignación" (primario, grande) y "Elegir otro drone"
  (secundario).
Nielsen: visibilidad del estado (#1), control y libertad (#3), reconocer en
  lugar de recordar (#6).
```

## Prompt 3: Confirmación

```
Actúa como diseñador UX/UI senior de sistemas de monitoreo ambiental.
[bloque de estilo común]
PANTALLA: Confirmación (pasos 1 y 2 completados, paso 3 activo).
DOS COLUMNAS:
- Izquierda: check verde grande, título "Misión asignada" y el texto
  "Misión M-050 registrada. Drone AR-12 asignado." Datos: estado PENDIENTE,
  ruta Laguna Sur → Laboratorio Hídrico, notificado a Centro de Control ECI.
  Botones "Ver misión" y "Nueva solicitud".
- Derecha: "Estado actualizado de la flota en Laguna Sur" con AR-12 ahora
  EN MISIÓN y resaltado, AR-08 DISPONIBLE 57%, AR-14 DISPONIBLE 33% con
  advertencia y AR-11 SUMERGIDO. Debajo, 3 cifras: 8 operativos, 2 en misión,
  3 requieren atención.
Nielsen: visibilidad del estado (#1), control y libertad (#3), minimalismo (#8).
```

## Prompt 4: Alerta de misión CRÍTICA sin drone disponible

```
Actúa como diseñador UX/UI senior de sistemas de monitoreo ambiental.
[bloque de estilo común]
PANTALLA: Panel de flota en estado de alerta (paso 1 activo).
- Mismo panel de 15 drones de la pantalla 1, pero AR-08 ahora está EN MISIÓN.
- En lugar de la solicitud, un banner rojo de ancho completo con ícono de
  alerta: "Misión CRÍTICA M-050 sin drone disponible". Texto: destino Laguna
  Sur, equipo de medición de 900 g; solo un semisumergido puede llevar esa
  carga y en la zona no hay ninguno operativo: AR-08 está en misión y AR-11
  está sumergido. Se notificó al Centro de Control y al técnico.
- Botón rojo "Ver alternativas" dentro del banner.
- AR-15 en FALLO con borde rojo.
Nielsen: visibilidad del estado (#1), reconocer, diagnosticar y recuperarse
  de errores (#9), prevención de errores (#5).
```

## Heurísticas de Nielsen que cumplen los mocks

| Heurística | Dónde se cumple |
|---|---|
| **#1 Visibilidad del estado del sistema** | Las tarjetas muestran estado con color, ícono y texto; el encabezado indica la estrategia activa, el número de drones y la hora; el indicador de pasos dice en qué parte del flujo está el operador |
| **#3 Control y libertad del usuario** | En la pantalla 2 el operador puede "Elegir otro drone" en lugar de aceptar la recomendación. "DETENER ASIGNACIONES" permite frenar el proceso automático en cualquier momento |
| **#4 Consistencia y estándares** | Los mismos colores, íconos y tarjetas se usan en las 4 pantallas; los IDs siempre van en fuente monoespaciada |
| **#5 Prevención de errores** | Los drones con batería < 35% muestran advertencia roja y no se recomiendan; la recomendación descarta drones sumergidos o en FALLO antes de mostrarse |
| **#6 Reconocer en lugar de recordar** | La pantalla 2 repite los datos de la misión y explica la razón de la selección, así el operador no tiene que volver al panel para comparar |
| **#8 Diseño estético y minimalista** | Cada tarjeta muestra solo 4 datos; la confirmación resume la flota en 3 cifras |
| **#9 Ayudar a reconocer, diagnosticar y recuperarse de errores** | El banner de alerta dice qué pasó, por qué (qué drones no sirven y por qué razón), a quién se notificó y ofrece "Ver alternativas" |
