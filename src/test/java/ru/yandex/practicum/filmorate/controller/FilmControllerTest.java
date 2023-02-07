package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.time.LocalDate;
import java.time.Month;

@SpringBootTest
class FilmControllerTest {
    @Autowired
    FilmController filmController;
    @Autowired
    FilmService filmService;
    @Autowired
    InMemoryFilmStorage filmStorage;
    @Autowired
    FilmValidation filmValidation;
    private final static String FILM_DESCRIPTION_200_CHAR = "Some description that equals 200 char very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-description.";


    @Test
    @DisplayName("Название фильма не может быть пустым")
    public void shouldReturnExceptionOnEmptyName () throws RuntimeException {
        Film film = Film.builder()
                .id(1).name("")
                .description("Some description")
                .releaseDate(LocalDate.of(2020, Month.DECEMBER,1))
                .duration(150).build();
        Throwable thrown = assertThrows(RuntimeException.class, () -> {
            filmController.addFilm(film);
        });
        assertEquals("Название фильма не может быть пустым.", thrown.getMessage(),
                "Ожидалась ошибка валидации названия фильма.");
    }

    @Test
    @DisplayName("Максимальная длина описания для фильма — 200 символов")
    public void shouldReturnExceptionOnDescriptionSize201 () throws RuntimeException {
        Film film = Film.builder()
                .id(1).name("Some name")
                .description(FILM_DESCRIPTION_200_CHAR + ".")
                .releaseDate(LocalDate.of(2020, Month.DECEMBER,1))
                .duration(150).build();
        Throwable thrown = assertThrows(RuntimeException.class, () -> {
            filmController.addFilm(film);
        });
        assertEquals("Максимальная длина описания для фильма — 200 символов.", thrown.getMessage(),
                "Ожидалась ошибка валидации описания фильма.");
    }


    @Test
    @DisplayName("Тест даты релиза — 27 декабря 1895 года - не пройдёт валидацию")
    public void shouldReturnExceptionOnDateBefore27_12_1895 () throws RuntimeException {
        Film film = Film.builder()
                .id(1).name("Some name")
                .description("Some description")
                .releaseDate(LocalDate.of(1895, Month.DECEMBER,27))
                .duration(150).build();
        Throwable thrown = assertThrows(RuntimeException.class, () -> {
            filmController.addFilm(film);
        });
        assertEquals("Дата релиза введена не корректно.", thrown.getMessage(),
                "Ожидалась ошибка валидации даты фильма.");
    }


    @Test
    @DisplayName("Продолжительность фильма должна быть положительной")
    public void shouldReturnExceptionOnNegativeOrZeroDurationFilm () throws RuntimeException {
        Film filmWithZeroDuration = Film.builder()
                .id(1).name("Some description")
                .description("Some description")
                .releaseDate(LocalDate.of(2020, Month.DECEMBER,1))
                .duration(0).build();
        Throwable thrownForFilmWithZeroDuration = assertThrows(RuntimeException.class, () -> {
            filmController.addFilm(filmWithZeroDuration);
        });
        Film filmWithNegativeDuration = Film.builder()
                .id(1).name("Some description")
                .description("Some description")
                .releaseDate(LocalDate.of(2020, Month.DECEMBER,1))
                .duration(-150).build();
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