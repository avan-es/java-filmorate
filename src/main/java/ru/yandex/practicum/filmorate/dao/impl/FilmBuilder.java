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
            Integer id = rs.getInt("FILM_ID");
            Film film = films.get(id);
            if (film == null){
                film = new Film();
                film.setId(rs.getInt("FILM_ID"));
                film.setName(rs.getString("FILM_NAME"));
                film.setDescription(rs.getString("FILM_DESCRIPTION"));
                film.setReleaseDate(LocalDate.parse(rs.getString("RELEASE_DATE")));
                film.setDuration(rs.getInt("FILM_DURATION"));
                film.setGenres(new ArrayList<>());
            }
            if (rs.getInt("GENRE_ID") != 0){
                Genre genre = new Genre(
                        rs.getInt("GENRE_ID"),
                        rs.getString("GENRE_NAME")
                );
                film.getGenres().add(genre);
            }
            Mpa mpa = new Mpa();
            mpa.setId(rs.getInt("MPAS_ID"));
            mpa.setName(rs.getString("MPAS_NAME"));
            film.setMpa(mpa);
            films.put(film.getId(), film);
        }

        return new ArrayList<>(films.values());
    }
}
