package ru.yandex.practicum.filmorate.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.genre.GenreDao;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;

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
        String sqlRequest =  String.format("select * " +
                "from PUBLIC.GENRES where EXISTS(SELECT 1 FROM PUBLIC.GENRES where GENRE_ID = %d);",id);
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(sqlRequest);
        if (!genreRows.next()) {
            throw new NotFoundException(String.format("Жанр с ID %d не найден.", id));
        }
    }
}
