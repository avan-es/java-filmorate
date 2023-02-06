package ru.yandex.practicum.filmorate.exeptions.FilmExeptions;

public class GenreNotFoundException extends RuntimeException {

    public GenreNotFoundException(final String message) {
        super(message);
    }
}