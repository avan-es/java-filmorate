package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;


@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Director {
    private Integer id;
    @NotBlank
    private String name;
}

