package ru.yandex.practicum.filmorate.exeptions.FilmExeptions;

public class FilmNotFoundException extends RuntimeException {

    public FilmNotFoundException(final String message) {
        super(message);
    }
}