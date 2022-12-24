package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.FilmExeptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int idFilmGenerator = 1;
    private final LocalDate FILMS_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);

    @Override
    public Film addFilm(Film film) {
        Film validFilm = filmValidation(film);
        validFilm.setId(idFilmGenerator);
        films.put(idFilmGenerator++, validFilm);
        return validFilm;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmValidationException("Фильма с таким ID (" + film.getId() +
                    ") не найден.");
        }
        Film validFilm = filmValidation(film);
        films.put(validFilm.getId(), validFilm);
        return validFilm;
    }

    @Override
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

    @Override
    public Film deleteFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmValidationException("Фильма с таким ID (" + film.getId() +
                    ") не найден.");
        }
        films.remove(film.getId());
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }
}
