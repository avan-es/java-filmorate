package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exeptions.GenreException.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exeptions.MpaException.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

@Component("genreDaoImpl")
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Genre getGenreById(Integer id) {
        String sql = "SELECT * FROM GENRES WHERE GENRE_ID = ?";
        SqlRowSet genreRS = jdbcTemplate.queryForRowSet(sql, id);
        if (genreRS.next()) {
            Genre genre = new Genre();
            genre.setId(genreRS.getInt("GENRE_ID"));
            genre.setName(genreRS.getString("GENRE_NAME"));
            return genre;
        } else {
            throw new GenreNotFoundException("Жанр с ID: " + id +" не найден.");
        }
    }

    @Override
    public List<Genre> getAllGenre() {
        List<Genre> result = new ArrayList<>();
        String sql = "SELECT GENRE_ID FROM GENRES ORDER BY GENRE_ID";
        List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class);
        for (Integer id : ids) {
            result.add(getGenreById(id));
        }
        return result;
    }
}
