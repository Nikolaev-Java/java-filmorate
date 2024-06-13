package ru.yandex.practicum.filmorate.model;

public record Mpa(int id, String name) {
    public Mpa(int id){
        this(id, "");
    }
}
