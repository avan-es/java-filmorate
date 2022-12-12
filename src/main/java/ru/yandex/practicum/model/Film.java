package ru.yandex.practicum.model;


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
    private final int id;
    @NonNull
    private final String name;
    @Builder.Default
    private final String description = "Описание не было заполнено!";
    @NonNull
    private final LocalDate releaseDate;
    @NonNull
    private final Duration duration;
}
