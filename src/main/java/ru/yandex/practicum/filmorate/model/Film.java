package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.Set;

@Data
/**Позволяет генерировать геттеры, сеттеры, методы toString(), equals() и hashCode() и конструкторы
 * со всеми final-полями, а значит, объединяет в себе возможности сразу пяти аннотаций
 * @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
 * */
@Builder
public class Film {
    private Set<Long> likes;
    @PositiveOrZero
    private int id;
    @NotNull @NotBlank
    private String name;
    @Builder.Default
    private String description = "Описание не было заполнено!";
    @PastOrPresent
    private LocalDate releaseDate;
    @Builder.Default
    private int duration = 0;
}
