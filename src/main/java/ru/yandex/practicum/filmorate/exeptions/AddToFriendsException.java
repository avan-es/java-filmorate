package ru.yandex.practicum.filmorate.exeptions;

public class AddToFriendsException extends RuntimeException {

    public AddToFriendsException(final String message) {
        super(message);
    }
}