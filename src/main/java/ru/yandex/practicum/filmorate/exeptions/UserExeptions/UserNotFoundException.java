package ru.yandex.practicum.filmorate.exeptions.UserExeptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(final String message) {
        super(message);
    }
}
