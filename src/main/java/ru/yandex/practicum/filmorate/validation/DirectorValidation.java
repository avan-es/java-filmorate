package ru.yandex.practicum.filmorate.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.director.DirectorDbStorage;
import ru.yandex.practicum.filmorate.exeptions.ModelValidationException;
import ru.yandex.practicum.filmorate.exeptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;

@Component("directorValidation")
public class DirectorValidation {

    private DirectorDbStorage directorDbStorage;
    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public DirectorValidation(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Director directorValidation (Director director) {
        if (director.getName().isBlank()) {
            throw new ModelValidationException(String.format("Ошибка в имени режиссёра. Введенное имя: '%s'.", director.getName()));
        }
        return director;
    }

    public void directorIDValidation(Integer id) {
        String sql = "SELECT director_id " +
                     "FROM director " +
                     "WHERE director_id = " + id +
                     " FETCH FIRST 1 ROWS ONLY";
        if (!jdbcTemplate.queryForRowSet(sql).next()) {
            throw new NotFoundException(String.format("Режиссёр с ID %d не найден.", id));
        }
    }
}