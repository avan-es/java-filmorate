package ru.yandex.practicum.filmorate.storage.film;
import ru.yandex.practicum.filmorate.constants.SearchBy;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface FilmStorage {

    List<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

//    void deleteFilm (Integer filmId);

    Film getFilmById(Integer id);

    void putLike (Integer filmId, Integer userId);

    void deleteLike (Integer filmId, Integer userId);

    Set<Film> getTopFilms(Map<String, Integer> searchParam);

    List<Film> searchDirectorsFilms(Integer directorId, List<String> sortBy);

    List<Film> searchFilms(String query, SearchBy type);
}
