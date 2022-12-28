package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.FilmExeptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage{

    private final HashMap<Integer, Film> films = new HashMap<>();
    private int idFilmGenerator = 1;

    @Override
    public Film addFilm(Film film) {
        film.setId(idFilmGenerator);
        films.put(idFilmGenerator++, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new FilmValidationException("Фильма с таким ID (" + film.getId() +
                    ") не найден.");
        }
        films.put(film.getId(), film);
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
    public Film getFilmById(Integer id) {
        return films.get(id);
    }

    @Override
    public Map<Integer, Film> getAllFilms() {
        return films;
    }
}
