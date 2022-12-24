package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
/**Позволяет генерировать геттеры, сеттеры, методы toString(), equals() и hashCode() и конструкторы
 * со всеми final-полями, а значит, объединяет в себе возможности сразу пяти аннотаций
 * @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
 * */
@Builder
//@RequiredArgsConstructor
public class User {
    @PositiveOrZero
    private int id;
    @Email
    private String email;
    @NotNull @NotBlank
    private String login;
    private String name;
    @PastOrPresent
    private final LocalDate birthday;
}
