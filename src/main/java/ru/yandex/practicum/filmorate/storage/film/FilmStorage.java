package ru.yandex.practicum.filmorate.storage.film;
import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;

public interface FilmStorage {

    Collection<Film> getAllFilms();

    Film filmValidation(Film film);

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film deleteFilm (Film film);
}
