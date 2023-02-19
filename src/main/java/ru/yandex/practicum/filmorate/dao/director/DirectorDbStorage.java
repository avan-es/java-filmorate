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
        return null;
    }

    @Override
    public List<Director> getAllDirectors() {
        return null;
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

        return director;
    }

    @Override
    public Director updateDirector(Director director) {
        return null;
    }

    @Override
    public Director deleteDirector(Integer id) {
        return null;
    }
}
