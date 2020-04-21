package ru.league.tinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.model.StateModel;
import ru.league.tinder.service.UserService;

@RestController
@RequestMapping("tinder/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/{tinderId}")
    public Long getUserById(@PathVariable Long tinderId) {
        return userService.getUserById(tinderId);
    }

    @GetMapping("/{tinderId}/lastlook")
    public Profile getUserLastlook(@PathVariable Long tinderId) {
        return userService.getUserLastlook(tinderId);
    }

    @GetMapping("/{tinderId}/methods")
    public StateModel getPossibleMethods(@PathVariable Long tinderId) {
        return userService.getPossibleMethods(tinderId);
    }

    @PostMapping("/create")
    public Long createUser() {
        return userService.createUser();
    }

    @DeleteMapping("/{tinderId}")
    public void deleteUser(@PathVariable Long tinderId) {
        userService.deleteUser(tinderId);
    }
}
