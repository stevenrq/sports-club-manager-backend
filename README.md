# Backend del Gestor de Club Deportivo

El Backend del Gestor de Club Deportivo es una aplicación desarrollada con Spring Boot que permite gestionar las operaciones de un club deportivo. Proporciona un sistema robusto para la administración de usuarios, control de acceso basado en roles y autenticación segura mediante JWT. La aplicación está diseñada para ser escalable y segura, ideal para gestionar jugadores, entrenadores, administradores y otros roles dentro de un club deportivo.

## Características

- **Gestión de Usuarios**: Crear, actualizar, eliminar y consultar información de usuarios.
- **Control de Acceso Basado en Roles**: Asignación de roles y permisos para un control de acceso detallado.
- **Autenticación y Autorización**: Protección de endpoints mediante autenticación basada en JWT.
- **Paginación**: Soporte para la recuperación paginada de datos de usuarios.
- **Configuración de CORS**: Permite solicitudes de origen cruzado para la integración con el frontend.
- **Extensibilidad**: Fácilmente ampliable para incluir nuevos roles y funcionalidades.

## Tecnologías Utilizadas

- **Spring Boot**: Framework para el desarrollo del backend.
- **Spring Data JPA**: Para la gestión de la persistencia de datos.
- **Spring Security**: Para autenticación y autorización.
- **JWT**: Autenticación basada en tokens para acceso seguro.
- **MySQL**: Base de datos relacional para almacenar datos de usuarios y roles.
- **Lombok**: Simplifica el código Java reduciendo el boilerplate.
- **MapStruct**: Para la conversión entre entidades y DTOs (Data Transfer Objects).
- **Maven**: Herramienta de construcción y gestión de dependencias.

## Cómo Empezar

1. **Clonar el Repositorio**:

   ```bash
   git clone https://github.com/Stevenrq/sports-club-manager-backend.git
   cd sports-club-manager-backend
   ```

2. **Configurar la Base de Datos**: Actualiza los detalles de conexión en `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/sports_club_manager
   spring.datasource.username=<tu-usuario>
   spring.datasource.password=<tu-contraseña>
   ```

3. **Construir el Proyecto**: Usa Maven para construir el proyecto:

   ```bash
   ./mvnw clean install
   ```

4. **Ejecutar la Aplicación**: Inicia la aplicación:

   ```bash
   ./mvnw spring-boot:run
   ```

   > **_NOTA:_**
   > Puedes usar este comando cada vez que te de algún tipo de error en tiempo de compilación debido a las librerías Lombok y MapStruct.

## Endpoints de la API

### Autenticación

- **`POST /login`**  
  **Descripción**: Autentica un usuario y devuelve un token JWT.  
  **Cuerpo de la solicitud**:
  ```json
  {
    "username": "admin",
    "password": "admin"
  }
  ```
  **Respuesta exitosa**:
  ```json
  {
    "access_token": "Bearer <token>"
  }
  ```
  **Errores comunes**:
  - **401 Unauthorized**: Credenciales incorrectas.
  - **403 Forbidden**: Usuario deshabilitado.

---

### Gestión de Usuarios

#### Crear un Usuario

- **`POST /api/users`**  
  **Descripción**: Crear un nuevo usuario.  
  **Cuerpo de la solicitud**:
  ```json
  {
    "name": "Carlos",
    "lastName": "Pérez",
    "phoneNumber": "3001234567",
    "email": "carlos.perez@example.com",
    "username": "carlosperez",
    "password": "password123",
    "roles": [
      {
        "id": 5,
        "name": "ROLE_USER"
      }
    ]
  }
  ```
  **Respuesta exitosa**:
  ```json
  {
    "id": 6,
    "name": "Carlos",
    "lastName": "Pérez",
    "phoneNumber": "3001234567",
    "email": "carlos.perez@example.com",
    "username": "carlosperez",
    "roles": [
      {
        "id": 5,
        "name": "ROLE_USER"
      }
    ]
  }
  ```
  **Permisos requeridos**: `ROLE_ADMIN` o `AUTHORITY_WRITE`.

---

#### Consultar un Usuario por ID

