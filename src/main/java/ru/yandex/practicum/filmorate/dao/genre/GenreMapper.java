package ru.yandex.practicum.filmorate.dao.genre;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class GenreMapper implements ResultSetExtractor<List<Genre>> {
    @Override
    public List<Genre> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Genre> genres = new LinkedHashMap<>();
        while (rs.next()){
            Integer id = rs.getInt("genre_id");
            Genre genre = genres.get(id);
            if (genre == null){
                genre = new Genre();
                genre.setId(rs.getInt("genre_id"));
                genre.setName(rs.getString("genre_name"));
            }
            genres.put(genre.getId(), genre);
        }
        return new ArrayList<>(genres.values());
    }
}
