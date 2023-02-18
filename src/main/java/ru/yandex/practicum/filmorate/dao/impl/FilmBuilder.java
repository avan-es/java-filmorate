package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;

@Component
public class FilmBuilder implements ResultSetExtractor<List<Film>> {

    @Override
    public List<Film> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Film> films = new LinkedHashMap<>();
        while (rs.next()){
            Integer id = rs.getInt("film_id");
            Film film = films.get(id);
            if (film == null){
                film = new Film();
                film.setId(rs.getInt("film_id"));
                film.setName(rs.getString("film_name"));
                film.setDescription(rs.getString("film_description"));
                film.setReleaseDate(LocalDate.parse(rs.getString("release_date")));
                film.setDuration(rs.getInt("film_duration"));
                film.setGenres(new ArrayList<>());
            }
            if (rs.getInt("genre_id") != 0){
                Genre genre = new Genre(
                        rs.getInt("genre_id"),
                        rs.getString("genre_name")
                );
                film.getGenres().add(genre);
            }
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("mpas_id"));
            mpa.setName(rs.getString("mpas_name"));
            film.setMpa(mpa);
            films.put(film.getId(), film);
        }

        return new ArrayList<>(films.values());
    }
}
