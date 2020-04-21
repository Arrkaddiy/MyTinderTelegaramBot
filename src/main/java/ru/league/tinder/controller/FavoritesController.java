package ru.league.tinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.league.tinder.service.FavoritesService;

@RestController
@RequestMapping("tinder/user/{tinderId}/favorites")
public class FavoritesController {

    @Autowired
    private FavoritesService favoritesService;

    @GetMapping
    public String favoritesMethod(@PathVariable Long tinderId) {
        return favoritesService.favoritesMethod(tinderId);
    }

    @GetMapping("{number}")
    public String favoritesNumberMethod(@PathVariable Long tinderId, @PathVariable String number) {
        return favoritesService.favoritesNumberMethod(tinderId, number);
    }

    @GetMapping("exit")
    public String exitMethod(@PathVariable Long tinderId) {
        return favoritesService.exitMethod(tinderId);
    }
}
