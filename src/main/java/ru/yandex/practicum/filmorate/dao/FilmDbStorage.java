package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeptions.FilmExeptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

@Component("filmDbStorage")
@Repository
public class FilmDbStorage implements FilmStorage {

    //Это должно быть здесь, для работы с БД
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Map<Integer, Film> getAllFilms() {
        return null;
    }

    @Override
    public Film addFilm(Film film) {
        return null;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Film deleteFilm(Film film) {
        return null;
    }

    @Override
    public Film getFilmById(Integer id) {
        String sqlRequest =  "select * " +
        "from PUBLIC.FILM where FILM_ID = " + id;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlRequest);
            // вы заполните данные пользователя в следующем уроке
            Film film = jdbcTemplate.queryForObject(
                    sqlRequest,
                    new RowMapper<Film>() {
                        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                            Film film = new Film(rs.getInt("FILM_ID"),
                                    rs.getString("film_name"),
                                    rs.getString("film_description"),
                                    LocalDate.parse(rs.getString("release_date")),
                                    rs.getInt("film_duration"),
                                    rs.getInt("film_rating"));
                            return film;
                        }
                    });
            return film;
    }
}
