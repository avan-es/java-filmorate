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
            throw new NotFoundException(String.format("Жанр с ID %d не найден.", id));
        }
    }

    @Override
    public List<Genre> getAllGenre() {
        List<Genre> result = new ArrayList<>();
        String sql = "SELECT * FROM GENRES ORDER BY GENRE_ID";
        result = jdbcTemplate.query(sql, new ResultSetExtractor<List<Genre>>() {
            @Override
            public List<Genre> extractData(ResultSet rs) throws SQLException, DataAccessException {
                List<Genre> listGere = new ArrayList<>();
                while (rs.next()){
                    Genre genre = new Genre();
                    genre.setId(rs.getInt("GENRE_ID"));
                    genre.setName(rs.getString("GENRE_NAME"));
                    listGere.add(genre);
                }
                return listGere;
            }
        });
        return result;
    }
}
