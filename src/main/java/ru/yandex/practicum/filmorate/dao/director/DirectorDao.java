package ru.yandex.practicum.filmorate.dao.director;

import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorDao {
    Director getDirectorById(Integer id);
    List<Director> getAllDirectors();
    Director addDirector (Director director);
    Director updateDirector (Director director);
    Director deleteDirector (Integer id);
}
