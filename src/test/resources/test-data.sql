insert into USERS(email, login, name, birthday)
VALUES ('test1@test.com', 'testLogin01', 'testName01', '1992-05-31'),
       ('test2@test.com', 'testLogin02', 'testName02', '1992-05-31'),
       ('test3@test.com', 'testLogin03', 'testName03', '1992-05-31');

insert into FRIENDSHIP(user_id, friend_id)
VALUES (1, 3),
       (2, 3);

insert into mpa (mpa_id, mpa_name)
values (1, 'test01'),
       (2, 'test02');

insert into genres (genre_id, genre_name)
values (1, 'test01'),
       (2, 'test02');

insert into FILMS(FILM_NAME, DESCRIPTION, RELEASE_DATE, DURATION, MPA_ID)
values ('test01', 'test01', '2020-05-10', 100, 1),
       ('test02', 'test02', '2020-05-10', 100, 2),
       ('test03', 'test03', '2020-05-10', 100, 1),
       ('test04', 'test04', '2020-05-10', 100, 1);

insert into LIKES(FILM_ID, USER_ID)
VALUES (1, 1),
       (1, 2),
       (1, 3),
       (3, 3),
       (2, 1),
       (2, 2);

insert into FILM_GENRES(film_id, genre_id)
VALUES (1, 1),
       (1, 2),
       (3, 1);