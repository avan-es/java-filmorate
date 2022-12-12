package ru.yandex.practicum.filmorate.exeptions.FilmExeptions;

public class InvalidReleaseDate extends RuntimeException{

    public InvalidReleaseDate(final String message) {
        super(message);
    }
}
