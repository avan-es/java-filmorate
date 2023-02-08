package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validation.FilmValidation;
import ru.yandex.practicum.filmorate.validation.GenreValidation;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private FilmStorage filmStorage;

    private final FilmValidation filmValidation;

    private final GenreValidation genreValidation;
    private final UserValidation userValidation;

    @Autowired
    public void setJdbcFilmDAO(@Qualifier("filmDbStorage") FilmDbStorage filmDbStorage) {
        this.filmStorage = filmDbStorage;
    }

    public Film addFilm(Film film) {
        filmValidation.filmValidation(film);
        if (film.getGenres() != null) {
            for (int i = 1; i <= film.getGenres().size(); i++) {
                genreValidation.genreIdValidationDB(i);
            };
        }
        return filmStorage.addFilm(film);
    }

    public Film getFilmById(Integer id) {
        filmValidation.filmIdValidationDB(id);
        return filmStorage.getFilmById(id);
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film updateFilm(Film film) {
        filmValidation.filmIdValidationDB(film.getId());
        return filmStorage.updateFilm(film);
    }

    public void addLike(int id, int userId) {
        filmValidation.filmIdValidationDB(id);
        userValidation.userIdValidationDB(userId);
        filmStorage.putLike(id, userId);
    }

    public void deleteLike(int id, int userId) {
        filmValidation.filmIdValidationDB(id);
        userValidation.userIdValidationDB(userId);
        filmStorage.deleteLike(id, userId);
    }

    public Set<Film> getTopFilms(Integer count) {
        return filmStorage.getTopFilms(count);
    }
}
