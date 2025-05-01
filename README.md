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