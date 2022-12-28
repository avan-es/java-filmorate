package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.validation.FilmValidation;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    private final FilmValidation filmValidation;

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms().values();
    }

    @GetMapping("/{id}")
    public Film geFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film){
        filmValidation.filmValidation(film);
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film){
        filmValidation.filmValidation(film);
        return filmService.updateFilm(film);
    }

}
