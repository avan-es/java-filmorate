package ru.yandex.practicum.filmorate.dao.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.mpa.MpaDao;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component("mpaDaoImpl")
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(Integer id) {
        String sql = "SELECT * " +
                     "FROM mpas " +
                     "WHERE mpas_id = ?";
        SqlRowSet mpaRS = jdbcTemplate.queryForRowSet(sql, id);
        if (mpaRS.next()) {
            Mpa mpa = new Mpa();
            mpa.setId(mpaRS.getInt("mpas_id"));
            mpa.setName(mpaRS.getString("mpas_name"));
            return mpa;
        } else {
            throw new NotFoundException("Возрастное ограничение с ID: " + id +" не найдено.");
        }
    }

    @Override
    public List<Mpa> getAllMpas() {
        String sql = "SELECT * " +
                     "FROM mpas ORDER BY mpas_id";
        List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
        List<Mpa> result = new ArrayList<>();
        list.forEach(m -> {
            Mpa mpa = new Mpa(((Integer)m.get("mpas_id")), ((String)m.get("mpas_name")));
            result.add(mpa);
        });
        return result;
    }
}
