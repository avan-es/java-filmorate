package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.validation.GenreValidation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private GenreDao genreDao;

    private final GenreValidation genreValidation;

    @Autowired
    public void setJdbcGenreDao(@Qualifier("genreDaoImpl") GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public List<Genre> getGenres() {
        return genreDao.getAllGenre();
    }

    public Genre getGenreById(Integer id) {
        genreValidation.genreIdValidationDB(id);
        return genreDao.getGenreById(id);

    }
}
