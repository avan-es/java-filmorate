package ru.yandex.practicum.filmorate.dao.mpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Mpa;
import java.util.List;

import static ru.yandex.practicum.filmorate.constants.Constants.INDEX_FOR_LIST_WITH_ONE_ELEMENT;

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
                     "WHERE mpas_id = " + id;
        List<Mpa> mpa = jdbcTemplate.query(sql, new MpaMapper());
        return mpa.get(INDEX_FOR_LIST_WITH_ONE_ELEMENT);
    }

    @Override
    public List<Mpa> getAllMpas() {
        String sql = "SELECT * " +
                     "FROM mpas ORDER BY mpas_id";
        List<Mpa> mpas = jdbcTemplate.query(sql, new MpaMapper());
        return mpas;
    }
}
