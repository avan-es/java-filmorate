package ru.yandex.practicum.filmorate.dao.mpa;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MpaMapper implements ResultSetExtractor <List<Mpa>> {
    @Override
    public List<Mpa> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Mpa> mpas = new LinkedHashMap<>();
        while (rs.next()){
            Integer id = rs.getInt("mpas_id");
            Mpa mpa = mpas.get(id);
            if (mpa == null) {
                mpa = new Mpa();
                mpa.setId(rs.getInt("mpas_id"));
                mpa.setName(rs.getString("mpas_name"));
            }
            mpas.put(mpa.getId(), mpa);
        }
        return new ArrayList<>(mpas.values());
    }
}
