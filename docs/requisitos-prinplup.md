# Requisitos de AquaPort v2 (Prinplup)

Sistema: AquaPort v2 — 15 drones de 3 tipos (superficial, semisumergido, buceador), asignación automática según zona, carga y condiciones del agua, y alertas automáticas al centro de control.

## Requisitos funcionales

| Código | Funcionalidad | Actor | Acción | Resultado observable |
|---|---|---|---|---|
| AP-07 | Asignación automática | Sistema (a partir de la solicitud del Operador Hídrico) | Recibe una solicitud de transporte y asigna automáticamente el drone disponible de mayor batería que pueda llevar la carga. | La misión queda registrada en estado PENDIENTE con el drone asignado, y ese drone pasa a EN_MISION. |
| AP-08 | Asignación automática (prioridad) | Sistema | Ante una misión CRÍTICA, asigna el drone técnicamente más apto para la zona de destino, independientemente de la batería. | La misión crítica queda asignada al drone ubicado en la zona de destino, si existe uno operativo. |
| AP-09 | Alertas de fallo | Sistema → Centro de Control y Técnico de Mantenimiento | Notifica cuando un drone en FALLO interrumpe una asignación o cuando una misión CRÍTICA no tiene drone disponible. | El centro de control y el técnico reciben una alerta con el id de la misión, su prioridad y el motivo. |
| AP-10 | Consulta de condiciones hídricas | Sistema → API Condiciones Hídricas | Consulta el nivel de agitación y la profundidad del cuerpo de agua de destino antes de filtrar los drones. | Solo quedan como candidatos los tipos de drone aptos para esas condiciones; si las condiciones son adversas para todos, la solicitud se rechaza con el motivo. |
| AP-11 | Gestión por técnico | Técnico de Mantenimiento | Cambia el estado de un drone (por ejemplo, de FALLO a MANTENIMIENTO o a DISPONIBLE) después de revisarlo. | El drone aparece con su nuevo estado en el panel de la flota y vuelve a estar disponible para asignación si quedó DISPONIBLE. |

AP-07 y AP-08 juntos componen la funcionalidad de **asignación automática**.

## Requisitos no funcionales

| Código | Categoría | Requisito | Cómo se mide |
|---|---|---|---|
| RNF-04 | Rendimiento | El algoritmo de asignación automática selecciona el drone óptimo en menos de 400 ms para una flota de hasta 30 drones. | Prueba de JUnit 5 con `assertTimeout(Duration.ofMillis(400), ...)` sobre `AsignadorAutomatico.asignar` con 30 drones. |
| RNF-05 | Confiabilidad | El 100% de las misiones CRÍTICAS sin drone disponible genera exactamente una alerta a cada observador registrado. | Prueba con Mockito: `verify(observador, times(1)).notificarFalloAsignacion(...)` por cada observador. |
| RNF-06 | Mantenibilidad | Agregar un nuevo tipo de drone requiere crear 1 clase nueva y modificar como máximo 1 línea de configuración, sin cambiar `AsignadorAutomatico`, `ValidadorMision` ni `FabricaDrones`. | Revisión del Pull Request que agregue el tipo: archivos modificados ≤ 1, más la clase nueva. |
| RNF-07 | Calidad | Cada clase del dominio y de servicios mantiene ≥ 80% de cobertura de líneas, y el análisis estático reporta 0 bugs, 0 vulnerabilidades, deuda técnica < 30 min y duplicación < 5%. | Quality gate de JaCoCo en `mvn verify` (falla el build si no se cumple) y dashboard de SonarQube. |

## Priorización MoSCoW

| Requisito | Categoría | Justificación |
|---|---|---|
| AP-07 Asignación automática | **Must Have** | Es el cambio central de la v2: el sistema decide y el operador solo supervisa. |
| AP-08 Prioridad crítica | **Must Have** | Una muestra crítica que llega tarde o con un drone inadecuado invalida el análisis hídrico. |
| AP-09 Alertas de fallo | **Must Have** | Sin alertas, un drone en fallo o una misión crítica sin atender pasan desapercibidos para el centro de control. |
| AP-10 Condiciones hídricas | **Should Have** | Mejora la elección del tipo de drone, pero la v2 puede operar con las reglas de capacidad mientras se integra la API externa. |
| AP-11 Gestión por técnico | **Should Have** | Es necesaria para recuperar drones, pero el cambio de estado puede hacerse temporalmente por el administrador. |
| RNF-04 Rendimiento < 400 ms | **Should Have** | Con 15 drones el riesgo es bajo, pero la métrica protege el crecimiento a 30 drones. |
| RNF-05 Alertas confiables | **Must Have** | Una alerta perdida o duplicada genera decisiones equivocadas en el centro de control. |
| RNF-06 Mantenibilidad OCP | **Should Have** | La flota crecerá en Empoleon; sin esta propiedad cada tipo nuevo arriesga la lógica de asignación. |
| RNF-07 Calidad | **Must Have** | Es parte de la Definition of Done del sprint. |

## Tensión entre AP-07 y AP-08

**Qué la causa.** AP-07 pide el drone de **mayor batería** y AP-08 pide, para misiones CRÍTICAS, el drone **más apto para la zona**, sin importar la batería. No son contradictorios, pero ante la misma flota pueden elegir drones distintos.

**Ejemplo con la flota v2.** Una misión con destino Laguna Sur:
- AP-07 elige **AR-01** (superficial, 92%, en Embalse Norte), que tiene la mayor batería pero está lejos.
- AP-08 elige **AR-12** (buceador, 76%, en Laguna Sur), que está en la zona de destino.

**Cómo se resuelve.**

1. **Las reglas duras se aplican antes que cualquier criterio.** Ninguna regla de selección puede saltarse la batería mínima de 35%, la capacidad de carga del tipo ni el estado operativo. El `AsignadorAutomatico` filtra los candidatos con esas reglas antes de consultar la estrategia, y vuelve a validar el drone elegido.
2. **El criterio depende de la prioridad.** Cada criterio es una estrategia distinta (patrón Strategy):
   - Misiones NORMAL, ALTA y BAJA → `MayorBateriaStrategy` (AP-07).
   - Misiones CRÍTICAS → `ZonaCercanaStrategy` (AP-08): prefiere drones en la zona de destino y desempata por batería.
3. **La precedencia queda explícita:** para una misión CRÍTICA, AP-08 prevalece sobre AP-07.

**Implementación.** El asignador permite cambiar la estrategia activa con `cambiarEstrategia(...)`, sin modificar su código. Elegir la estrategia automáticamente según la prioridad de cada solicitud queda como trabajo de la siguiente iteración.
