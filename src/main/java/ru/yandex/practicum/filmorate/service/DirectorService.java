package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.director.DirectorDbStorage;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.validation.DirectorValidation;

@Service
@RequiredArgsConstructor
public class DirectorService {
    private DirectorDbStorage directorDbStorage;
    private final DirectorValidation directorValidation;

    @Autowired
    public void setJdbcFilmDAO(@Qualifier("directorDbStorage") DirectorDbStorage directorDbStorage) {
        this.directorDbStorage = directorDbStorage;
    }


    public Director addDirector(Director director) {
        directorValidation.directorValidation(director);
        return directorDbStorage.addDirector(director);
    }

    public Director getDirectorById(Integer id) {
        directorValidation.directorIDValidation(id);
        return directorDbStorage.getDirectorById(id);
    }
}
