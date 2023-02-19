package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Data
/**Позволяет генерировать геттеры, сеттеры, методы toString(), equals() и hashCode() и конструкторы
 * со всеми final-полями, а значит, объединяет в себе возможности сразу пяти аннотаций
 * @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor
 * */
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Film {
    private final Set<Integer> likes = new HashSet<>();
    @PositiveOrZero
    private int id;
    private String name;
    @Builder.Default
    private String description = "Описание не было заполнено!";
    @PastOrPresent
    private LocalDate releaseDate;
    @Builder.Default
    private int duration = 0;
    private ArrayList<Genre> genres;
    private Mpa mpa;
    private ArrayList<Director> directors;
}
