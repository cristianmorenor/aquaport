# Reto 13 · JaCoCo — Cobertura de la capa de dominio v2 (Prinplup)

## 1. Quality gate configurado en Maven

En `pom.xml`, el plugin `jacoco-maven-plugin` tiene una ejecución `quality-gate` con el goal `check` en la fase `verify`:

| Configuración | Valor | Por qué |
|---|---|---|
| `element` | `CLASS` | La regla se evalúa **clase por clase**. Ninguna clase puede esconder su falta de pruebas detrás del promedio del proyecto |
| `counter` | `LINE` | Se mide la cobertura de líneas, que es la que pide el reto |
| `minimum` | `0.80` | Si cualquier clase baja del 80%, `mvn verify` termina en **BUILD FAILURE** |
| `excludes` | `AquaPortApp`, `AquaPortV2App` | Son demostraciones por consola (`main`), no lógica de dominio |

El gate corre con `mvn clean verify`, el mismo comando que se usa para el análisis de SonarQube.

## 2. El gate funcionando (antes de agregar pruebas)

Al activar el gate, el build falló porque estas clases de Piplup no tenían pruebas:

Clases detectadas: `ConsultorFlota`, `RegistradorMisiones`, `NotificadorOperador` y `RepositorioMisionesEnMemoria`.

Se agregaron sus pruebas: `ConsultorFlotaTest`, `RegistradorMisionesTest` (con Mockito sobre `RepositorioMisiones`), `NotificadorOperadorTest` y `RepositorioMisionesEnMemoriaTest`.

## 3. Ramas amarillas (parcialmente cubiertas) y su prueba

| Clase y condición | Camino que faltaba | Prueba agregada | Por qué es importante probar esa rama |
|---|---|---|---|
| `DroneAcuatico`: `id == null \|\| id.isBlank()` | id **nulo** (solo se había probado en blanco) | `constructor_idNulo_lanzaExcepcion` | Un drone sin id no se puede buscar ni identificar en las alertas. Si la validación de `null` fallara, el error aparecería mucho después, como un `NullPointerException` al comparar ids |
| `DroneAcuatico`: `bateria < 0 \|\| bateria > 100` | batería **negativa** (solo se había probado > 100) | `constructor_bateriaNegativa_lanzaExcepcion` | Una batería negativa haría que la regla del 35% diera resultados absurdos. Cada extremo del rango es un caso distinto y se prueba por separado |
| `SolicitudMision`: `id == null \|\| id.isBlank()` | id **nulo** | `constructor_idNulo_lanzaExcepcion` | El id de la solicitud se convierte en el id de la misión. Sin él no se puede registrar ni notificar al centro de control |
| `FabricaDrones.registrarTipo`: `tipo == null \|\| tipo.isBlank()` | tipo **nulo** | `registrarTipo_tipoNulo_lanzaExcepcion` | Registrar un tipo `null` dejaría la fábrica en un estado inválido y el error saldría al crear drones, lejos de su causa |
| `AsignadorAutomatico`: `candidato != drone` | caso **falso**: el drone en fallo sí estaba entre los candidatos | `asignar_droneFallaDuranteAsignacion_excluyeloDeLosCandidatos` | Es la condición que **saca de la lista** al drone que falló durante la asignación. Si no funcionara, la estrategia podría volver a elegirlo y la búsqueda del alternativo no terminaría |

También se cubrieron líneas sin ejecutar del asignador:

- `cambiarEstrategia_nuevaEstrategia_seUsaEnLaAsignacion` comprueba que, al cambiar la estrategia, la anterior ya no se consulta (Strategy en tiempo de ejecución).
- `asignar_estrategiaEligeDrone_misionLlegaAZonaDestino` comprueba que la misión creada lleva como destino la zona de la solicitud.

## 4. Resultado final

| Evidencia | Archivo |
|---|---|
| Tabla general del reporte | `docs/jacoco-prinplup-general.png` |
| Línea antes amarilla, ahora verde | `docs/jacoco-prinplup-rama-verde.png` |

- `mvn clean verify` → **BUILD SUCCESS**, quality gate aprobado.
- Todas las clases de dominio y servicio con ≥ 80% de cobertura de líneas.
