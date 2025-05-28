# Deshabilita las comprobaciones de claves foráneas para evitar errores al crear tablas
set foreign_key_checks = 0;

# borrar todas las tablas (SOLO PARA PRUEBAS)
drop table if exists players_events;
drop table if exists tournaments;
drop table if exists trainings;
drop table if exists club_administrators;
drop table if exists coaches;
drop table if exists players;
drop table if exists clubs;
drop table if exists roles_authorities;
drop table if exists users_roles;
drop table if exists roles;
drop table if exists authorities;
drop table if exists events;
drop table if exists users;

create table if not exists users
(
    id                      bigint auto_increment,
    national_id             bigint(10)                               not null,
    name                    varchar(20)                              not null,
    last_name               varchar(20)                              not null,
    phone_number            bigint(10)                               not null,
    email                   varchar(40)                              not null,
    username                varchar(20)                              not null,
    password                varchar(60)                              not null,
    enabled                 boolean                                  not null default true,
    account_non_expired     boolean                                  not null default true,
    account_non_locked      boolean                                  not null default true,
    credentials_non_expired boolean                                  not null default true,
    affiliation_status      enum ('ACTIVE', 'INACTIVE', 'SUSPENDED') not null default 'ACTIVE',
    primary key (id),
    unique key uk_users_national_id (national_id),
    unique key uk_users_phone_number (phone_number),
    unique key uk_users_email (email),
    unique key uk_users_username (username)
);

create table if not exists club_administrators
(
    id      bigint not null,
    club_id bigint,
    primary key (id),
    constraint fk_club_administrators_users_id
        foreign key (id) references users (id),
    constraint fk_club_administrators_club_id
        foreign key (club_id) references clubs (id)
);

create table if not exists coaches
(
    id      bigint not null,
    club_id bigint,
    primary key (id),
    constraint fk_coaches_users_id
        foreign key (id) references users (id),
    constraint fk_coaches_club_id
        foreign key (club_id) references clubs (id)
);

create table if not exists players
(
    id      bigint not null,
    club_id bigint,
    primary key (id),
    constraint fk_players_users_id
        foreign key (id) references users (id),
    constraint fk_players_club_id
        foreign key (club_id) references clubs (id)
);

create table if not exists clubs
(
    id                    bigint auto_increment,
    club_administrator_id bigint,
    coach_id              bigint,
    player_id             bigint,
    name                  varchar(255) not null,
    address               varchar(255) not null,
    phone_number          bigint       not null,
    enabled               boolean      not null default true,
    creation_date         datetime     not null default now(),
    primary key (id),
    constraint fk_clubs_club_administrators_id
        foreign key (club_administrator_id) references club_administrators (id),
    constraint fk_clubs_coaches_id
        foreign key (coach_id) references coaches (id),
    constraint fk_clubs_players_id foreign key (player_id) references players (id),
    unique key uk_clubs_name (name),
    unique key uk_clubs_phone_number (phone_number)

);

create table if not exists roles
(
    id   bigint auto_increment,
    name varchar(255) not null,
    primary key (id),
    unique key uk_roles_name (name)
);

create table if not exists authorities
(
    id   bigint auto_increment,
    name varchar(255) not null,
    primary key (id),
    unique key uk_authorities_name (name)
);

create table if not exists users_roles
(
    user_id bigint not null,
    role_id bigint not null,
    primary key (user_id, role_id),
    constraint fk_users_roles_user_id foreign key (user_id) references users (id),
    constraint fk_users_roles_role_id foreign key (role_id) references roles (id)
);

create table if not exists roles_authorities
(
    role_id      bigint not null,
    authority_id bigint not null,
    primary key (role_id, authority_id),
    constraint fk_roles_authorities_role_id foreign key (role_id) references roles (id),
    constraint fk_roles_authorities_authority_id foreign key (authority_id) references authorities (id)
);

create table if not exists events
(
    id                   bigint auto_increment,
    name                 varchar(50)                not null,
    description          varchar(255)               not null,
    location             varchar(50)                not null,
    start_date           datetime                   not null,
    end_date             datetime                   not null,
    event_visibility     enum ('PUBLIC', 'PRIVATE') not null,
    maximum_participants int                        not null,
    primary key (id),
    unique key events_name (name)
);

create table if not exists tournaments
(
    id bigint not null,
    primary key (id),
    constraint fk_tournaments_events_id
        foreign key (id) references events (id)
);

create table if not exists trainings
(
    id bigint not null,
    primary key (id),
    constraint fk_training_events_id
        foreign key (id) references events (id)
);

create table if not exists players_events
(
    player_id bigint not null,
    event_id  bigint not null,
    primary key (player_id, event_id),
    constraint players_events_player_id foreign key (player_id) references players (id),
    constraint players_events_event_id foreign key (event_id) references events (id)
);

# Habilita las comprobaciones de claves foráneas de nuevo para evitar errores al crear tablas
set foreign_key_checks = 1;