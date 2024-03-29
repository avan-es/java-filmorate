package ru.yandex.practicum.filmorate.storage.film;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmStorage {

    List<Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    void deleteFilm (Film film);

    Film getFilmById(Integer id);

    void putLike (Integer filmId, Integer userId);

    void deleteLike (Integer filmId, Integer userId);

    Set<Film> getTopFilms(Integer limit);
}
