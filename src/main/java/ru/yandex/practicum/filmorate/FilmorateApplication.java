package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import org.springframework.context.ApplicationContext;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;


@SpringBootApplication
public class FilmorateApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(FilmorateApplication.class, args);
	}

}
