-- Devuelve todos los usuarios con sus roles verificando tablas específicas de roles
SELECT u.id,
       u.name,
       u.last_name,
       u.email,
       u.username,
       CASE
           WHEN ca.id IS NOT NULL THEN 'Club Administrator'
           WHEN c.id IS NOT NULL THEN 'Coach'
           WHEN p.id IS NOT NULL THEN 'Player'
           ELSE 'System Administrator'
           END AS role
FROM users u
         LEFT JOIN club_administrators ca ON ca.id = u.id
         LEFT JOIN coaches c ON c.id = u.id
         LEFT JOIN players p ON p.id = u.id;

-- Devuelve los detalles del club con la información asociada del administrador, entrenador y jugador.
SELECT cl.id   AS club_id,
       cl.name AS club_name,
       cl.address,
       cl.phone_number,
       ca.id   AS admin_id,
       ua.name AS admin_name,
       c.id    AS coach_id,
       uc.name AS coach_name,
       p.id    AS player_id,
       up.name AS player_name
FROM clubs cl
         LEFT JOIN club_administrators ca ON cl.club_administrator_id = ca.id
         LEFT JOIN users ua ON ca.id = ua.id
         LEFT JOIN coaches c ON cl.coach_id = c.id
         LEFT JOIN users uc ON c.id = uc.id
         LEFT JOIN players p ON cl.player_id = p.id
         LEFT JOIN users up ON p.id = up.id
WHERE cl.enabled = 1;

-- Devuelve el recuento de usuarios por rol
SELECT 'Club Administrators' as role, COUNT(*)
FROM club_administrators
UNION
SELECT 'Coaches', COUNT(*)
FROM coaches
UNION
SELECT 'Players', COUNT(*)
FROM players;

-- Devoluciones de clubes habilitados ordenados por fecha de creación
SELECT id, name, address, phone_number, enabled, creation_date
FROM clubs
ORDER BY creation_date DESC;

-- Devuelve los usuarios que no están en estado ACTIVO
SELECT id, name, last_name, email, username, affiliation_status
FROM users
WHERE affiliation_status <> 'ACTIVE';

-- Devuelve correos electrónicos que aparecen más de una vez
SELECT email, COUNT(*) as times_used
FROM users
GROUP BY email
HAVING COUNT(*) > 1;

-- Devuelve números de teléfono que aparecen más de una vez
SELECT phone_number, COUNT(*) as times_used
FROM users
GROUP BY phone_number
HAVING COUNT(*) > 1;

-- Devuelve información del jugador con su club asociado.
SELECT u.id   AS player_id,
       u.name AS player_name,
       u.last_name,
       c.id   AS club_id,
       c.name AS club_name
FROM players p
         JOIN users u ON u.id = p.id
         LEFT JOIN clubs c ON p.club_id = c.id;

-- Devuelve las estadísticas del club que muestran el número de miembros por rol.
SELECT c.id                                                                  AS club_id,
       c.name                                                                AS club_name,
       (SELECT COUNT(*) FROM club_administrators ca WHERE ca.club_id = c.id) AS administrators,
       (SELECT COUNT(*) FROM coaches co WHERE co.club_id = c.id)             AS coaches,
       (SELECT COUNT(*) FROM players p WHERE p.club_id = c.id)               AS players
FROM clubs c;

-- Obtener todos los eventos
SELECT *
FROM events;

-- Buscar eventos públicos ordenados por fecha de inicio (más próximos primero)
SELECT id, name, location, start_date, end_date
FROM events
WHERE event_visibility = 'PUBLIC'
ORDER BY start_date;

-- Contar la cantidad de eventos según visibilidad
SELECT event_visibility, COUNT(*) AS cantidad
FROM events
GROUP BY event_visibility;

-- Obtener los eventos que tienen más de 50 participantes permitidos
SELECT id, name, maximum_participants
FROM events
WHERE maximum_participants > 50;

-- Buscar eventos activos al día de hoy
SELECT id, name, start_date, end_date
FROM events
WHERE NOW() BETWEEN start_date AND end_date;

-- Buscar eventos que ya han terminado
SELECT id, name, end_date
FROM events
WHERE end_date < NOW();

-- Buscar eventos por nombre (ejemplo parcial)
SELECT *
FROM events
WHERE name LIKE '%torneo%';

-- Listar los nombres de eventos con su duración en días
SELECT id, name, DATEDIFF(end_date, start_date) AS duracion_dias
FROM events;

-- Obtener la cantidad máxima de participantes permitidos en todos los eventos privados
SELECT MAX(maximum_participants) AS max_participantes_privados
FROM events
WHERE event_visibility = 'PRIVATE';

-- Mostrar eventos y el número de jugadores inscritos (requiere tabla players_events)
SELECT e.id, e.name, COUNT(pe.player_id) AS inscritos
FROM events e
         LEFT JOIN players_events pe ON e.id = pe.event_id
GROUP BY e.id, e.name;