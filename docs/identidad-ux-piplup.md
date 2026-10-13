# Manual de identidad y UX de AquaPort MVP (Piplup)

La identidad se define antes de diseñar cualquier pantalla, para que todas las vistas de AquaPort se vean como parte de la misma aplicación.

## Concepto

AquaPort es un sistema de monitoreo hídrico usado en contextos de investigación. La identidad debe transmitir **calma, precisión y entorno natural**: azules profundos del agua, cian técnico de los instrumentos de medición y un fondo oscuro tipo dashboard, que cansa menos la vista en jornadas largas de monitoreo.

## Paleta de colores

| Rol | Nombre | Hex | Uso |
|---|---|---|---|
| Primario | Azul laguna | `#1A7FA3` | Botones principales ("Asignar a misión"), encabezado, elementos de marca |
| Acento | Cian sensor | `#1FB6C9` | Enlaces, foco del teclado, drone seleccionado, gráficas de batería |
| Fondo | Profundidad | `#0B1F2E` | Fondo general del panel |
| Superficie | Agua nocturna | `#12304A` | Tarjetas de drone, paneles y tablas |
| Texto principal | Espuma | `#E6F1F5` | Títulos y datos importantes |
| Texto secundario | Neblina | `#9FB7C3` | Etiquetas, ayudas y datos de apoyo |

**Justificación:** el azul y el cian vienen del agua y de los instrumentos de medición, así que conectan visualmente con el dominio. El fondo oscuro permite que los colores de estado resalten, que es lo más importante para el operador. Los textos tienen contraste alto sobre el fondo: 14.6:1 el principal y 8.0:1 el secundario, por encima del mínimo de 4.5:1 de WCAG AA.

El primario Azul laguna se eligió para que funcione en los dos sentidos: el texto blanco de los botones se lee con 4.6:1 (cumple AA), y el botón se distingue del fondo oscuro con 3.7:1 (por encima del 3:1 que exige WCAG para componentes de interfaz). Un azul más oscuro haría que el botón principal se perdiera contra el fondo.

## Colores de estado del drone

| Estado | Color | Hex | Significado para el operador |
|---|---|---|---|
| DISPONIBLE | Verde monitoreo | `#2EB872` | Puede asignarse a una misión |
| EN_MISION | Azul tránsito | `#4C8DF6` | Está ejecutando una misión |
| RECARGANDO | Ámbar | `#F5A524` | No disponible temporalmente |
| MANTENIMIENTO | Gris | `#8A9AA6` | Fuera de servicio programado |
| FALLO | Rojo alerta | `#F0565B` | Requiere atención inmediata |

**Regla:** el estado **nunca se comunica solo con color**. Siempre va acompañado del texto del estado y de un ícono, para que lo entienda también una persona con daltonismo.

En el MVP el modelo solo distingue drone disponible o no disponible. Los cinco estados definen desde ya el lenguaje visual, para que la identidad no cambie cuando el modelo crezca en Prinplup.

## Tipografía

| Uso | Fuente | Por qué |
|---|---|---|
| Interfaz (títulos, textos, botones) | **Inter** | Sans-serif moderna, muy legible en pantallas de monitoreo y gratuita (Google Fonts) |
| IDs de drones y códigos de misión (`AR-01`, `M-001`) | **JetBrains Mono** | Monoespaciada: cada carácter ocupa lo mismo, así los IDs quedan alineados en tablas y no se confunden caracteres como `0` y `O` |

Escala: títulos de 24 px, subtítulos de 18 px, texto base de 14 px y etiquetas de 12 px.

## Tono de voz

Técnico, preciso y ambiental. Los mensajes son directos, dicen qué pasó, con qué dato y qué hacer.

| Situación | Sí | No |
|---|---|---|
| Error de batería | "El drone AR-03 tiene batería insuficiente (18%). Mínimo requerido: 35%." | "Error de asignación" |
| Confirmación | "Misión M-001 registrada. Drone AR-01 asignado." | "¡Listo! 🎉" |
| Flota sin drones disponibles | "No hay drones disponibles. 4 de 4 están en misión." | "Ups, algo salió mal" |

## Heurísticas de Nielsen aplicadas al panel de monitoreo

| Heurística | Cómo se aplica en el panel |
|---|---|
| **#1 Visibilidad del estado del sistema** | Cada tarjeta de drone muestra su estado con color, ícono y texto, sin clics adicionales |
| **#3 Control y libertad del usuario** | Las misiones en estado PENDIENTE tienen un botón "Cancelar misión" visible |
| **#5 Prevención de errores** | Los drones con batería menor a 35% aparecen deshabilitados y su botón "Asignar" no se puede presionar, en lugar de mostrar un error después |
| **#8 Diseño estético y minimalista** | Cada tarjeta muestra solo lo necesario para decidir: ID, batería, estado y zona actual |
| **#9 Ayudar a reconocer y recuperarse de errores** | Los mensajes de error dicen el dato exacto y el mínimo requerido, como en el ejemplo del tono de voz |
