package ru.yandex.practicum.filmorate.exeptions.MpaException;

public class MpaNotFoundException extends RuntimeException {

    public MpaNotFoundException(final String message) {
        super(message);
    }
}
