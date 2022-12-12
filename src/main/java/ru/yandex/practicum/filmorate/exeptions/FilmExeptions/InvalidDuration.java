package ru.yandex.practicum.filmorate.exeptions.FilmExeptions;

public class InvalidDuration extends RuntimeException {

    public InvalidDuration(final String message) {
        super(message);
    }
}
