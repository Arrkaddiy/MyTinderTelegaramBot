package ru.league.tinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.league.tinder.service.UserProfileService;

@RestController
@RequestMapping("tinder/user/{tinderId}/my-profile")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping
    public String myProfileMethod(@PathVariable Long tinderId) {
        return userProfileService.myProfileMethod(tinderId);
    }

    @PostMapping("/sing-in")
    public String singInMethod(@PathVariable Long tinderId, @RequestParam String name, @RequestParam String pass) {
        return userProfileService.singInMethod(tinderId, name, pass);
    }

    @PostMapping("/sing-up")
    public String singUpMethod(@PathVariable Long tinderId,
                               @RequestParam String name,
                               @RequestParam String pass,
                               @RequestParam String sex) {
        return userProfileService.singUpMethod(tinderId, name, pass, sex);
    }

    @GetMapping("/sing-out")
    public String singOutMethod(@PathVariable Long tinderId) {
        return userProfileService.singOutMethod(tinderId);
    }

    @PostMapping("/update")
    public String updateMethod(@PathVariable Long tinderId, @RequestParam String about) {
        return userProfileService.updateMethod(tinderId, about);
    }
}