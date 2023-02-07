package ru.yandex.practicum.filmorate.exeptions;

public class UserValidationException extends RuntimeException {

    public UserValidationException(final String message) {
        super(message);
    }
}