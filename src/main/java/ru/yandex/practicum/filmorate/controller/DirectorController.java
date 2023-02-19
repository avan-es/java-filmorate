package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @PostMapping
    public Director addDirector(@Valid @RequestBody Director director){
        return directorService.addDirector(director);
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable Integer id){
        return directorService.getDirectorById(id);
    }

}
