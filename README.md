# Filmorate (Фильмотека)
Бэкенд для сервиса, который работает с фильмами и оценками пользователей, а также возвращает топ-N фильмов, рекомендованных к просмотру.

# Стек технологий
- Java
- Spring Boot
- Hibernate
- PostgreSQL
- JdbcTemplate
- REST
- Apache Maven
- Lombok
- Postman
- IntelliJ IDEA
- DBeaver

# Схема базы данных

![DB](https://github.com/avan-es/java-filmorate/assets/83888190/6446ba8d-dfff-4174-9e2c-45c4cb870a0a)

# Реализованные эндпоинты

<p align="center">
  <table align="center" width="100%">
    <tr>
        <th colspan="2">
          <img width="50" src="https://github.com/avan-es/java-filmorate/assets/83888190/bf414de4-dbba-4f3a-a888-b180fad09728"/><br>UserController
        </th>
      </tr>
    <tr>
      <td>POST /users</td><td>Добавление нового пользователя в БД</td>
    </tr>
    <tr>
      <td>GET /users/{id}</td><td>Получение информации о пользователе из БД по его ID</td>
    </tr>
    <tr>
      <td>PUT /users</td><td>Обновление информации о существующем пользователе</td>
    </tr>
    <tr>
      <td>GET /users</td><td>Получение списка всех пользователей из БД</td>
    </tr>
    <tr>
      <td>PUT /users/{id}/friends/{friendId}</td><td>Отправление запроса на добавление в друзья/ответить на заявку</td>
    </tr>
    <tr>
      <td>GET /users/{id}/friends</td><td>Получение списка друзей пользователя</td>
    </tr>
    <tr>
      <td>GET /users/{id}/friends/common/{otherId}</td><td>Получение списка общих друзей</td>
    </tr>
    <tr>
      <td>DELETE /users/{id}/friends/{friendId}</td><td>Удаление пользователя из друзей</td>
    </tr>
    <tr>
        <th colspan="2">
          <img width="50" src="https://github.com/avan-es/java-filmorate/assets/83888190/44063823-9839-4876-8791-ed41d8b8453f"/><br>FilmController
        </th>
      </tr>
    <tr>
      <td>POST /films</td><td>Добавление нового фильма в БД</td>
    </tr>
    <tr>
      <td>GET /films/{id}</td><td>Получение информации о фильме из БД по его ID</td>
    </tr>
    <tr>
      <td>GET /films</td><td>Получение списка всех фильмов из БД</td>
    </tr>
    <tr>
      <td>PUT /films</td><td>Обновление информации о существующем фильме</td>
    </tr>
    <tr>
      <td>PUT /films/{id}/like/{userId}</td><td>Добавление лайка фильму от пользователя</td>
    </tr>
    <tr>
      <td>DELETE /films/{id}/like/{userId}</td><td>Удаление лайка пользователя с фильма</td>
    </tr>
    <tr>
      <td>GET /films/popular</td><td>Получение топ-N фильмов по оценкам</td>
    </tr>
    <tr>
        <th colspan="2">
          <img width="50" src="https://github.com/avan-es/java-filmorate/assets/83888190/7947d063-025b-4e50-bb1e-e729f19ec18e"/><br>GenreController
        </th>
      </tr>
    <tr>
      <td>GET /genres</td><td>Получение списка всех жанров из БД</td>
    </tr>
    <tr>
      <td>GET /genres/{id}</td><td>Получение информации о жанре из БД по его ID</td>
    </tr>
    <tr>
        <th colspan="2">
          <img width="50" src="https://github.com/avan-es/java-filmorate/assets/83888190/a8c3e005-c3c8-4052-9f59-03ba594f956f"/><br>MpaController (возрастной рейтинг)
        </th>
      </tr>
    <tr>
      <td>GET /mpa</td><td>Получение списка всех возрастных ограничений из БД</td>
    </tr>
    <tr>
      <td>GET /mpa/{id}</td><td>Получение информации о возрастном ограничении из БД по его ID</td>
    </tr>
  </table>
  </p>

## Запуск программы

Для запуска приложения потребуется *** Java 11, IntelliJ IDEA.***

Алгоритм:
- Склонировать приложение в свой репозиторий или скачать на компьютер;
- Открыть и запустить проект в ***IntelliJ IDEA***;

### Для тестирования подготовлен файл коллекции Postman
[Test Filmorate - Filmorate-Postman-Tests.postman_collection.json](https://github.com/avan-es/java-filmorate/blob/main/postman/Filmorate-Postman-Tests.postman_collection.json)

