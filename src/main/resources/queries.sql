-- Consulta para obtener información detallada sobre los clubes y sus relaciones con
-- administradores, entrenadores y jugadores.
select admin_user.name     as club_administrator_name,
       coach_user.name     as coach_name,
       player_user.name    as player_name,
       clubs.name          as club_name,
       clubs.address       as club_address,
       clubs.phone_number  as club_phone_number,
       clubs.enabled       as club_enabled,
       clubs.creation_date as club_creation_date
from clubs
         join club_administrators on clubs.club_administrator_id = club_administrators.id
         join users as admin_user on club_administrators.id = admin_user.id
         join coaches on clubs.coach_id = coaches.id
         join users as coach_user on coaches.id = coach_user.id
         join players on clubs.player_id = players.id
         join users as player_user on players.id = player_user.id;

-- Obtiene la cantidad de entrenadores y jugadores asociados a un club
select clubs.name                 as club_name,
       count(distinct coaches.id) as total_coaches,
       count(distinct players.id) as total_players
from clubs
         left join coaches on clubs.id = coaches.club_id
         left join players on clubs.id = players.club_id
group by clubs.name;

-- Obtiene el club asociado a cada usuario según su rol
select u.name         as user_name,
       u.email        as user_email,
       r.name         as role_name,
       c.name         as club_name,
       c.address      as club_address,
       c.phone_number as club_phone_number
from users u
         join users_roles ur on u.id = ur.user_id
         join roles r on ur.role_id = r.id
         left join club_administrators ca on u.id = ca.id
         left join coaches co on u.id = co.id
         left join players p on u.id = p.id
         left join clubs c on c.id  = co.club_id or c.id = p.club_id
order by r.name, u.name;