# Reto 09 · Sprint de la v2 en Jira (Prinplup)

## Sprint

| Campo | Valor |
|---|---|
| Nombre | AP Sprint 2 — AquaPort v2 |
| Duración | 2 semanas |
| Sprint Goal | **Completar el asignador automático con Strategy y las alertas con Observer** |

## Historias de usuario del sprint

Todas van bajo la épica AP-1. Se agrupan en una feature nueva: **"Asignación automática y alertas de la flota v2"**.

| HU | Historia | Story Points | Por qué esa estimación |
|---|---|---|---|
| HU-04 | Como operador hídrico, quiero que el sistema asigne automáticamente el drone de mayor batería apto para la carga, para no tener que elegirlo manualmente en cada misión. | **8** | Es la pieza más compleja: filtro de aptos, Strategy y creación de la misión, con pruebas Mockito |
| HU-05 | Como operador hídrico, quiero que las misiones CRÍTICAS se asignen al drone más apto para la zona de destino, para que las muestras urgentes lleguen con el drone adecuado. | **3** | Reutiliza el asignador; solo agrega `ZonaCercanaStrategy` |
| HU-06 | Como centro de control, quiero recibir una alerta cuando una misión CRÍTICA no tenga drone o un drone falle durante la asignación, para reaccionar a tiempo. | **5** | Observer, búsqueda del drone alternativo y prueba de "notificados exactamente una vez" |
| HU-07 | Como técnico de mantenimiento, quiero ser notificado de los drones en fallo, para revisarlos y devolverlos a servicio. | **2** | Un observador más sobre la interfaz existente |
| HU-08 | Como administrador ECI, quiero registrar drones de los 3 tipos con su capacidad de carga, para que la flota refleje los equipos reales. | **5** | Jerarquía de drones y `FabricaDrones` sin `switch` |

**Total del sprint: 23 story points** (Fibonacci: 1, 2, 3, 5, 8).

## Subtareas sugeridas para HU-04

1. Implementar `SolicitudMision` con validación de peso y prioridad (TDD).
2. Implementar `EstrategiaSeleccion` y `MayorBateriaStrategy` (TDD).
3. Implementar `AsignadorAutomatico` con pruebas Mockito (TDD).

## Definition of Done del sprint

- [ ] El código compila sin warnings.
- [ ] Las pruebas unitarias pasan (JUnit 5).
- [ ] JaCoCo reporta ≥ 80% de line coverage para la feature (quality gate en `mvn verify`).
- [ ] El PR tiene revisión aprobada por al menos otro miembro (o por el revisor de código DOSW).
- [ ] El diagrama C4 y la plantilla DOSW están actualizados.

## Cómo cargarlo en Jira

1. En el espacio AquaPort, abre **Backlog**.
2. Crea la feature: **+ Crear** → tipo **Feature** → "Asignación automática y alertas de la flota v2" → **Principal: AP-1**.
3. Crea las 5 historias (tipo **Story**) desde la épica AP-1, en **Actividades secundarias**, igual que en Piplup.
4. En cada historia, llena el campo **Story point estimate** con el valor de la tabla.
5. Vincula las 5 historias a la feature nueva (**Actividades vinculadas** → "está relacionado con").
6. En **Backlog**, arrastra las 5 historias al sprint **AP Sprint 1** (o crea uno con **Crear sprint**).
7. En el sprint, haz clic en **Iniciar sprint**, pega el **Sprint Goal** y elige 2 semanas.
8. Pega el **DoD** en la descripción de la feature.
9. Toma una captura del **Tablero** con el sprint activo y guárdala como `docs/jira-sprint-prinplup.png`.
