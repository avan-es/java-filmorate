package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController();
    }

    @Test
    @DisplayName("Электронная почта не может быть пустой и должна содержать символ @")
    public void shouldReturnExceptionOnNonCorrectEmail () throws RuntimeException {
        User user = User.builder()
                .id(1)
                .email("bagratMail.ru")
                .login("Avan")
                .name("Bagrat")
                .birthday(LocalDate.of(1990, Month.SEPTEMBER, 4))
                .build();
        Throwable thrown = assertThrows(RuntimeException.class, () -> {
            userController.addUser(user);
        });
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @", thrown.getMessage(),
                "Ожидалась ошибка валидации почты пользователя.");
    }

    @Test
    @DisplayName("Логин не может быть пустым и содержать пробелы")
    public void shouldReturnExceptionOnNonCorrectLogin () throws RuntimeException {
        User userWithSpacesInLogin = User.builder()
                .id(1)
                .email("bagrat@mail.ru")
                .login("Av an")
                .name("Bagrat")
                .birthday(LocalDate.of(1990, Month.SEPTEMBER, 4))
                .build();
        Throwable thrownUserWithSpaces = assertThrows(RuntimeException.class, () -> {
            userController.addUser(userWithSpacesInLogin);
        });
        User userWithEmptyLogin = User.builder()
                .id(1)
                .email("bagrat@mail.ru")
                .login("")
                .name("Bagrat")
                .birthday(LocalDate.of(1990, Month.SEPTEMBER, 4))
                .build();
        Throwable thrownUserWithEmptyLogin = assertThrows(RuntimeException.class, () -> {
            userController.addUser(userWithEmptyLogin);
        });
        assertAll(
                () -> assertEquals("Логин не может быть пустым и содержать пробелы.", thrownUserWithSpaces.getMessage(),
                        "Ожидалась ошибка валидации логина пользователя."),
                () -> assertEquals("Логин не может быть пустым и содержать пробелы.", thrownUserWithEmptyLogin.getMessage(),
                        "Ожидалась ошибка валидации логина пользователя.")
        );
    }

    @Test
    @DisplayName("Имя для отображения может быть пустым — в таком случае будет использован логин")
    public void shouldSetLoginAsNameIfNameIsEmpty () throws RuntimeException {
        User user = User.builder()
                .id(1)
                .email("bagrat@Mail.ru")
                .login("Avan")
                .name("")
                .birthday(LocalDate.of(1990, Month.SEPTEMBER, 4))
                .build();
        userController.addUser(user);
        assertEquals(user.getLogin(), user.getName(),
                "Имя для отображения может быть пустым — в таком случае будет использован логин");
    }

    @Test
    @DisplayName("Дата рождения не может быть в будущем")
    public void shouldReturnExceptionOnNonCorrectBirthday () throws RuntimeException {
        User user = User.builder()
                .id(1)
                .email("bagrat@Mail.ru")
                .login("Avan")
                .name("Bagrat")
                .birthday(LocalDate.now().plusDays(1))
                .build();
        Throwable thrown = assertThrows(RuntimeException.class, () -> {
            userController.addUser(user);
        });
        assertEquals("Дата рождения не может быть в будущем.", thrown.getMessage(),
                "Дата рождения не может быть в будущем");
    }
}