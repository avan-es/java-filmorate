package ru.yandex.practicum.filmorate.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exeptions.GenreException.GenreNotFoundException;

@Component("genreValidation")
public class GenreValidation {
    @Qualifier("genreDaoImpl")
    private GenreDao genreDao;

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public GenreValidation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void genreIdValidationDB(Integer id) {
        String sqlRequest =  "select * " +
                "from PUBLIC.GENRES where GENRE_ID = " + id;
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlRequest);
        if (!genreRows.next()) {
            throw new GenreNotFoundException("Жанр с ID: " + id +" не найден.");
        }
    }
}
