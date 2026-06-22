# Sistema de Reservas de Salas — Biblioteca Duoc UC

Proyecto semestral de arquitectura de microservicios para la asignatura **Desarrollo FullStack II (DSY1103)**.

## Integrante del equipo

- Rodrigo Rojas

---

## Contexto del dominio

El sistema permite a estudiantes de la Biblioteca Duoc UC reservar salas de estudio. Cada sala pertenece a un tipo (laboratorio, sala de reuniones, etc.) y está asociada a un instituto. Los estudiantes se identifican por su RUN y pertenecen a una carrera académica. Las reservas registran el estudiante, la sala, la fecha y el bloque horario solicitado.

El sistema está compuesto por microservicios independientes que se comunican vía REST y se exponen al exterior a través de un API Gateway centralizado.

---

## Microservicios implementados

| Microservicio | Puerto | Descripción |
|---|---|---|
| `ms-gateway` | 8080 | API Gateway — punto de entrada único para todos los servicios |
| `biblioteca` | 8081 | Gestión de salas, reservas, estudiantes, carreras y tipos de sala |
| `ms-auth` | 8082 | Autenticación de usuarios mediante JWT |
| `ms-autor` | 8083 | Gestión de autores de libros |
| `ms-libro` | 8084 | Gestión de libros con comunicación REST hacia `ms-autor` |

---

## Rutas principales del Gateway

Todas las peticiones pasan por `http://localhost:8080`.

| Ruta | Destino | Descripción |
|---|---|---|
| `POST /auth/login` | ms-auth | Obtener token JWT |
| `GET /api/v2/salas` | biblioteca | Listar salas disponibles |
| `GET /api/v2/salas/{codigo}` | biblioteca | Obtener sala por código |
| `GET /api/v2/reservas` | biblioteca | Listar todas las reservas |
| `GET /api/v2/reservas/{id}` | biblioteca | Obtener reserva por ID |
| `GET /api/v2/reservas/sala/{codigoSala}` | biblioteca | Reservas por sala |
| `GET /api/v2/reservas/fecha/{fecha}` | biblioteca | Reservas por fecha (yyyy-MM-dd) |
| `GET /api/v2/reservas/estado/{estado}` | biblioteca | Reservas por estado (1=Activa, 0=Cancelada) |
| `GET /api/v2/estudiantes` | biblioteca | Listar estudiantes |
| `GET /api/v2/estudiantes/{id}` | biblioteca | Obtener estudiante por ID |
| `GET /api/v2/carreras` | biblioteca | Listar carreras |
| `GET /api/v2/tipos-salas` | biblioteca | Listar tipos de sala |
| `GET /api/carreras` | biblioteca | Listar carreras (v1) |
| `GET /api/autores` | ms-autor | Listar autores (requiere JWT) |
| `POST /api/autores` | ms-autor | Crear autor (requiere JWT + rol ADMIN) |
| `GET /api/libros` | ms-libro | Listar libros con autor resuelto (requiere JWT) |
| `POST /api/libros` | ms-libro | Crear libro (requiere JWT + rol ADMIN) |

---

## Documentación Swagger

La documentación interactiva del microservicio principal está disponible en:

- **Local:** [http://localhost:8080/doc/swagger-ui.html](http://localhost:8080/doc/swagger-ui.html)

---

## Instrucciones de ejecución local

### Opción 1: Docker Compose (recomendado)

Requiere tener Docker Desktop instalado y activo.

```bash
# Desde la raíz del proyecto
docker compose up --build
```

Este comando levanta la base de datos MySQL y todos los microservicios automáticamente.

Para detener y eliminar los contenedores:

```bash
docker compose down
```

Para detener y también limpiar los volúmenes de base de datos:

```bash
docker compose down -v
```

### Opción 2: Ejecución desde el IDE

Requiere Java 21, Maven y una instancia de MySQL corriendo en el puerto 3307.

1. Levantar solo la base de datos con Docker:

```bash
docker compose up mysql-db
```

2. Crear los schemas necesarios en MySQL:

```sql
CREATE DATABASE IF NOT EXISTS `db_biblioteca`;
CREATE DATABASE IF NOT EXISTS `ms-user`;
CREATE DATABASE IF NOT EXISTS `ms-autor`;
CREATE DATABASE IF NOT EXISTS `ms-libro`;
```

3. Ejecutar cada microservicio desde el IDE en este orden:
   - `ms-auth` (puerto 8082)
   - `ms-autor` (puerto 8083)
   - `ms-libro` (puerto 8084)
   - `biblioteca` (puerto 8081)
   - `ms-gateway` (puerto 8080)

### Variables de entorno principales

| Variable | Descripción | Valor por defecto |
|---|---|---|
| `DB_HOST` | Host de la base de datos | `localhost` |
| `DB_PORT` | Puerto de la base de datos | `3307` |
| `DB_PASSWORD` | Contraseña de MySQL | *(vacío)* |
| `JWT_SECRET` | Clave para firmar los tokens JWT | `clave_super_secreta_...` |
| `MS_AUTOR_URL` | URL del servicio ms-autor | `http://localhost:8083` |

---

## Ejecución de pruebas unitarias

```bash
# Desde la carpeta del microservicio biblioteca
cd biblioteca
./mvnw test
```

Las pruebas cubren la capa de servicios y controllers del microservicio `biblioteca`, usando JUnit 5 y Mockito.
