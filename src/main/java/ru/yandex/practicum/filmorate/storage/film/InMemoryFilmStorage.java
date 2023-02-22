package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.constants.SearchParam;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constants.SearchParam.LIMIT;

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
            throw new NotFoundException("Фильма с таким ID (" + film.getId() +
                    ") не найден.");
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public void deleteFilm(Integer filmId) {
        if (!films.containsKey(filmId)) {
            throw new NotFoundException(String.format("Фильм с ID %d не найден.", filmId));
        }
        films.remove(filmId);
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
    public Set <Film> getTopFilms(Map<SearchParam, Integer> searchParam) {
        Set <Film> topFilms = new TreeSet<>((o1, o2) -> {
            if(o1.getLikes().size() == o2.getLikes().size()) {
                return o1.getId() - o2.getId();

            } else if (o1.getLikes().size() > o2.getLikes().size()) {
                return -1;
            } else {
                return 1;
            }
        });
        topFilms.addAll(getAllFilms());
        return topFilms.stream().limit(searchParam.get(LIMIT)).collect(Collectors.toSet());
    }

    //TODO реализовать метод
    @Override
    public List<Film> searchDirectorsFilms(Integer directorId, List<String> sortBy) {
        return null;
    }

    //TODO реализовать метод
    @Override
    public List<Film> searchFilms(String query, SearchParam type) {
        return null;
    }

    //TODO реализовать метод
    @Override
    public List<Film> getCommonFilms(Integer userId, Integer friendId) {
        return null;
    }

    @Override
    public List<Film> getAllFilms() {
        return films.entrySet()
                .stream()
                .map (e -> e.getValue())
                .collect(Collectors.toList());
    }
}
