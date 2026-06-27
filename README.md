# Sistema de Inscripción y Validación de Eventos (MVP)

Plataforma para gestionar las inscripciones al evento **"Día del Padre"**. Valida la
elegibilidad de cada solicitante contra una API externa de colegiados, permite a un
administrador aprobar o rechazar solicitudes, y controla de forma estricta el aforo.

**Stack:** Java 21 · Spring Boot · PostgreSQL · Thymeleaf · json-server (mock) · Docker Compose.

---

## Requisitos

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) instalado y en ejecución.

No se necesita instalar Java, Maven ni PostgreSQL: todo corre en contenedores.

## Arranque

Desde la raíz del proyecto:

```bash
docker compose up --build
```

Este único comando levanta la base de datos, el mock de colegiados y la aplicación.

Para detener:

```bash
docker compose down        # detiene
docker compose down -v     # detiene y borra todos los datos
```

## Acceso

| Vista | URL |
|---|---|
| Portal de inscripción | http://localhost:8080/inscripcion |
| Dashboard de administración | http://localhost:8080/dashboard |
| API mock de colegiados | http://localhost:3000/colegiados |

## Credenciales de la base de datos

| Parámetro | Valor |
|---|---|
| Base de datos | `inscripciones` |
| Usuario | `postgres` |
| Contraseña | `postgres` |
| Puerto (host) | `5433` |

> El puerto se mapea a `5433` en el host para no chocar con instalaciones locales de
> PostgreSQL. Dentro de Docker los contenedores se comunican por el puerto interno `5432`.

## DNIs de prueba

| DNI | Resultado |
|---|---|
| `12345678`, `99887766` | Aceptado |
| `87654321` | Rechazado (administrativo) |
| `11223344` | Rechazado (no habilitado) |
| `44332211` | Rechazado (no es de Lima) |
| `00000000` | Rechazado (no registrado) |

Reglas de aceptación: el colegiado debe estar habilitado, pertenecer al consejo de **Lima**
y no ser personal administrativo. El cupo máximo del evento se configura en
`application.yaml` (`app.aforo`).

---

## Persistencia

Se usa **Spring Data JPA sobre PostgreSQL**. El esquema de la tabla se genera
automáticamente al arrancar (`ddl-auto: update`). Los datos persisten en un **volumen de
Docker** (`postgres_data`), por lo que sobreviven a reinicios de los contenedores. La imagen
del DNI del menor se guarda en disco (carpeta `uploads/`, mapeada como volumen) y en la base
de datos solo se almacena el nombre del archivo, manteniéndola ligera.

## Concurrencia en los cupos

El riesgo es que dos aprobaciones simultáneas superen el aforo (condición de carrera): ambas
leen el mismo conteo de cupos antes de que la otra guarde, y las dos aprueban. Para evitarlo,
la aprobación combina dos mecanismos:

- **`@Transactional`**: la verificación del cupo y el cambio de estado ocurren en una
  transacción atómica (todo o nada).
- **`synchronized`**: el método de aprobación se ejecuta de un hilo a la vez, de modo que dos
  administradores no puedan leer el mismo conteo en simultáneo.

La verificación del cupo se hace **antes** de aprobar; si el aforo está lleno, la operación se
aborta. El aforo también se valida en el portal (tanto al mostrar el formulario como al
procesarlo), para no depender solo de la validación del lado del cliente.

> Esta estrategia es correcta para un despliegue de **una sola instancia** (el caso de este
> MVP). En un escenario distribuido se trasladaría el control a la base de datos mediante
> bloqueo pesimista (`SELECT ... FOR UPDATE`), ya que `synchronized` solo opera dentro de una
> misma JVM.
