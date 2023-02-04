package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    //@Qualifier("filmDbStorage")
    private FilmStorage filmStorage;

    private final FilmValidation filmValidation;

    @Autowired
    public void setJdbcFilmDAO(@Qualifier("filmDbStorage") FilmDbStorage filmDbStorage) {
        this.filmStorage = filmDbStorage;
    }

    public Map<Integer, Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmById(Integer id) {
        filmValidation.filmIdValidationDB(id);
        return filmStorage.getFilmById(id);
    }

    public void addLike(int id, int userId) {
        filmStorage.getFilmById(id).getLikes().add(userId);
        System.out.println("Hi");
    }

    public void deleteLike(int id, int userId) {
        filmStorage.getFilmById(id).getLikes().remove(userId);
    }

    public Set<Film> getTopFilms(Integer count) {
        Set <Film> topFilms = new TreeSet<>((o1, o2) -> {
            if(o1.getLikes().size() == o2.getLikes().size()) {
                return o1.getId() - o2.getId();

            } else if (o1.getLikes().size() > o2.getLikes().size()) {
                return -1;
        } else {
            return 1;
        }
    });
        topFilms.addAll(filmStorage.getAllFilms().values());
        return topFilms.stream().limit(count).collect(Collectors.toSet());
    }
}
