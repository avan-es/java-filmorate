package ru.yandex.practicum.filmorate.dao.film;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.constants.SearchParam;
import ru.yandex.practicum.filmorate.dao.genre.GenreMapper;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.constants.Constants.INDEX_FOR_LIST_WITH_ONE_ELEMENT;
import static ru.yandex.practicum.filmorate.constants.FilmsSortBy.FILM_SORT_BY_LIKES;
import static ru.yandex.practicum.filmorate.constants.FilmsSortBy.FILM_SORT_BY_YEAR;
import static ru.yandex.practicum.filmorate.constants.SearchParam.*;

@Component("filmDbStorage")
@Repository
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String BASIC_SQL_REQUEST_FOR_FILM =
            "SELECT f.film_id, f.film_name, f.film_description, f.release_date, f.film_duration, " +
                    "m.mpas_id, m.mpas_name, g.genre_id, g.genre_name, d.director_id, d.director_name, " +
                    "COUNT(DISTINCT likes.USER_ID) AS likes_count " +
                    "FROM films AS f LEFT JOIN likes ON f.film_id = likes.film_id " +
                    "LEFT JOIN mpas m ON f.mpa_id = m.mpas_id " +
                    "LEFT JOIN films_genres fg ON fg.film_id = f.film_id " +
                    "LEFT JOIN genres g ON g.genre_id = fg.genre_id " +
                    "LEFT JOIN films_directors fd ON f.film_id = fd.film_id " +
                    "LEFT JOIN director d ON fd.director_id = d.director_id ";

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
                    "INSERT INTO films (film_name, film_description, release_date, film_duration, mpa_id) " +
                        "VALUES (?, ?, ?, ?, ?)",
                    new String[]{"film_id"});
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
            String insertFilmGenre = "INSERT INTO films_genres (film_id, genre_id) " +
                                     "VALUES (?, ?)";
            jdbcTemplate.batchUpdate(insertFilmGenre, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(@NonNull PreparedStatement ps, int i) throws SQLException {
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
        //Заполняем режиссёров
        if (film.getDirectors() != null) {
            ArrayList<Director> directors = film.getDirectors();
            String insertDirectorsSql = "INSERT INTO films_directors (film_id, director_id) " +
                                        "VALUES (?, ?)";
            jdbcTemplate.batchUpdate(insertDirectorsSql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Director director = directors.get(i);
                    ps.setInt(1, film.getId());
                    ps.setInt(2, director.getId());
                }
                @Override
                public int getBatchSize() {
                    return directors.size();
                }
            });
        }
        return getFilmById(film.getId());
    }

    @Override
    public Film getFilmById(Integer id) {
        String sqlFilm = BASIC_SQL_REQUEST_FOR_FILM +
                         "WHERE f.film_id = " + id +
                         " GROUP BY g.genre_id";
        List<Film> film = jdbcTemplate.query(sqlFilm, new FilmMapper());
        return Objects.requireNonNull(film).get(INDEX_FOR_LIST_WITH_ONE_ELEMENT);
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films;
        String sqlFilm = BASIC_SQL_REQUEST_FOR_FILM +
                "GROUP BY f.film_id, g.genre_id";
        films = jdbcTemplate.query(sqlFilm, new FilmMapper());
        return films;
    }


    @Override
    public void putLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("INSERT INTO likes (user_id, film_id) " +
                                "VALUES (?, ?)", userId, filmId);
    }

    @Override
    public void deleteLike(Integer filmId, Integer userId) {
        jdbcTemplate.update("DELETE FROM likes " +
                                "WHERE user_id = ? AND film_id = ?", userId, filmId);

    }

    @Override
    public Set<Film> getTopFilms(Map<SearchParam, Integer> searchParam) {
        List<Film> films = new ArrayList<>();
        String sql = BASIC_SQL_REQUEST_FOR_FILM;
        if (searchParam.containsKey(YEAR) && searchParam.containsKey(GENRE)) {
            sql = sql +
                    "WHERE EXTRACT(YEAR FROM CAST(f.release_date AS DATE))  = " + searchParam.get(YEAR) +
                    " AND g.genre_id = " + searchParam.get(GENRE);
        } else if (searchParam.containsKey(YEAR)) {
            sql = sql +
                    "WHERE EXTRACT(YEAR FROM CAST(f.release_date AS DATE))  = " + searchParam.get(YEAR);

        } else if (searchParam.containsKey(GENRE)) {
            sql = sql +
                    "WHERE g.genre_id = " + searchParam.get(GENRE);
        }
            sql = sql +
                    " GROUP BY f.film_id, g.genre_id " +
                    "ORDER BY likes_count DESC " +
                    "FETCH FIRST " + searchParam.get(LIMIT) +" ROWS ONLY";

        films = jdbcTemplate.query(sql, new FilmMapper());
        for (Film film: films) {
            film.setGenres(new ArrayList<>(getGenres(film)));
        }
        assert films != null;
        return new HashSet<>(films);
    }


    private List<Genre> getGenres (Film film){
        String sql = "SELECT fg.genre_id, g.genre_name FROM films_genres AS fg JOIN genres AS g ON fg.genre_id = g.genre_id WHERE fg.film_id = ?";
        return jdbcTemplate.query(sql, new GenreMapper(), film.getId()).stream().collect(Collectors.toList());

    }

    @Override
    public List<Film> searchDirectorsFilms(Integer directorId, List<String> sortBy) {
        List<Film> films = new ArrayList<>();
        if (sortBy.contains(FILM_SORT_BY_YEAR.toString())) {
            String sql = (BASIC_SQL_REQUEST_FOR_FILM +
                    "WHERE d.director_id = " + directorId +
                    " GROUP BY f.film_id, g.genre_id " +
                    "ORDER BY f.release_date ASC");
            films = jdbcTemplate.query(sql, new FilmMapper());
        } else if (sortBy.contains(FILM_SORT_BY_LIKES.toString())) {
            String sql = (BASIC_SQL_REQUEST_FOR_FILM +
                    "WHERE d.director_id = " + directorId +
                    " GROUP BY f.film_id, g.genre_id " +
                    "ORDER BY likes_count DESC ");
            films = jdbcTemplate.query(sql, new FilmMapper());
        }
        return films;
    }

    @Override
    public List<Film> searchFilms(String query, SearchParam type) {
        String sql;
        List<Film> result = new ArrayList<>();
        switch (type) {
            case BOTH:
                sql =BASIC_SQL_REQUEST_FOR_FILM +
                        "WHERE (f.film_name ILIKE '%" + query + "%')" +
                        "GROUP BY m.mpas_id " +
                        "UNION " +
                        BASIC_SQL_REQUEST_FOR_FILM +
                        "WHERE (d.director_name ILIKE '%" + query + "%')" +
                        "GROUP BY m.mpas_id " +
                        "ORDER BY film_id DESC;";
                result = jdbcTemplate.query(sql, new FilmMapper());
                System.out.println(result);
                break;
            case TITLE:
                sql = BASIC_SQL_REQUEST_FOR_FILM +
                        "WHERE f.film_name ILIKE '%" + query + "%'";
                result = jdbcTemplate.query(sql, new FilmMapper());
                break;
            case DIRECTOR:
                sql = BASIC_SQL_REQUEST_FOR_FILM +
                        "WHERE d.director_name ILIKE '%" + query + "%'";
                result = jdbcTemplate.query(sql, new FilmMapper());
                break;
        }
        return result;
    }

    @Override
    public Film updateFilm(Film film) {
        List<Integer> genresList = new ArrayList<>();
        List<Integer> directorList = new ArrayList<>();
        String sqlFilm = "UPDATE films SET film_name = ?, film_description = ?, release_date = ?, " +
                         "film_duration = ?, mpa_id = ? " +
                         "WHERE film_id = ?";
        jdbcTemplate.update(sqlFilm, film.getName(),film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        String deleteGenres = "DELETE " +
                              "FROM films_genres " +
                              "WHERE film_id = ?";
        jdbcTemplate.update(deleteGenres, film.getId());
        if (film.getGenres() != null) {
            String insertGenres = "INSERT INTO films_genres (film_id, genre_id) " +
                    "VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                if (!genresList.contains(genre.getId())) {
                    genresList.add(genre.getId());
                    jdbcTemplate.update(insertGenres, film.getId(), genre.getId());
                }
            }
        }
        String deleteDirector = "DELETE " +
                                "FROM films_directors " +
                                "WHERE film_id = ?";
        jdbcTemplate.update(deleteDirector, film.getId());
        if (film.getDirectors() != null) {
            String insertDirector = "INSERT INTO films_directors (film_id, director_id) " +
                    "VALUES (?, ?)";
            for (Director director : film.getDirectors()) {
                if (!directorList.contains(director.getId())) {
                    directorList.add(director.getId());
                    jdbcTemplate.update(insertDirector, film.getId(), director.getId());
                }
            }
        }
        return getFilmById(film.getId());
    }

    @Override
    public void deleteFilm(Integer filmId) {
        String sqlRequest =  "DELETE " +
                             "FROM films " +
                             "WHERE film_id = " + filmId;
        jdbcTemplate.update(sqlRequest);
    }
}
