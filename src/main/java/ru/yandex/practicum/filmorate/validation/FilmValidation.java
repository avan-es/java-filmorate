package ru.yandex.practicum.filmorate.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.FilmExeptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.FilmExeptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.time.Month;

@RequiredArgsConstructor
@Component
public class FilmValidation {
    private final FilmStorage filmStorage;

    public static final LocalDate FILMS_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);


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
        if (!filmStorage.getAllFilms().containsKey(id)) {
            throw new FilmNotFoundException("Фильм с ID: " + id +" не найден.");
        }
    }

}
