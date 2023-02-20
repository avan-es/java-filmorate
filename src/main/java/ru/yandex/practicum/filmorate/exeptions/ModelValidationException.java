package ru.yandex.practicum.filmorate.exeptions;

public class ModelValidationException extends RuntimeException{
    public ModelValidationException(final String message) {
        super(message);
    }
}
