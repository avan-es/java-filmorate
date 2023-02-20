package ru.yandex.practicum.filmorate.dao.director;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.PreparedStatement;
import java.util.List;

import static ru.yandex.practicum.filmorate.constants.Constants.INDEX_FOR_LIST_WITH_ONE_ELEMENT;

@Component("directorDbStorage")
@Repository
public class DirectorDbStorage implements DirectorDao{

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DirectorDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Director getDirectorById(Integer id) {
        String sql = "SELECT * " +
                     "FROM director " +
                     "WHERE director_id = " + id;
        List<Director> directors = jdbcTemplate.query(sql, new DirectorMapper());
        return directors.get(INDEX_FOR_LIST_WITH_ONE_ELEMENT);
    }

    @Override
    public List<Director> getAllDirectors() {
        String sql = "SELECT * " +
                     "FROM director ";
        List<Director> directors = jdbcTemplate.query(sql, new DirectorMapper());
        return directors;
    }

    @Override
    public Director addDirector(Director director) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        String name = director.getName();
        String sql = "INSERT INTO director (director_name) " +
                     "VALUES ?";
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"director_id"});
            ps.setString(1, director.getName());
            return ps;
        }, keyHolder);
        director.setId(keyHolder.getKey().intValue());
        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        String sql = "UPDATE director " +
                     "SET director_name = ? " +
                     "WHERE director_id = ?";
        jdbcTemplate.update(sql, director.getName(), director.getId());
        return getDirectorById(director.getId());
    }

    @Override
    public void deleteDirector(Integer id) {
        String sql = "DELETE FROM director " +
                     "WHERE director_id = ?";
        jdbcTemplate.update(sql, id);
    }
}
