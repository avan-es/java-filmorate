package ru.yandex.practicum.filmorate.storage.film;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Map;

public interface FilmStorage {

    Map<Integer, Film> getAllFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film deleteFilm (Film film);

    Film getFilmById(Integer id);
}
