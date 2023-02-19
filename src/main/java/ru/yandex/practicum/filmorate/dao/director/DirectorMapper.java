package ru.yandex.practicum.filmorate.dao.director;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DirectorMapper implements ResultSetExtractor <List<Director>> {
    @Override
    public List<Director> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Director> directors = new LinkedHashMap<>();
        while (rs.next()) {
            Integer id = rs.getInt("director_id");
            Director director = directors.get(id);
            if (director == null) {
                director = new Director();
                director.setId(rs.getInt("director_id"));
                director.setName(rs.getString("director_name"));
            }
            directors.put(director.getId(), director);
        }
        return new ArrayList<>(directors.values());
    }
}
