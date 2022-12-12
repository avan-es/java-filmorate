package ru.yandex.practicum.filmorate.exeptions.FilmExeptions;

public class InvalidNameException extends RuntimeException{

    public InvalidNameException(final String message) {
        super(message);
    }
}
