package ru.yandex.practicum.filmorate.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.FilmValidationException;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.time.Month;

//@RequiredArgsConstructor
@Component("filmValidation")
public class FilmValidation {
    @Qualifier("filmDbStorage")
    private FilmStorage filmStorage;

    public static final LocalDate FILMS_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public FilmValidation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Film filmValidation(Film film) {
        if (film.getName().isEmpty() ||
                film.getName().isBlank()) {
            throw new FilmValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new FilmValidationException("Максимальная длина описания для фильма — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(FILMS_BIRTHDAY) ||
                film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new FilmValidationException("Дата релиза введена не корректно.");
        }
        if (film.getDuration() <= 0) {
            throw new FilmValidationException("Продолжительность фильма должна быть положительной.");
        }
        return film;
    }

    public void filmIdValidation(int id) {
        try {
            filmStorage.getFilmById(id);
        } catch (RuntimeException e){
            throw new NotFoundException(String.format("Фильм с ID %d не найден.", id));
        }
    }

    public void filmIdValidationDB(int id) {
        String sqlRequest =  "select * " +
                "from PUBLIC.FILMS where FILM_ID = " + id;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlRequest);
        if (!filmRows.next()) {
            throw new NotFoundException(String.format("Фильм с ID %d не найден.", id));
        }
    }

}
