package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.MpaDao;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.validation.MpaValidation;
import ru.yandex.practicum.filmorate.validation.UserValidation;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {

    private MpaDao mpaDao;

    private final MpaValidation mpaValidation;

    @Autowired
    public void setJdbcmpaDao(@Qualifier("mpaDaoImpl") MpaDao mpaDao) {
        this.mpaDao = mpaDao;
    }

    public Mpa getMpaById(int id) {
        mpaValidation.mpaIdValidationDB(id);
        return mpaDao.getMpaById(id);
    }

    public List<Mpa> getAllMpas() {
        return mpaDao.getAllMpas();
    }
}
