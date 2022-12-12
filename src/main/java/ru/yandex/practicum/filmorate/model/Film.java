package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class Film {
    private int id;
    @NonNull
    private String name;
    @Builder.Default
    private String description = "Описание не было заполнено!";
    @NonNull
    private LocalDate releaseDate;
    @Builder.Default
    private int duration = 0;
//    @Builder.Default
//    private int rate = 0;
}
