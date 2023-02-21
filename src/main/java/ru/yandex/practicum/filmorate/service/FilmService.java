package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.constants.SearchBy;
import ru.yandex.practicum.filmorate.dao.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.validation.DirectorValidation;
import ru.yandex.practicum.filmorate.validation.FilmValidation;
import ru.yandex.practicum.filmorate.validation.GenreValidation;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.*;

import static ru.yandex.practicum.filmorate.constants.FilmsSortBy.LIKES;
import static ru.yandex.practicum.filmorate.constants.FilmsSortBy.YEAR;
import static ru.yandex.practicum.filmorate.constants.SearchBy.DIRECTOR;
import static ru.yandex.practicum.filmorate.constants.SearchBy.TITLE;

@Service
@RequiredArgsConstructor
public class FilmService {
    private FilmStorage filmStorage;

    private final FilmValidation filmValidation;

    private final GenreValidation genreValidation;
    private final UserValidation userValidation;
    private final DirectorValidation directorValidation;

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

    public List<Film> searchDirectorsFilms(Integer directorId, List<String> sortBy) {
        directorValidation.directorIDValidation(directorId);
        sortBy.replaceAll(String::toUpperCase);
        if (!sortBy.contains(YEAR.toString()) && !sortBy.contains(LIKES.toString())) {
            throw new NotFoundException("Сортировка фильмов режиссёра возможна только по годам или лайкам.");
        }
        return filmStorage.searchDirectorsFilms(directorId, sortBy);
    }

    public List<Film> searchFilms(String query, List<String> by) {
        by.replaceAll(String::toUpperCase);
        if (by.contains(DIRECTOR.toString()) && by.contains(TITLE.toString())) {
            return filmStorage.searchFilms(query, SearchBy.BOTH);
        } else if (by.contains(TITLE.toString())) {
            return filmStorage.searchFilms(query, TITLE);
        } else if (by.contains(DIRECTOR.toString())) {
            return filmStorage.searchFilms(query, DIRECTOR);
        }
        throw new NotFoundException("Поля для поиска не заданы. Поиск возможен только по названию и/или режиссёру.");
    }

//    public void deleteFilm(int filmId) {
//        filmValidation.filmIdValidationDB(filmId);
//        filmStorage.deleteFilm(filmId);
//    }
}
