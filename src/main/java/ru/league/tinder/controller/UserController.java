package ru.league.tinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.league.tinder.entity.User;
import ru.league.tinder.model.UserModel;
import ru.league.tinder.service.UserService;

@RestController
@RequestMapping("tinder/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("{tinderId}")
    public UserModel getUserById(@PathVariable Long tinderId) {
        User user = userService.findByChatId(tinderId);
        return new UserModel(tinderId, "Start");
    }

    @PostMapping("/")
    public UserModel createUser() {
        System.out.println("2");
        return new UserModel(1L, "Start");
    }

    @DeleteMapping("{tinderId}")
    public void deleteUser(@PathVariable Long tinderId) {
        System.out.println(tinderId + tinderId);
    }
}
