# Sports Club Manager - Backend

**Sports Club Manager** es una plataforma digital para la gestión de clubes, usuarios y eventos de ajedrez en Montería. Permite la administración eficiente de clubes, torneos, entrenadores y jugadores, promoviendo la participación y visibilidad del ajedrez local.

## Tabla de Contenidos

- [Características](#características)
- [Tecnologías y Dependencias](#tecnologías-y-dependencias)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Modelo de Base de Datos](#modelo-de-base-de-datos)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Principales Endpoints](#principales-endpoints)
- [Consultas SQL Útiles](#consultas-sql-útiles)
- [Documentación DeepWiki](#documentación-deepwiki)
- [Contribuciones](#contribuciones)
- [Licencia](#licencia)

## Características

- **Gestión de Usuarios**: CRUD de jugadores, entrenadores y administradores.
- **Gestión de Clubes**: Administración de clubes, dirección, teléfono y estado.
- **Gestión de Eventos**: Organización de torneos y entrenamientos.
- **Control de Acceso por Roles**: Roles y permisos detallados (admin, entrenador, jugador).
- **Autenticación JWT**: Seguridad robusta en los endpoints.
- **Paginación y Filtros**: Consultas eficientes y personalizables.
- **Configuración CORS**: Integración sencilla con frontend.
- **Extensible**: Fácil de ampliar con nuevos roles y funcionalidades.

## Tecnologías y Dependencias

- **Java 17**
- **Spring Boot 3.5** (Web, Security, Data JPA, Validation, Actuator)
- **MySQL** (persistencia de datos)
- **JWT (jjwt)** (autenticación)
- **Lombok** y **MapStruct** (boilerplate y mapeo DTO)
- **Maven** (gestión de dependencias)

## Estructura del Proyecto

```
src/
  main/
    java/com/sportsclubmanager/backend/
      auth/           # Seguridad y autenticación
      club/           # Lógica y controladores de clubes
      event/          # Lógica y controladores de eventos
      member/         # Lógica de jugadores, entrenadores, admins
      shared/         # Utilidades y validaciones comunes
      user/           # Gestión de usuarios y roles
    resources/
      application.properties  # Configuración
      schema.sql              # Esquema de base de datos
      data.sql                # Datos de ejemplo
      queries.sql             # Consultas SQL útiles
  test/
    java/com/sportsclubmanager/backend/ # Pruebas unitarias
```

## Modelo de Base de Datos

- **Usuarios**: Roles (jugador, entrenador, admin), credenciales, estado de afiliación.
- **Clubes**: Relación con administradores, entrenadores y jugadores.
- **Eventos**: Torneos y entrenamientos, visibilidad, fechas y participantes.
- **Relaciones**: Tablas de unión para roles, autoridades y participación en eventos.

Los scripts `schema.sql` y `data.sql` permiten crear y poblar la base de datos automáticamente.

## Instalación y Ejecución

1. **Clona el repositorio:**
    ```bash
    git clone https://github.com/stevenrq/sports-club-manager-backend.git
    ```
2. **Configura la base de datos:** Edita `src/main/resources/application.properties`:
    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/sports_club_manager
    spring.datasource.username=<tu-usuario>
    spring.datasource.password=<tu-contraseña>
    ```
3. **Construye el proyecto:**
    ```bash
    ./mvnw clean install
    ```
4. **Ejecuta la aplicación:**
    ```bash
    ./mvnw spring-boot:run
    ```

## Principales Endpoints

- **/api/auth/**: Autenticación y registro de usuarios.
- **/api/users/**: Gestión de usuarios (CRUD, roles, afiliación).
- **/api/clubs/**: Gestión de clubes (crear, listar, actualizar, eliminar).
- **/api/events/**: Gestión de eventos (torneos, entrenamientos).

> La documentación OpenAPI/Swagger puede ser añadida para facilitar la exploración de los endpoints.

## Consultas SQL Útiles

- `queries.sql` contiene ejemplos para obtener usuarios con roles, detalles de clubes y estadísticas.

## Documentación DeepWiki

Aquí podrás encontrar la documentación del proyecto generada automáticamente:

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/stevenrq/sports-club-manager-backend)

## Contribuciones

¡Las contribuciones son bienvenidas! Para colaborar:

1. Haz un fork del repositorio.
2. Crea una rama (`git checkout -b feature/nueva-funcionalidad`).
3. Realiza tus cambios y haz commit.
4. Sube tus cambios (`git push origin feature/nueva-funcionalidad`).
5. Abre un Pull Request.

## Licencia

Este proyecto está bajo la [Licencia Apache 2.0](LICENSE).

---

> Proyecto desarrollado para la comunidad ajedrecística de Montería.
