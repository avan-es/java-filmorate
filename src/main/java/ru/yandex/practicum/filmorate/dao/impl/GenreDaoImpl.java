package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            return new Genre(
                genreRS.getInt("GENRE_ID"),
                genreRS.getString("GENRE_NAME")
            );
        } else {
            throw new NotFoundException(String.format("Жанр с ID %d не найден.", id));
        }
    }

    @Override
    public List<Genre> getAllGenre() {
        String sql = "SELECT * FROM GENRES ORDER BY GENRE_ID";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<Genre> result = new ArrayList<>();
        list.forEach(m -> {
            Genre g = new Genre(((Integer)m.get("GENRE_ID")), ((String)m.get("GENRE_NAME")));
            result.add(g);
        });
        return result;
    }
}
