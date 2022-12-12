package ru.yandex.practicum.filmorate.exeptions.UserExeptions;

public class UserValidationException extends RuntimeException {

    public UserValidationException(final String message) {
        super(message);
    }
}