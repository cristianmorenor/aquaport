# Requisitos de AquaPort MVP (Piplup)

Sistema: AquaPort MVP — 4 drones Aqua-Ranger 100, zonas fijas del campus, asignación manual por el operador.

Zonas del MVP: Embalse Norte, Canal Central, Laguna Sur, Punto Ribereño Este y Laboratorio Hídrico.

## Requisitos funcionales

| Código | Actor | Acción | Resultado observable |
|---|---|---|---|
| AP-01 | Operador Hídrico | Registra una misión de transporte indicando punto de partida, punto de llegada, tipo de carga y el drone que asigna manualmente. | El sistema genera el código de la misión y la muestra en estado PENDIENTE con el drone asignado. |
| AP-02 | Operador Hídrico | Consulta la flota de drones disponibles. | El sistema muestra los drones disponibles con batería ≥ 35%, con su ID, batería y zona, ordenados de mayor a menor batería. |
| AP-03 | Operador Hídrico | Cancela una misión que está en estado PENDIENTE. | La misión deja de estar pendiente y el drone asignado vuelve a aparecer como disponible en la consulta de la flota. |

## Requisitos no funcionales

| Código | Categoría | Requisito | Cómo se mide |
|---|---|---|---|
| RNF-01 | Rendimiento | La consulta de drones disponibles debe responder en menos de 200 ms con una flota de hasta 10 drones. | Prueba de JUnit 5 con `assertTimeout(Duration.ofMillis(200), ...)`. |
| RNF-02 | Usabilidad | El 100% de los mensajes de error de validación debe estar en español e indicar el dato que falló y su valor. Ejemplo: "El drone AR-03 tiene batería insuficiente (18%). Mínimo requerido: 35%." | Pruebas unitarias que verifican el texto de la excepción con `assertEquals`. |
| RNF-03 | Mantenibilidad | Cada merge a `develop` debe tener ≥ 80% de cobertura de líneas en `ValidadorMision` y 0 bugs y 0 vulnerabilidades en SonarQube. | Reporte de JaCoCo y dashboard de SonarQube antes de aprobar el Pull Request. |

## Priorización MoSCoW

| Requisito | Categoría | Justificación |
|---|---|---|
| AP-01 Registrar misión | **Must Have** | Es el objetivo del MVP: sin registrar misiones no hay transporte de muestras. |
| AP-02 Consultar flota disponible | **Must Have** | El operador no puede asignar un drone manualmente si no sabe cuáles están disponibles y con batería suficiente. |
| AP-03 Cancelar misión pendiente | **Should Have** | Es importante para corregir una asignación errónea, pero el MVP puede operar sin ella si el operador revisa bien antes de registrar. |
| RNF-01 Rendimiento < 200 ms | **Should Have** | Con solo 4 drones el riesgo de lentitud es bajo, pero la métrica protege la experiencia del operador cuando la flota crezca. |
| RNF-02 Mensajes de error claros | **Must Have** | Si el operador no entiende por qué falló una asignación, no puede corregirla y la misión no sale. |
| RNF-03 Calidad del código | **Must Have** | Es la condición de la Definition of Done del curso para integrar cualquier cambio a `develop`. |

## Fuera del alcance del MVP

| Funcionalidad | Categoría | Justificación |
|---|---|---|
| Filtrar drones por zona | Could Have | Con 4 drones el operador los ve todos de un vistazo. |
| Exportar reporte de misiones | Could Have | Aporta valor administrativo, no operativo. |
| Asignación automática de drones | Won't Have | Corresponde a la versión Prinplup (flota autónoma). |
| Rutas multi-etapa con waypoints | Won't Have | Corresponde a la versión Empoleon. |
