SET REFERENTIAL_INTEGRITY FALSE;
TRUNCATE TABLE GENRE RESTART IDENTITY;
INSERT INTO GENRE (GENRE_ID, GENRE_NAME)
VALUES (1, 'аниме'),
    (2, 'биографический'),
    (3, 'боевик'),
    (4, 'вестерн'),
    (5, 'военный'),
    (6, 'детектив'),
    (7, 'детский'),
    (8, 'документальный'),
    (9, 'драма'),
    (10, 'исторический'),
    (11, 'кинокомикс'),
    (12, 'комедия'),
    (13, 'концерт'),
    (14, 'короткометражный'),
    (15, 'криминал'),
    (16, 'мелодрама'),
    (17, 'мистика'),
    (18, 'музыка'),
    (19, 'мультфильм'),
    (20, 'мюзикл'),
    (21, 'научный'),
    (22, 'нуар'),
    (23, 'приключения'),
    (24, 'реалити-шоу'),
    (25, 'семейный'),
    (26, 'спорт'),
    (27, 'ток-шоу'),
    (28, 'триллер'),
    (29, 'ужасы'),
    (30, 'фантастика'),
    (31, 'фэнтези'),
    (32, 'эротика');

TRUNCATE TABLE RATING RESTART IDENTITY;
INSERT INTO RATING (RATING_ID, RATING_NAME)
VALUES (1, 'G'), (2, 'PG'),(3,'PG-13'),(4,'R'),(5,'NC-17');

TRUNCATE TABLE FILM RESTART IDENTITY;
insert into FILM (FILM_ID, FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, FILM_DURATION, FILM_RATING)
VALUES (1, 'фИЛЬМ 1','оПИСАНИЕ 1',date '2004-12-31',120,1),
       (2, 'Фильм 2','Описание 2',DATE '2005-12-31',52,2),
       (3, 'Фильм 3','Описание 3',DATE '2006-12-31',100,5);

TRUNCATE TABLE USERS RESTART IDENTITY;
INSERT INTO USERS (USER_ID, EMAIL, LOGIN, NAME, BIRTHDAY)
VALUES (1, 'user1@mail.ru', 'user1', 'user1', DATE '1994-09-01'),
       (2, 'user2@mail.ru', 'user2', 'user2', DATE '1994-09-02'),
       (3, 'user3@mail.ru', 'user3', 'user3', DATE '1994-09-03'),
       (4, 'user4@mail.ru', 'user4', 'user4', DATE '1994-09-04'),
       (5, 'user5@mail.ru', 'user5', 'user5', DATE '1994-09-05');
SET REFERENTIAL_INTEGRITY TRUE;