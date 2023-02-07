package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exeptions.FilmExeptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.FilmExeptions.FilmValidationException;
import ru.yandex.practicum.filmorate.exeptions.GenreException.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.MpaException.MpaNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.AddToFriendsException;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.UserValidationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        return new ErrorResponse(
                "Ошибка в запросе.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserValidationException(final UserValidationException e) {
        return new ErrorResponse(
                "Ошибка в веденных данных.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAddToFriendsException(final AddToFriendsException e) {
        return new ErrorResponse(
                "Ошибка при добавлении в друзья.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundException(final FilmNotFoundException e) {
        return new ErrorResponse(
                "Ошибка в веденных данных.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleFilmValidationFoundException(final FilmValidationException e) {
        return new ErrorResponse(
                "Ошибка в ведённых данных.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGenreNotFoundException(final GenreNotFoundException e) {
        return new ErrorResponse(
                "Ошибка с ID жанра фильма.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleMpaNotFoundException(final MpaNotFoundException e) {
        return new ErrorResponse(
                "Ошибка с ID возрастного ограничения",
                e.getMessage());
    }

}
