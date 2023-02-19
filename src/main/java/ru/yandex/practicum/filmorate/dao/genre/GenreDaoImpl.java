package ru.yandex.practicum.filmorate.dao.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

import static ru.yandex.practicum.filmorate.constants.Constants.INDEX_FOR_LIST_WITH_ONE_ELEMENT;

@Component("genreDaoImpl")
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public GenreDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public Genre getGenreById(Integer id) {
        String sql = "SELECT * " +
                     "FROM genres " +
                     "WHERE genre_id = " + id;
        List<Genre> genres = jdbcTemplate.query(sql, new GenreMapper());
        return genres.get(INDEX_FOR_LIST_WITH_ONE_ELEMENT);
    }

    @Override
    public List<Genre> getAllGenre() {
        String sql = "SELECT * " +
                     "FROM genres " +
                     "ORDER BY genre_id";
        List<Genre> genres = jdbcTemplate.query(sql, new GenreMapper());
        return genres;
    }
}
