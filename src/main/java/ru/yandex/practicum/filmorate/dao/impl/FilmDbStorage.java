package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constants.Constants.INDEX_FOR_LIST_WITH_ONE_ELEMENT;

@Component("filmDbStorage")
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film addFilm(Film film) {
        //Вставляем фильм в БД
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO films (film_name, film_description, release_date, film_duration, mpa_id)" +
                            " VALUES (?, ?, ?, ?, ?)",
                    new String[]{"FILM_ID"});
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            return ps;
        }, keyHolder);
        film.setId(Objects.requireNonNull(keyHolder.getKey()).intValue());
        //Заполняем жанры фильма
        if (film.getGenres() != null) {
            ArrayList<Genre> genres = film.getGenres();
            String insertFilmGenre = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
            jdbcTemplate.batchUpdate(insertFilmGenre, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Genre genre = genres.get(i);
                    ps.setInt(1, film.getId());
                    ps.setInt(2, genre.getId());
                }
                @Override
                public int getBatchSize() {
                    return genres.size();
                }
            });
        }
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        String sqlFilm = "SELECT f.film_id, f.film_name, f.film_description, f.release_date, f.film_duration, " +
                "m.mpas_id, m.mpas_name, g.genre_id, g.genre_name " +
        "FROM films f " +
        "LEFT JOIN mpas m ON f.mpa_id = m.mpas_id " +
        "LEFT JOIN films_genres fg ON fg.film_id = f.film_id " +
        "LEFT JOIN genres g ON g.genre_id = fg.genre_id " +
        "WHERE f.film_id = " + id;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlFilm);
        List<Film> film = new ArrayList<>();
        film = jdbcTemplate.query(sqlFilm, new FilmBuilder());
        return film.get(INDEX_FOR_LIST_WITH_ONE_ELEMENT);
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        String sqlFilm = "SELECT f.film_id, f.film_name, f.film_description, f.release_date, f.film_duration, " +
                "m.mpas_id, m.mpas_name, g.genre_id, g.genre_name " +
                "FROM films f " +
                "LEFT JOIN mpas m ON f.mpa_id = m.mpas_id " +
                "LEFT JOIN films_genres fg ON fg.film_id = f.film_id " +
                "LEFT JOIN genres g ON g.genre_id = fg.genre_id ";
        films = jdbcTemplate.query(sqlFilm, new FilmBuilder());
        return films;
    }


    @Override
    public void putLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("INSERT INTO likes (user_id, film_id) VALUES (?, ?)", userId, filmId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("DELETE FROM likes WHERE user_id = ? AND film_id = ?", userId, filmId);

    }

    @Override
    public Set<Film> getTopFilms(Integer limit) {
        String sql = "SELECT " +
                "f.film_id, " +
                "f.film_name, " +
                "f.film_description, " +
                "f.release_date, " +
                "f.film_duration, " +
                "f.mpa_id, " +
                "COUNT(likes.film_id) AS likes_count " +
                "FROM films AS f LEFT JOIN likes ON f.film_id = likes.film_id " +
                "GROUP BY f.film_id " +
                "ORDER BY likes_count DESC " +
                "FETCH FIRST ? ROWS ONLY";
        List<Film> films = jdbcTemplate.query(sql, new Object[]{limit}, (rs) -> {
            List<Film> filmsList = new ArrayList<>();
            while (rs.next()) {
                Film film = getFilmById(rs.getInt("film_id"));
                filmsList.add(film);
            }
            return filmsList;
        });
        return films.stream().collect(Collectors.toSet());
    }


    @Override
    public Film updateFilm(Film film) {
        List<Integer> genresList = new ArrayList<>();
        ArrayList<Genre> uniqueGenres = new ArrayList<>();
        String sqlFilm = "UPDATE films SET film_name = ?, film_description = ?, release_date = ?, " +
                "film_duration = ?, mpa_id = ? WHERE film_id = ?";
        String deleteGenres = "DELETE FROM films_genres WHERE film_id = ?";
        String insertGenres = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlFilm, film.getName(),film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        jdbcTemplate.update(deleteGenres, film.getId());
        if (film.getGenres() == null) {
            return film;
        }
        for (Genre genre : film.getGenres()) {
            if (!genresList.contains(genre.getId())) {
                genresList.add(genre.getId());
                jdbcTemplate.update(insertGenres, film.getId(), genre.getId());
                uniqueGenres.add(genre);
            }
        }
        film.setGenres(uniqueGenres);
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        String sqlRequest =  "DELETE " +
                "from films where film_id = " + film.getId();
        jdbcTemplate.queryForRowSet(sqlRequest);
    }
}
