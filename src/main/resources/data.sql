insert into genres (genre_id, genre_name)
values (1, 'Комедия'),
       (2, 'Драма'),
       (3, 'Мультфильм'),
       (4, 'Триллер'),
       (5, 'Документальный'),
       (6, 'Боевик') on conflict do nothing;

insert into mpa (mpa_id, mpa_name)
values (1, 'G'),
       (2, 'PG'),
       (3, 'PG-13'),
       (4, 'R'),
       (5, 'NC-17') on conflict do nothing;

alter table GENRES
    alter column genre_id INTEGER generated by default as identity (restart with 7);
alter table MPA
    alter column MPA_ID integer generated by default as identity (restart with 6);