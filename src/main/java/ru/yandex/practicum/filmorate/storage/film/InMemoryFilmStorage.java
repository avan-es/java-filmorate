package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.FilmExeptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.FilmExeptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Component("inMemoryFilmStorage")
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
            throw new FilmNotFoundException("Фильм с ID: " + film.getId() +
                    " не найден.");
        }
        films.remove(film.getId());
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        return films.get(id);
    }

    @Override
    public void putLike(Integer filmId, Integer userId) {
        films.get(filmId).getLikes().add(userId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        films.get(filmId).getLikes().remove(userId);
    }

    @Override
    public Set <Film> getTopFilms(Integer limit) {
        Set <Film> topFilms = new TreeSet<>((o1, o2) -> {
            if(o1.getLikes().size() == o2.getLikes().size()) {
                return o1.getId() - o2.getId();

            } else if (o1.getLikes().size() > o2.getLikes().size()) {
                return -1;
            } else {
                return 1;
            }
        });
        topFilms.addAll(getAllFilms().values());
        return topFilms.stream().limit(limit).collect(Collectors.toSet());
    }

    @Override
    public Map<Integer, Film> getAllFilms() {
        return films;
    }
}
