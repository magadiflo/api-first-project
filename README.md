# 📖 API First

---

## ¿Qué es API First?

`API First` es una `estrategia de desarrollo` donde la API se diseña, documenta y acuerda entre
todos los involucrados antes de escribir una sola línea de código de implementación. La idea es que la API sea el
contrato central entre equipos (backend, frontend, QA, negocio).

Primero defines **qué va a hacer tu API** (sus endpoints, sus modelos, sus errores, sus respuestas), lo escribes
en un contrato formal, todos lo aprueban, y recién ahí comienzas a programar.

> 💡 **Piénsalo así:** es como planos arquitectónicos. El arquitecto no empieza a construir el edificio sin antes tener
> los planos aprobados por el cliente, el municipio y los ingenieros. API First es exactamente eso, pero para software.

### 🔗 ¿Cómo se relacionan API First, OpenAPI y Swagger?

Son tres cosas distintas que trabajan juntas. Aquí la jerarquía:

````
API First                   →   Estrategia / filosofía de trabajo
    └── OpenAPI             →   Especificación / estándar (el "lenguaje" del contrato)
          └── Swagger UI    →   Herramienta visual que lee esa especificación
````

- `API First`: Es la `metodología`. Significa que primero escribes el archivo YAML de OpenAPI, lo validas con los
  interesados (negocio, frontend, arquitectura) y, una vez aprobado, generas el código base (interfaces y DTOs)
  a partir de ese archivo.
- `OpenAPI`: Es el `estándar` (el lenguaje). Es un archivo YAML o JSON que describe qué endpoints existen,
  qué reciben y qué devuelven.
- `Swagger`: Es el conjunto de `herramientas`. `Swagger UI` sirve para visualizar la documentación y
  `Swagger Codegen` para generar código a partir del YAML.

| Concepto                        | Tipo              | ¿Qué es?                                                                  |
|---------------------------------|-------------------|---------------------------------------------------------------------------|
| **API First**                   | 🧠 Estrategia     | Forma de trabajar: primero el contrato, luego el código                   |
| **OpenAPI**                     | 📄 Especificación | Estándar (YAML/JSON) para describir APIs REST. Antes llamado Swagger Spec |
| **Swagger UI**                  | 🖥️ Herramienta   | Interfaz gráfica que renderiza una spec OpenAPI                           |
| **Springdoc / Swagger Codegen** | 🔧 Herramienta    | Genera código o documentación a partir de la spec                         |

> ✅ `Swagger UI` es la `implementación gráfica`, `OpenAPI` es la `especificación`, y `API First` es la estrategia que
> determina cuándo y cómo usas todo eso.

#### 🔹 ¿Es una planificación?

- Exacto ✅. Es como un diseño contractual:
    1. Se define el contrato (OpenAPI).
    2. Se revisa y aprueba con negocio/equipo.
    3. Se implementa en código.
- Esto evita retrabajos porque todos saben de antemano qué esperar de la API.

### 🔄 API First vs Code First (el contraste que lo aclara todo)

|                 | **Code First** ❌ (lo común al inicio)                    | **API First** ✅ (lo que piden en consultoras, bancos, etc.) |
|-----------------|----------------------------------------------------------|-------------------------------------------------------------|
| **Orden**       | Escribes código → generas la doc después                 | Escribes el contrato → generas/implementas el código        |
| **Contrato**    | Sale del código (anotaciones)                            | El contrato ES la fuente de verdad                          |
| **Cambios**     | El frontend se entera cuando el backend ya cambió        | Todos acuerdan el cambio en el contrato primero             |
| **Equipos**     | Backend y frontend dependen entre sí                     | Pueden trabajar en paralelo desde el día 1                  |
| **Herramienta** | `springdoc-openapi` genera el YAML desde tus anotaciones | `openapi-generator` genera el código desde el YAML          |
