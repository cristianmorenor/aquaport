# Reto 14 · Análisis estático de la v2 con SonarQube (Prinplup)

## Metas del reto

| Indicador | Meta | Resultado |
|---|---|---|
| Bugs (Reliability) | 0 | (completar) |
| Vulnerabilidades (Security) | 0 | (completar) |
| Deuda técnica (Maintainability) | < 30 min | (completar) |
| Duplicación | < 5% | (completar) |

Captura del dashboard: `docs/sonarqube-dashboard-prinplup.png`

## Cómo se ejecutó

1. `docker start sonarqube` y esperar `SonarQube is operational` en `docker logs -f sonarqube`.
2. En la rama `release/v2.0`, ejecutar el mismo comando de Piplup (el que muestra SonarQube con tu token):

```bash
mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar \
  -Dsonar.projectKey=aquaport \
  -Dsonar.projectName='AquaPort' \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=TU_TOKEN
```

## Issues resueltos

Para cada issue, el commit de la corrección documenta: causa raíz, corrección aplicada y por qué esa corrección es la adecuada.

| Issue | Causa raíz | Corrección | Por qué es la adecuada | Commit |
|---|---|---|---|---|
| (completar si SonarQube reporta alguno) | | | | |

Si el análisis no reporta issues, se deja constancia aquí: las prácticas aplicadas durante el desarrollo los evitaron (constantes en vez de números sueltos, `Optional` en vez de `null`, `flatMap` en vez de `Optional.get()`, salidas inyectadas en vez de `System.out` en las clases de negocio y pruebas con un solo propósito).
