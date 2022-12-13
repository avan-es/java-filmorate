package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
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
