package ru.yandex.practicum.filmorate.exeptions.UserExeptions;

public class AddToFriendsException extends RuntimeException {

    public AddToFriendsException(final String message) {
        super(message);
    }
}