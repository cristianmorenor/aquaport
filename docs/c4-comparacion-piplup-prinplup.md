# Reto 05 · Diagrama de contexto C4: MVP vs. v2

| Versión | Diagrama |
|---|---|
| Piplup (MVP) | `docs/c4-contexto-piplup.png` |
| Prinplup (v2) | `docs/c4-contexto-prinplup.png` |

## Qué creció

| Elemento | MVP | v2 |
|---|---|---|
| Actores (personas) | 3: Solicitante, Operador Hídrico, Administrador ECI | **4**: se agrega el **Técnico de Mantenimiento** |
| Sistemas externos | Ninguno | **3**: API Condiciones Hídricas, Centro de Control ECI y Sistema de Alertas |
| Responsabilidad del sistema | Registrar misiones con drone asignado a mano | **Decidir** el drone según zona, carga y condiciones del agua, y **avisar** cuando hay fallos |
| Flujos etiquetados | 6 | **13** |

## Flujos nuevos con sistemas externos

| Flecha | Datos que fluyen | Dirección |
|---|---|---|
| AquaPort → API Condiciones Hídricas | Consulta de condiciones de la zona destino | Salida |
| API Condiciones Hídricas → AquaPort | Agitación, profundidad y temperatura | Entrada |
| AquaPort → Centro de Control ECI | Registro de misión y alerta de misión crítica | Salida |
| Centro de Control ECI → AquaPort | Autorización de ruta acuática | Entrada |
| AquaPort → Sistema de Alertas | Notificación de drone en FALLO | Salida |

## Qué se mantuvo

- AquaPort sigue siendo **una sola caja**: el diagrama de contexto no muestra clases, paquetes ni patrones (Strategy y Observer viven *dentro* de la caja).
- Los 3 actores del MVP siguen presentes y con el mismo objetivo. Cambia lo que intercambian: el Operador Hídrico ya no elige el drone, sino que supervisa la asignación automática.
- Las zonas hídricas del campus son las mismas 5 del MVP.

## Lectura

El cambio más grande no está en las personas, sino en que AquaPort **deja de ser un sistema aislado**: depende de datos externos (condiciones del agua) para decidir, y produce información que otros sistemas consumen (centro de control y alertas). Por eso aparece Observer en el diseño: el sistema avisa sin conocer los detalles de quién recibe la alerta.
