# Sistema de diseño y leyes UX de AquaPort v2 (Prinplup)

La v2 mantiene la identidad definida en Piplup (`identidad-ux-piplup.md`): misma paleta, tipografía Inter + JetBrains Mono y tono de voz. Lo nuevo es el sistema de componentes con sus estados y la aplicación de las leyes de Fitts, Hick y Miller al flujo de asignación automática.

Imagen de los componentes: `docs/mock-v2-componentes-prinplup.png`

## Componente 1: Tarjeta de drone acuático

| Estado | Color | Hex | Ícono | Cuándo aparece |
|---|---|---|---|---|
| DISPONIBLE | Verde monitoreo | `#2EB872` | Check | Operativo y libre para una misión |
| EN_MISION | Azul tránsito | `#4C8DF6` | Flecha de navegación | Ejecutando una misión |
| RECARGANDO | Ámbar | `#F5A524` | Rayo | Cargando batería en la base |
| MANTENIMIENTO | Gris | `#8A9AA6` | Reloj | Fuera de servicio programado |
| FALLO | Rojo alerta | `#F0565B` | Triángulo de alerta | Requiere atención del técnico; la tarjeta además lleva borde y brillo rojo |
| SUMERGIDO | Azul índigo | `#7C8CFF` | Ondas | Drone bajo el agua, sin comunicación directa |

SUMERGIDO es el estado nuevo de la v2. Se eligió un azul índigo en lugar de un azul oscuro porque un azul oscuro se pierde sobre el fondo `#0B1F2E`; el índigo se distingue del azul de EN_MISION y mantiene la relación con el agua profunda.

Cada tarjeta muestra solo ID, estado, batería y zona. El estado siempre va con color, ícono y texto.

## Componente 2: Botón "Asignar misión"

| Estado | Aspecto | Significado |
|---|---|---|
| Default | Fondo Azul laguna `#1A7FA3`, texto blanco | Acción disponible |
| Hover | Azul más claro `#2290B8` con anillo cian | Indica que se puede presionar |
| Procesando | Spinner + "Asignando…" | El sistema está trabajando; evita doble clic |
| Éxito | Fondo verde `#2EB872` + check + "Asignada" | Confirma el resultado de la acción |
| Deshabilitado | Gris translúcido, texto atenuado | El drone no cumple las reglas (batería < 35%, estado distinto de DISPONIBLE) |

## Componente 3: Indicador de batería

| Rango | Color | Detalle |
|---|---|---|
| ≥ 60% | Verde `#2EB872` | Batería suficiente |
| 35–59% | Amarillo `#F5A524` | Operativo, pero conviene vigilarlo |
| < 35% | Rojo `#F0565B` + ícono de advertencia | Por debajo del mínimo de RN-01; el drone no se asigna |

El porcentaje se muestra siempre en número, además de la barra, para no depender solo del color.

## Ley de Fitts: botón de emergencia

> El tiempo para alcanzar un objetivo depende de su distancia y de su tamaño.

- El botón **"DETENER ASIGNACIONES"** mide 270 × 64 px, mucho más que los botones normales (40 px de alto).
- Está **fijo en la esquina inferior derecha** de las 3 pantallas. Las esquinas son fáciles de alcanzar con el mouse y el botón no cambia de lugar, así que el operador no lo tiene que buscar.
- Usa el rojo de alerta y un ícono de triángulo, y está separado del resto de acciones para no presionarlo por error.

## Ley de Hick: agrupación de los 15 drones

> El tiempo para decidir aumenta con el número de opciones.

- Los 15 drones no se muestran como una lista plana: se agrupan en **3 columnas por tipo** (Superficial, Semisumergido y Buceador).
- Cada grupo indica su capacidad máxima (500 g, 1500 g y 300 g) y cuántos drones operativos tiene. El operador primero descarta el tipo que no sirve para la carga y luego elige entre 4 a 6 drones, no entre 15.
- En la pantalla 2 el sistema ya recomienda un drone, así que la decisión se reduce a confirmar o elegir otro.

## Ley de Miller: cantidad de información por bloque

- Cada grupo tiene como máximo 6 tarjetas, dentro del rango de 7 ± 2 elementos.
- Cada tarjeta muestra 4 datos (ID, zona, batería y estado).
- El resumen de la confirmación usa 3 cifras: operativos, en misión y que requieren atención.

## Flujo de asignación automática (3 pantallas)

| Paso | Pantalla | Imagen | Qué hace el operador |
|---|---|---|---|
| 1 | Panel de flota | `docs/mock-v2-pantalla1-panel-flota-prinplup.png` | Ve la solicitud M-050 CRÍTICA y los 15 drones agrupados; presiona "Asignar automáticamente" |
| 2 | Detalle de misión | `docs/mock-v2-pantalla2-detalle-mision-prinplup.png` | Revisa el drone recomendado (AR-12) y la razón de la selección; confirma o elige otro |
| 3 | Confirmación | `docs/mock-v2-pantalla3-confirmacion-prinplup.png` | Ve la misión registrada y el estado actualizado de la flota en la zona |
| Alerta | Misión CRÍTICA sin drone | `docs/mock-v2-alerta-critica-sin-drone-prinplup.png` | Recibe el aviso de que ningún drone puede cumplir la misión y la acción "Ver alternativas" |

Un indicador de pasos (1 → 2 → 3) en la parte superior muestra en qué parte del flujo está el operador.
