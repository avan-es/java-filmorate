package ru.yandex.practicum.filmorate.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constance.Constance;
import ru.yandex.practicum.filmorate.exeptions.FilmExeptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

@RequiredArgsConstructor
@Component
public class FilmValidation {
    private final FilmStorage filmStorage;

    public Film filmValidation(Film film) {
        if (film.getName().isEmpty() ||
                film.getName().isBlank()) {
            throw new FilmValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw new FilmValidationException("Максимальная длина описания для фильма — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(Constance.FILMS_BIRTHDAY) ||
                film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new FilmValidationException("Дата релиза введена не корректно.");
        }
        if (film.getDuration() <= 0) {
            throw new FilmValidationException("Продолжительность фильма должна быть положительной.");
        }
        return film;
    }

}
