# Sports Club Manager - Backend

El **Sports Club Manager** es una plataforma digital diseñada para facilitar la conexión entre jugadores, clubes y eventos de ajedrez en Montería. Su objetivo principal es optimizar la gestión administrativa y promover el deporte en la ciudad, aumentando la participación en la comunidad ajedrecística y mejorando la visibilidad de actividades y torneos.

## Características

- **Gestión de Usuarios**: Crear, actualizar, eliminar y consultar información de usuarios, incluyendo jugadores, entrenadores y administradores.
- **Gestión de Clubes**: Administración de clubes de ajedrez, incluyendo detalles como dirección, número de teléfono y estado de habilitación.
- **Gestión de Eventos**: Organización y promoción de torneos y actividades relacionadas con el ajedrez.
- **Control de Acceso Basado en Roles**: Asignación de roles (administrador, entrenador, jugador) y permisos para un control de acceso detallado.
- **Autenticación y Autorización**: Protección de endpoints mediante autenticación basada en JWT.
- **Paginación y Filtros**: Soporte para la recuperación paginada y filtrada de datos.
- **Configuración de CORS**: Permite solicitudes de origen cruzado para la integración con el frontend.
- **Extensibilidad**: Fácilmente ampliable para incluir nuevos roles, funcionalidades y tipos de eventos.

## Tecnologías Utilizadas

- **Spring Boot**: Framework para el desarrollo del backend.
- **Spring Data JPA**: Para la gestión de la persistencia de datos.
- **Spring Security**: Para autenticación y autorización.
- **JWT**: Autenticación basada en tokens para acceso seguro.
- **MySQL**: Base de datos relacional para almacenar datos de usuarios, roles, clubes y eventos.
- **Lombok**: Simplifica el código Java reduciendo el boilerplate.
- **MapStruct**: Para la conversión entre entidades y DTOs (Data Transfer Objects).
- **Maven**: Herramienta de construcción y gestión de dependencias.

## Objetivo del Proyecto

Desarrollar e implementar **Sports Club Manager**, una plataforma que facilite la conexión entre jugadores, clubes y eventos de ajedrez en Montería. Esto permitirá:

- Optimizar la gestión administrativa de clubes y eventos.
- Promover el deporte del ajedrez en la ciudad.
- Aumentar la participación en la comunidad ajedrecística.
- Mejorar la visibilidad de actividades y torneos.

## Documentación (Inglés)

Aquí podrás encontrar la documentación del proyecto gracias a Devin.

[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/stevenrq/sports-club-manager-backend)

## Cómo Empezar

1. **Clonar el Repositorio**:

   ```bash
   git clone https://github.com/stevenrq/sports-club-manager-backend.git
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

   > **_NOTA:_**
   > Puedes usar este comando o reconstruir el proyecto cada vez que te de algún tipo de error en tiempo de compilación debido a las librerías Lombok y MapStruct.

4. **Ejecutar la Aplicación**: Inicia la aplicación:

   ```bash
   ./mvnw spring-boot:run
   ```

## Contribuciones

¡Las contribuciones son bienvenidas! Si deseas colaborar, por favor sigue estos pasos:

1. Haz un fork del repositorio.
2. Crea una rama para tu funcionalidad o corrección (`git checkout -b feature/nueva-funcionalidad`).
3. Realiza tus cambios y haz commit (`git commit -m "Agrega nueva funcionalidad"`).
4. Sube tus cambios (`git push origin feature/nueva-funcionalidad`).
5. Abre un Pull Request.

## Licencia

Este proyecto está licenciado bajo la [Licencia MIT](LICENSE.txt). Puedes usarlo, modificarlo y distribuirlo libremente.
