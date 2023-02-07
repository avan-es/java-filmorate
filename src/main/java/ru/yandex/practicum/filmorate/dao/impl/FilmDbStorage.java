package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.PreparedStatement;
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
            String insertFilmGenre = "INSERT INTO films_genres (film_id, genre_id) VALUES (?, ?)";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(insertFilmGenre, film.getId(), genre.getId());
            }
        }
        return film;
    }

    @Override
    public Film getFilmById(Integer id) {
        Film film = new Film();
        String sqlFilm = "SELECT * FROM FILMS WHERE FILM_ID = " + id;
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet(sqlFilm);
        if (filmRows.next()){
            film.setId(id);
            film.setName(filmRows.getString("FILM_NAME"));
            film.setDescription(filmRows.getString("FILM_DESCRIPTION"));
            film.setReleaseDate(LocalDate.parse(filmRows.getString("RELEASE_DATE")));
            film.setDuration(filmRows.getInt("FILM_DURATION"));
        }

        String sqlRequestGenre = "SELECT g.* FROM GENRES g JOIN FILMS_GENRES fg ON g.GENRE_ID = fg.GENRE_ID WHERE fg.FILM_ID = ?";
        List<Genre> genres = jdbcTemplate.query(sqlRequestGenre, new Object[]{id}, (rs, rowNum) -> {
            Genre genre = new Genre();
            genre.setId(rs.getInt("GENRE_ID"));
            genre.setName(rs.getString("GENRE_NAME"));
            return genre;
        });
        Genre[] genresForFilm = genres.toArray(new Genre[0]);
        film.setGenres(genresForFilm);

        Mpa mpa = new Mpa();
        String sqlRequestMpa = "SELECT m.* FROM MPAS m JOIN FILMS f ON m.MPAS_ID = f.MPA_ID WHERE f.FILM_ID = ?";
        jdbcTemplate.query(sqlRequestMpa, new Object[]{id}, (rs, rowNum) -> {
            mpa.setId(rs.getInt("MPAS_ID"));
            mpa.setName(rs.getString("MPAS_NAME"));
            return mpa;
        });
        film.setMpa(mpa);

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
                "LIMIT ?";
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
        for (Integer filmId: filmsId) {
            films.put(filmId, getFilmById(filmId));
        }
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
