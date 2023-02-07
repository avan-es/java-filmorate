package ru.yandex.practicum.filmorate.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

@Component("mpaDaoImpl")
public class MpaDaoImpl implements MpaDao {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Mpa getMpaById(Integer id) {
        String sql = "SELECT * FROM MPAS WHERE MPAS_ID = ?";
        SqlRowSet mpaRS = jdbcTemplate.queryForRowSet(sql, id);
        if (mpaRS.next()) {
            Mpa mpa = new Mpa();
            mpa.setId(mpaRS.getInt("MPAS_ID"));
            mpa.setName(mpaRS.getString("MPAS_NAME"));
            return mpa;
        } else {
            throw new NotFoundException("Возрастное ограничение с ID: " + id +" не найдено.");
        }
    }

    @Override
    public List<Mpa> getAllMpas() {
        List<Mpa> result = new ArrayList<>();
        String sql = "SELECT MPAS_ID FROM MPAS ORDER BY MPAS_ID";
        List<Integer> ids = jdbcTemplate.queryForList(sql, Integer.class);
        for (Integer id : ids) {
            result.add(getMpaById(id));
        }
        return result;
    }
}
