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

select clubs.name                 as club_name,
       count(distinct coaches.id) as total_coaches,
       count(distinct players.id) as total_players
from clubs
         left join coaches on clubs.id = coaches.club_id
         left join players on clubs.id = players.club_id
group by clubs.name;