package ru.yandex.practicum.filmorate.dao.genre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
                     "WHERE genre_id = ?";
        SqlRowSet genreRS = jdbcTemplate.queryForRowSet(sql, id);
        if (genreRS.next()) {
            return new Genre(
                genreRS.getInt("genre_id"),
                genreRS.getString("genre_name")
            );
        } else {
            throw new NotFoundException(String.format("Жанр с ID %d не найден.", id));
        }
    }

    @Override
    public List<Genre> getAllGenre() {
        String sql = "SELECT * " +
                     "FROM genres " +
                     "ORDER BY genre_id";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<Genre> result = new ArrayList<>();
        list.forEach(m -> {
            Genre g = new Genre(((Integer)m.get("genre_id")), ((String)m.get("genre_name")));
            result.add(g);
        });
        return result;
    }
}
