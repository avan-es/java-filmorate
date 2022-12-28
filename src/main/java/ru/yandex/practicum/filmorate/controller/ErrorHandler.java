package ru.yandex.practicum.filmorate.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.practicum.filmorate.exeptions.FilmExeptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.UserExeptions.UserValidationException;

@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        return new ErrorResponse(
                "Ошибка в ведённых данных.",
                e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserValidationException(final UserValidationException e) {
        return new ErrorResponse(
                "Ошибка в одном из полей.",
                e.getMessage());
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleFilmNotFoundException(final FilmNotFoundException e) {
        return new ErrorResponse(
                "Ошибка в ведённых данных.",
                e.getMessage());
    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleIncorrectParameterException(final IncorrectParameterException e) {
//        return new ErrorResponse(
//                String.format("Ошибка с полем \"%s\".", e.getParameter())
//        );
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public ErrorResponse handleInvalidEmailException(final InvalidEmailException e) {
//        return new ErrorResponse(
//                e.getMessage()
//        );
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handlePostNotFoundException(final PostNotFoundException e) {
//        return new ErrorResponse(
//                e.getMessage()
//        );
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
//        return new ErrorResponse(
//                e.getMessage()
//        );
//    }
//
//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.CONFLICT)
//    public ErrorResponse handlePostNotFoundException(final UserAlreadyExistException e) {
//        return new ErrorResponse(
//                e.getMessage()
//        );
//    }

//    @ExceptionHandler
//    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
//    public ErrorResponse handleThrowable(final Throwable e) {
//        return new ErrorResponse(
//                "Произошла непредвиденная ошибка."
//        );
//    }
}