- **`GET /api/users/{id}`**  
  **Descripción**: Consultar un usuario por ID.  
  **Encabezado de autorización**:  
  `Authorization: Bearer <token>`  
  **Respuesta exitosa**:
  ```json
  {
    "id": 3,
    "name": "Jane",
    "lastName": "Doe",
    "phoneNumber": "3125467789",
    "email": "janedoe23@gmail.com",
    "username": "janedoe23",
    "roles": [
      {
        "id": 3,
        "name": "ROLE_COACH"
      }
    ]
  }
  ```
  **Permisos requeridos**: `ROLE_ADMIN` o `AUTHORITY_VIEW_ALL`.

---

#### Consultar un Usuario por Nombre de Usuario

- **`GET /api/users/username/{username}`**  
  **Descripción**: Consultar un usuario por nombre de usuario.  
  **Encabezado de autorización**:  
  `Authorization: Bearer <token>`  
  **Respuesta exitosa**:
  ```json
  {
    "id": 4,
    "name": "John",
    "lastName": "Smith",
    "phoneNumber": "3123456789",
    "email": "johnsmith13@gmail.com",
    "username": "johnsmith13",
    "roles": [
      {
        "id": 4,
        "name": "ROLE_PLAYER"
      }
    ]
  }
  ```
  **Permisos requeridos**: `ROLE_ADMIN` o `AUTHORITY_VIEW_MEMBERS`.

---

#### Consultar Todos los Usuarios

- **`GET /api/users`**  
  **Descripción**: Consultar todos los usuarios.  
  **Encabezado de autorización**:  
  `Authorization: Bearer <token>`  
  **Respuesta exitosa**:
  ```json
  [
    {
      "id": 1,
      "name": "Admin",
      "lastName": "Admin",
      "phoneNumber": "3207108160",
      "email": "admin@gmail.com",
      "username": "admin",
      "roles": [
        {
          "id": 1,
          "name": "ROLE_ADMIN"
        }
      ]
    },
    {
      "id": 2,
      "name": "Alexa",
      "lastName": "Rodríguez Hernández",
      "phoneNumber": "3156765546",
      "email": "alexarh23@gmail.com",
      "username": "alexarh23",
      "roles": [
        {
          "id": 2,
          "name": "ROLE_CLUB_ADMIN"
        }
      ]
    }
  ]
  ```
  **Permisos requeridos**: `ROLE_ADMIN` o `AUTHORITY_VIEW_ALL`.

---

#### Actualizar un Usuario

- **`PUT /api/users/{id}`**  
  **Descripción**: Actualizar información de un usuario.  
  **Cuerpo de la solicitud**:
  ```json
  {
    "name": "Jane Updated",
    "lastName": "Doe Updated",
    "phoneNumber": "3125467789",
    "email": "janedoe.updated@gmail.com",
    "username": "janedoe23",
    "roles": [
      {
        "id": 3,
        "name": "ROLE_COACH"
      }
    ]
  }
  ```
  **Encabezado de autorización**:  
  `Authorization: Bearer <token>`  
  **Respuesta exitosa**:
  ```json
  {
    "id": 3,
    "name": "Jane Updated",
    "lastName": "Doe Updated",
    "phoneNumber": "3125467789",
    "email": "janedoe.updated@gmail.com",
    "username": "janedoe23",
    "roles": [
      {
        "id": 3,
        "name": "ROLE_COACH"
      }
    ]
  }
  ```
  **Permisos requeridos**: `ROLE_ADMIN` o `AUTHORITY_WRITE`.

---

#### Eliminar un Usuario

- **`DELETE /api/users/{id}`**  
  **Descripción**: Eliminar un usuario por ID.  
  **Encabezado de autorización**:  
  `Authorization: Bearer <token>`  
  **Respuesta exitosa**:  
  Código de estado `204 No Content`.  
  **Permisos requeridos**: `ROLE_ADMIN`.

---

### Notas Adicionales

- **Roles y Permisos**:

  - `ROLE_ADMIN`: Acceso completo a todos los endpoints.
  - `ROLE_CLUB_ADMIN`: Gestión de clubes y miembros.
  - `ROLE_COACH`: Gestión de entrenamientos.
  - `ROLE_PLAYER`: Acceso limitado a su perfil y torneos.
  - `ROLE_USER`: Acceso básico.

- **Autenticación**:  
  Todos los endpoints protegidos requieren un token JWT válido en el encabezado `Authorization`.

- **Paginación**:  
  El tamaño de página predeterminado es de 5 elementos por página.
