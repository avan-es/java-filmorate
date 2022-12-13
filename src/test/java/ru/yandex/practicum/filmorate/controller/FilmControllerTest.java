package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exeptions.FilmExeptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController;

    @BeforeEach
    void setUp(){
        filmController = new FilmController();
    }

    @Test
    @DisplayName("Название фильма не может быть пустым")
    public void shouldReturnExceptionOnEmptyName () throws RuntimeException {
        Film film = new Film(1,"","Some description", LocalDate.of(2020, Month.DECEMBER,1),150);
        Throwable thrown = assertThrows(RuntimeException.class, () -> {
            filmController.addFilm(film);
        });
        assertEquals("Название фильма не может быть пустым.", thrown.getMessage(),
                "Ожидалась ошибка валидации названия фильма.");
    }

    @Test
    @DisplayName("Максимальная длина описания для фильма — 200 символов")
    public void shouldReturnExceptionOnLongDescription () throws RuntimeException {
        Film film = new Film(1,"Some name",
                "Some description that equals 201 char very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-description..",
                LocalDate.of(2020, Month.DECEMBER,1),150);
        Throwable thrown = assertThrows(RuntimeException.class, () -> {
            filmController.addFilm(film);
        });
        assertEquals("Максимальная длина описания для фильма — 200 символов.", thrown.getMessage(),
                "Ожидалась ошибка валидации описания фильма.");
    }

    @Test
    @DisplayName("Дата релиза — не раньше 28 декабря 1895 года")
    public void shouldReturnExceptionOnDateBeforeMoveWasFound () throws RuntimeException {
        Film film = new Film(1,"Some name",
                "Some description", LocalDate.of(1895, Month.DECEMBER,27),150);
        Throwable thrown = assertThrows(RuntimeException.class, () -> {
            filmController.addFilm(film);
        });
        assertEquals("Дата релиза введена не корректно.", thrown.getMessage(),
                "Ожидалась ошибка валидации даты фильма.");
    }

    @Test
    @DisplayName("Продолжительность фильма должна быть положительной")
    public void shouldReturnExceptionOnNegativeOrZeroDurationFilm () throws RuntimeException {
        Film filmWithZeroDuration = new Film(1,"Some name",
                "Some description", LocalDate.of(2020, Month.DECEMBER,27),0);
        Throwable thrownForFilmWithZeroDuration = assertThrows(RuntimeException.class, () -> {
            filmController.addFilm(filmWithZeroDuration);
        });
        Film filmWithNegativeDuration = new Film(1,"Some name",
                "Some description", LocalDate.of(2020, Month.DECEMBER,27),-150);
        Throwable thrownForFilmWithNegativeDuration = assertThrows(RuntimeException.class, () -> {
            filmController.addFilm(filmWithNegativeDuration);
        });
        assertAll(
                () -> assertEquals("Продолжительность фильма должна быть положительной.", thrownForFilmWithZeroDuration.getMessage(),
                        "Ожидалась ошибка валидации продолжительности фильма."),
                () -> assertEquals("Продолжительность фильма должна быть положительной.", thrownForFilmWithNegativeDuration.getMessage(),
                "Ожидалась ошибка валидации продолжительности фильма.")
        );

    }

}