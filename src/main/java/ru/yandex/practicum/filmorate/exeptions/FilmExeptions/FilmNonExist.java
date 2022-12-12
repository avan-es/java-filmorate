package ru.yandex.practicum.filmorate.exeptions.FilmExeptions;

public class FilmNonExist extends RuntimeException {

    public FilmNonExist(final String message) {
        super(message);
    }
}