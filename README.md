# AquaPort 🌊

Sistema de gestión de drones acuáticos de la Escuela Colombiana de Ingeniería.
Curso DOSW · 2026-2 · Nivel actual: **Piplup (MVP)**

## Descripción

AquaPort gestiona el transporte de muestras de agua y sensores entre las zonas
hídricas del campus usando una flota de 4 drones acuáticos (Aqua-Ranger 100).
Un operador asigna manualmente cada drone a una misión.

## Requisitos

- Java 17 o superior
- Maven 3.8 o superior

## Cómo compilar y probar

```bash
mvn clean test
```

El reporte de cobertura de JaCoCo queda en `target/site/jacoco/index.html`.

## Estructura del proyecto

```
src/main/java/edu/eci/dosw/aquaport/   código de producción
src/test/java/edu/eci/dosw/aquaport/   pruebas (JUnit 5)
docs/                                  diagramas (C4, casos de uso)
```

## Flujo de trabajo (GitFlow)

| Rama | Propósito |
|---|---|
| `main` | Código estable, aprobado |
| `develop` | Integración del trabajo en curso |
| `feature/<nombre>` | Una rama por reto, se integra a `develop` por Pull Request |

Los commits siguen el formato `tipo: descripción en presente`, por ejemplo
`feat: implementar ConsultorFlota con 5 consultas Stream`.
