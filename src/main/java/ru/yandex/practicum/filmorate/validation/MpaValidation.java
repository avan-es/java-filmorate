package ru.yandex.practicum.filmorate.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.exeptions.MpaException.MpaNotFoundException;

@Component("mpaValidation")
public class MpaValidation {

    @Qualifier("mpaDaoImpl")
    private MpaDao mpaDao;

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaValidation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public void mpaIdValidationDB(Integer id) {
        String sqlRequest =  "select * " +
                "from PUBLIC.MPAS where MPAS_ID = " + id;
        SqlRowSet mpaRows = jdbcTemplate.queryForRowSet(sqlRequest);
        if (!mpaRows.next()) {
            throw new MpaNotFoundException("Возрастное ограничение с ID: " + id +" не найдено.");
        }
    }

}
