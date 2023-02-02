create table IF NOT EXISTS PUBLIC.RATING
(
    RATING_ID   INTEGER auto_increment,
    RATING_NAME CHARACTER VARYING(10) not null,
    constraint RATING_PK
        primary key (RATING_ID)
);

create table IF NOT EXISTS PUBLIC.FILM
(
    FILM_ID          INTEGER auto_increment,
    FILM_NAME        CHARACTER VARYING(100) not null,
    FILM_DESCRIPTION CHARACTER VARYING(255),
    RELEASE_DATE     DATE                   not null,
    FILM_DURATION    INTEGER                not null,
    FILM_RATING      INTEGER                not null,
    constraint FILM_PK
        primary key (FILM_ID),
    constraint FILM_RATING_RATING_ID_FK
        foreign key (FILM_RATING) references RATING
);

create table IF NOT EXISTS PUBLIC.GENRE
(
    GENRE_ID   INTEGER auto_increment,
    GENRE_NAME CHARACTER VARYING(50) not null,
    constraint GENRE_PK
        primary key (GENRE_ID)
);

create table IF NOT EXISTS PUBLIC.FILM_GENRE
(
    FILM_GENRE_ID INTEGER auto_increment,
    GENRE_ID      INTEGER not null,
    FILM_ID       INTEGER not null,
    constraint FILM_GENRE_PK
        primary key (FILM_GENRE_ID),
    constraint FILM_GENRE_FILM_FILM_ID_FK
        foreign key (FILM_ID) references FILM,
    constraint FILM_GENRE_GENRE_GENRE_ID_FK
        foreign key (GENRE_ID) references GENRE
);

create table IF NOT EXISTS PUBLIC.USERS
(
    USER_ID  INTEGER auto_increment,
    EMAIL    CHARACTER VARYING(100) not null,
    LOGIN    CHARACTER VARYING(50)  not null,
    NAME     CHARACTER VARYING(50),
    BIRTHDAY DATE                   not null,
    constraint USERS_PK
        primary key (USER_ID)
);

create table IF NOT EXISTS PUBLIC.FRIENDS
(
    USER_ID   INTEGER               not null,
    FRIEND_ID INTEGER               not null,
    STATUS    CHARACTER VARYING(20) not null,
    constraint FRIENDS_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS,
    constraint FRIENDS_USERS_USER_ID_FK_2
        foreign key (FRIEND_ID) references USERS
);

create table IF NOT EXISTS PUBLIC.LIKES
(
    LIKE_ID INTEGER auto_increment,
    FILM_ID INTEGER not null,
    USER_ID INTEGER not null,
    constraint LIKES_PK
        primary key (LIKE_ID),
    constraint LIKES_FILM_FILM_ID_FK
        foreign key (FILM_ID) references FILM,
    constraint LIKES_USERS_USER_ID_FK
        foreign key (USER_ID) references USERS
);