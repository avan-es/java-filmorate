package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;


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
            if (o1.getLikes().size() > o2.getLikes().size()) {
                return -1;
        } else {
            return 1;
        }
    });
        topFilms.addAll(filmStorage.getAllFilms().values());
        return topFilms.stream().limit(count).collect(Collectors.toSet());
    }
}
