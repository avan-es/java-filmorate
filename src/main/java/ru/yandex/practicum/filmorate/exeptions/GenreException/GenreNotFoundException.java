package ru.yandex.practicum.filmorate.exeptions.GenreException;

public class GenreNotFoundException extends RuntimeException {

    public GenreNotFoundException(final String message) {
        super(message);
    }
}