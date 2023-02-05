package ru.yandex.practicum.filmorate.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeptions.FilmExeptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

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
    public Map<Integer,Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        String sqlRequest =  "select * from PUBLIC.FILMS";
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlRequest);
        films =  jdbcTemplate.query(sqlRequest, (rs, rowNum) -> makeFilm(rs));
        HashMap<Integer, Film> filmsMap = new HashMap<>();
        for (Film film: films) {
            filmsMap.put(film.getId(), film);
        }
        return filmsMap;
    }


    @Override
    public Film addFilm(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement("insert into FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, FILM_DURATION, FILM_GENRE_ID, MPA_ID)" +
                            " VALUES (?, ?, ?, ?, ?)",
                    new String[]{"FILM_ID"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, Integer.parseInt(film.getGenre()));
            //ps.setInt(6, Integer.parseInt(film.getGenre()));
            return ps;
        }, keyHolder);

        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        return null;
    }

    @Override
    public Film deleteFilm(Film film) {
        String sqlRequest =  "DELETE " +
                "from PUBLIC.FILM where FILM_ID = " + film.getId();
        jdbcTemplate.queryForRowSet(sqlRequest);
        return null;
    }

    @Override
    public Film getFilmById(Integer id) {
        String sqlRequest =  "select * " +
        "from PUBLIC.FILMS where FILM_ID = " + id;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlRequest);
        return jdbcTemplate.queryForObject(
                sqlRequest, new RowMapper<Film>() {
                    public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
                        Film film = makeFilm(rs);
                        return film;
                    }
                });
    }

    private Film makeFilm(ResultSet rs) throws SQLException {
        Film film = new Film(rs.getInt("FILM_ID"),
                rs.getString("film_name"),
                rs.getString("film_description"),
                LocalDate.parse(rs.getString("release_date")),
                rs.getInt("film_duration"),
                rs.getString("film_genre_id"),
                rs.getString("MPA_ID"));
        return film;
    }
}
