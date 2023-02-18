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


    //Это должно быть здесь, для работы с БД
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
                    "INSERT INTO FILMS (FILM_NAME, FILM_DESCRIPTION, RELEASE_DATE, FILM_DURATION, MPA_ID)" +
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
        String sqlFilm = "SELECT f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.RELEASE_DATE, f.FILM_DURATION, m.MPAS_ID, m.MPAS_NAME, g.GENRE_ID, g.GENRE_NAME " +
        "FROM FILMS F " +
        "LEFT JOIN MPAS m ON f.MPA_ID = m.MPAS_ID " +
        "LEFT JOIN FILMS_GENRES fg ON fg.FILM_ID = f.FILM_ID " +
        "LEFT JOIN GENRES g ON g.GENRE_ID = fg.GENRE_ID " +
        "WHERE F.FILM_ID = " + id;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlFilm);
        List<Film> film = new ArrayList<>();
        film = jdbcTemplate.query(sqlFilm, new FilmBuilder());
        return film.get(INDEX_FOR_LIST_WITH_ONE_ELEMENT);
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = new ArrayList<>();
        String sqlFilm = "SELECT f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.RELEASE_DATE, f.FILM_DURATION, m.MPAS_ID, m.MPAS_NAME, g.GENRE_ID, g.GENRE_NAME " +
                "FROM FILMS F " +
                "LEFT JOIN MPAS m ON f.MPA_ID = m.MPAS_ID " +
                "LEFT JOIN FILMS_GENRES fg ON fg.FILM_ID = f.FILM_ID " +
                "LEFT JOIN GENRES g ON g.GENRE_ID = fg.GENRE_ID ";
        films = jdbcTemplate.query(sqlFilm, new FilmBuilder());
        return films;
    }


    @Override
    public void putLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("INSERT INTO LIKES (USER_ID, FILM_ID) VALUES (?, ?)", userId, filmId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("DELETE FROM LIKES WHERE USER_ID = ? AND FILM_ID = ?", userId, filmId);

    }

    @Override
    public Set<Film> getTopFilms(Integer limit) {
        String sql = "SELECT " +
                "F.FILM_ID, " +
                "F.FILM_NAME, " +
                "F.FILM_DESCRIPTION, " +
                "F.RELEASE_DATE, " +
                "F.FILM_DURATION, " +
                "F.MPA_ID, " +
                "COUNT(LIKES.FILM_ID) AS LIKES_COUNT " +
                "FROM FILMS AS F LEFT JOIN LIKES ON F.FILM_ID = LIKES.FILM_ID " +
                "GROUP BY F.FILM_ID " +
                "ORDER BY LIKES_COUNT DESC " +
                "FETCH FIRST ? ROWS ONLY";
        List<Film> films = jdbcTemplate.query(sql, new Object[]{limit}, (rs) -> {
            List<Film> filmsList = new ArrayList<>();
            while (rs.next()) {
                Film film = getFilmById(rs.getInt("FILM_ID"));
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
        String sqlFilm = "UPDATE FILMS SET FILM_NAME = ?, FILM_DESCRIPTION = ?, RELEASE_DATE = ?, " +
                "FILM_DURATION = ?, MPA_ID = ? WHERE FILM_ID = ?";
        String deleteGenres = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
        String insertGenres = "INSERT INTO films_genres (FILM_ID, GENRE_ID) VALUES (?, ?)";
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
                "from PUBLIC.FILM where FILM_ID = " + film.getId();
        jdbcTemplate.queryForRowSet(sqlRequest);
    }
}
