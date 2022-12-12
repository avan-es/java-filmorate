package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.FilmExeptions.*;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final HashMap<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private int idFilmGenerator = 1;
    private final LocalDate FILMS_BIRTHDAY = LocalDate.of(1895, Month.DECEMBER, 28);

    @GetMapping
    public Collection<Film> getAllFilms() {
        log.info("Получен GET запрос к films");
        return films.values();
    }

    @PostMapping
    public Film addFilm(@RequestBody Film film){
        Film validFilm = filmValidation(film);
        validFilm.setId(idFilmGenerator);
        films.put(idFilmGenerator++, validFilm);
        return validFilm;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film){
        if (!films.containsKey(film.getId())) {
            throw new FilmNonExist("Фильма с таким ID (" + film.getId() +
                    ") не найден.");
        }
        Film validFilm = filmValidation(film);
        films.put(validFilm.getId(), validFilm);
        return validFilm;
    }

    private Film filmValidation (Film film){
        if (film.getName().isEmpty() ||
                film.getName().isBlank()) {
            throw new InvalidNameException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200) {
            throw  new InvalidDescriptionSize("Максимальная длина описания для фильма — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(FILMS_BIRTHDAY) ||
                film.getReleaseDate().isAfter(LocalDate.now())) {
            throw new InvalidReleaseDate("Дата релиза введена не корректно.");
        }
        if (film.getDuration() <= 0) {
            throw new InvalidDuration("Продолжительность фильма должна быть положительной.");
        }
        return film;
    }

}
