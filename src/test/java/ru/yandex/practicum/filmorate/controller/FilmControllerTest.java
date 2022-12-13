package ru.yandex.practicum.filmorate.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;

class FilmControllerTest {
    FilmController filmController;
    private final static String FILM_DESCRIPTION_200_CHAR = "Some description that equals 200 char very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-very-vere-very-description.";

    @BeforeEach
    void setUp(){
        filmController = new FilmController();
    }

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
    @DisplayName("Описание фильма 200 символов - ОК")
    public void shouldCreateFilmWithDescriptionSize200Char () {
        Film film = Film.builder()
                .id(1).name("Some name")
                .description(FILM_DESCRIPTION_200_CHAR)
                .releaseDate(LocalDate.of(2020, Month.DECEMBER,1))
                .duration(150).build();
        filmController.addFilm(film);
        assertEquals(1, filmController.getAllFilms().size(), "Фильм не был добавлен в коллекцию.");
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
    @DisplayName("Тест даты релиза — 28 декабря 1895 года - пройдёт валидацию")
    public void shouldAddFilmAfter27_12_1895 () {
        Film film = Film.builder()
                .id(1).name("Some name")
                .description("Some description")
                .releaseDate(LocalDate.of(1895, Month.DECEMBER,28))
                .duration(150).build();
        filmController.addFilm(film);
        assertEquals(1, filmController.getAllFilms().size(), "Фильм не был добавлен в коллекцию.");
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