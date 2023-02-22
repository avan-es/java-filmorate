package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.*;

import static ru.yandex.practicum.filmorate.constants.Constants.FILMS_BIRTHDAY;

@RestController
@RequiredArgsConstructor
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;
    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film){
        return filmService.addFilm(film);
    }

    @GetMapping("/{id}")
    public Film geFilmById(@PathVariable Integer id) {
        return filmService.getFilmById(id);
    }

    @GetMapping
    public Collection<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film){
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId){
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId){
        filmService.deleteLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getTopFilms(@RequestParam(defaultValue = "10", required = false) @Positive Integer count,
                                        @RequestParam(value = "genreId", required = false) @Positive Optional<Integer> genreId,
                                        @RequestParam(value = "year", required = false) @Positive Optional<Integer> year) {
        Map<String, Integer> searchParam = new HashMap<>();
        searchParam.put("count",count);
        if ((genreId.isPresent() && genreId.get() > 0) && (year.isPresent() && year.get() >= FILMS_BIRTHDAY.getYear())) {
            searchParam.put("genre", genreId.get());
            searchParam.put("year", year.get());
            return filmService.getTopFilms(searchParam);
        } else if (genreId.isPresent() && genreId.get() > 0) {
            searchParam.put("genre", genreId.get());
            return filmService.getTopFilms(searchParam);
        } else if (year.isPresent() && year.get() >= FILMS_BIRTHDAY.getYear()) {
            searchParam.put("year", year.get());
            return filmService.getTopFilms(searchParam);
        }
        return filmService.getTopFilms(searchParam);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> searchDirectorsFilms(@PathVariable Integer directorId,
                                           @RequestParam("sortBy") List<String> sortBy) {
        return filmService.searchDirectorsFilms(directorId, sortBy);
    }

    @GetMapping("/search")
    public List<Film> searchFilms(@RequestParam("query") @NotBlank @NotEmpty String query,
                                  @RequestParam("by") List<String> by) {
        return filmService.searchFilms(query, by);
    }

//    @DeleteMapping("/{filmId}")
//    public void deleteFilm(@PathVariable int filmId){
//        filmService.deleteFilm(filmId);
//    }
}
