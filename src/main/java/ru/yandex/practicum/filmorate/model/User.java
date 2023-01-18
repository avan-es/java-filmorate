package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
/**Позволяет генерировать геттеры, сеттеры, методы toString(), equals() и hashCode() и конструкторы
 * со всеми final-полями, а значит, объединяет в себе возможности сразу пяти аннотаций
 * @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
 * */
@Builder
public class User {
    //final позволяет исключить поле класса из видимости Builder - тогда id добавляются!
    private final Set<Integer> friends = new HashSet<>();
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
