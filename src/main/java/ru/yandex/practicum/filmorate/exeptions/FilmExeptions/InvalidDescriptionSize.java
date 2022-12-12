package ru.yandex.practicum.filmorate.exeptions.FilmExeptions;

public class InvalidDescriptionSize extends RuntimeException{

    public InvalidDescriptionSize(final String message) {
        super(message);
    }
}