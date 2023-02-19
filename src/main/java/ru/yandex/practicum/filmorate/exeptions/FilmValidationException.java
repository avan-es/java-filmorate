package ru.yandex.practicum.filmorate.exeptions;

public class FilmValidationException extends RuntimeException {

    public FilmValidationException(final String message) {
        super(message);
    }
}