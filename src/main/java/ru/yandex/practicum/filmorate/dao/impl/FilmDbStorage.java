package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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
            List<Genre> genres = List.of(film.getGenres());
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
        Film film = new Film();
        String sqlFilm = "SELECT f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.RELEASE_DATE, f.FILM_DURATION, m.MPAS_ID, m.MPAS_NAME, g.GENRE_ID, g.GENRE_NAME " +
        "FROM FILMS F " +
        "LEFT JOIN MPAS m ON f.MPA_ID = m.MPAS_ID " +
        "LEFT JOIN FILMS_GENRES fg ON fg.FILM_ID = f.FILM_ID " +
        "LEFT JOIN GENRES g ON g.GENRE_ID = fg.GENRE_ID " +
        "WHERE F.FILM_ID = " + id;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlFilm);
        return makeFilm(filmRows);
    }

    private Film makeFilm (SqlRowSet filmRows){
        Film film = new Film();
        List<Genre> genres = new ArrayList<>();
        while (filmRows.next()){
            film.setId(filmRows.getInt("FILM_ID"));
            film.setName(filmRows.getString("FILM_NAME"));
            film.setDescription(filmRows.getString("FILM_DESCRIPTION"));
            film.setReleaseDate(LocalDate.parse(filmRows.getString("RELEASE_DATE")));
            film.setDuration(filmRows.getInt("FILM_DURATION"));
            if (filmRows.getInt("GENRE_ID") != 0){
                Genre genre = new Genre();
                genre.setId(filmRows.getInt("GENRE_ID"));
                genre.setName(filmRows.getString("GENRE_NAME"));
                genres.add(genre);
            }
            Mpa mpa = new Mpa();
            mpa.setId(filmRows.getInt("MPAS_ID"));
            mpa.setName(filmRows.getString("MPAS_NAME"));
            film.setMpa(mpa);
        }
        film.setGenres(genres.toArray(new Genre[genres.size()]));
        return film;
    }

    private Film makeFilmRs (ResultSet rs) throws SQLException {
        Film film = new Film();
        List<Genre> genres = new ArrayList<>();

            film.setId(rs.getInt("FILM_ID"));
            film.setName(rs.getString("FILM_NAME"));
            film.setDescription(rs.getString("FILM_DESCRIPTION"));
            film.setReleaseDate(LocalDate.parse(rs.getString("RELEASE_DATE")));
            film.setDuration(rs.getInt("FILM_DURATION"));
            if (rs.getInt("GENRE_ID") != 0){
                Genre genre = new Genre();
                    genre.setId(rs.getInt("GENRE_ID"));
                    genre.setName(rs.getString("GENRE_NAME"));
                    genres.add(genre);
            }
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("MPAS_ID"));
            mpa.setName(rs.getString("MPAS_NAME"));
            film.setMpa(mpa);
        film.setGenres(genres.toArray(new Genre[genres.size()]));
        return film;
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
    public Map<Integer,Film> getAllFilms() {
        HashMap<Integer, Film> films = new HashMap<>();
        String sqlRequest =  "select FILM_ID from PUBLIC.FILMS";
        List<Integer> filmsId = jdbcTemplate.queryForList(sqlRequest, Integer.class);
        String sqlFilm = "SELECT f.FILM_ID, f.FILM_NAME, f.FILM_DESCRIPTION, f.RELEASE_DATE, f.FILM_DURATION, m.MPAS_ID, m.MPAS_NAME, g.GENRE_ID, g.GENRE_NAME " +
                "FROM FILMS F " +
                "LEFT JOIN MPAS m ON f.MPA_ID = m.MPAS_ID " +
                "LEFT JOIN FILMS_GENRES fg ON fg.FILM_ID = f.FILM_ID " +
                "LEFT JOIN GENRES g ON g.GENRE_ID = fg.GENRE_ID ";
        films = jdbcTemplate.query(sqlFilm, new ResultSetExtractor<HashMap>() {
            @Override
            public HashMap extractData(ResultSet rs) throws SQLException, DataAccessException {
                HashMap<Integer, Film> mapRet = new HashMap<>();
                while (rs.next()){
                    Film film  = makeFilmRs(rs);
                    mapRet.put(film.getId(),film);
                }
                return mapRet;
            }
        });

        System.out.println("Hi");
        return films;
    }

    @Override
    public Film updateFilm(Film film) {
        List<Integer> genresList = new ArrayList<>();
        List<Genre> uniqueGenres = new ArrayList<>();
        String sqlFilm = "UPDATE FILMS SET FILM_NAME = ?, FILM_DESCRIPTION = ?, RELEASE_DATE = ?, " +
                "FILM_DURATION = ?, MPA_ID = ? WHERE FILM_ID = ?";
        String deleteGenres = "DELETE FROM FILMS_GENRES WHERE FILM_ID = ?";
        String insertGenres = "INSERT INTO films_genres (FILM_ID, GENRE_ID) VALUES (?, ?)";
        jdbcTemplate.update(sqlFilm, film.getName(),film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        jdbcTemplate.update(deleteGenres, film.getId());
        String mpaName = jdbcTemplate.queryForObject("SELECT MPAS_NAME FROM MPAS WHERE MPAS_ID =?",
                new Object[]{film.getMpa().getId()}, String.class);
        film.getMpa().setName(mpaName);
        if (film.getGenres() == null) {
            return film;
        }
        for (Genre genre : film.getGenres()) {
            if (!genresList.contains(genre.getId())) {
                String genreName = jdbcTemplate.queryForObject("SELECT GENRE_NAME FROM GENRES WHERE GENRE_ID =?",
                        new Object[]{genre.getId()}, String.class);
                genresList.add(genre.getId());
                jdbcTemplate.update(insertGenres, film.getId(), genre.getId());
                genre.setName(genreName);
                uniqueGenres.add(genre);
            }
        }
        film.setGenres(uniqueGenres.toArray(new Genre[0]));
        return film;
    }

    @Override
    public Film deleteFilm(Film film) {
        String sqlRequest =  "DELETE " +
                "from PUBLIC.FILM where FILM_ID = " + film.getId();
        jdbcTemplate.queryForRowSet(sqlRequest);
        return null;
    }
}
