package ru.league.tinder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.league.tinder.model.ProfileModel;
import ru.league.tinder.service.ProfileService;

@RestController
@RequestMapping("tinder/user/{tinderId}/profiles")
public class ProfilesController {

    private static final Logger log = LoggerFactory.getLogger(UserProfileController.class);

    @Autowired
    private ProfileService profileService;

    @GetMapping("/left")
    public ProfileModel leftMethod(@PathVariable Long tinderId) {
        return profileService.leftMethod(tinderId);
    }

    @PostMapping("/right")
    public String rightMethod(@PathVariable Long tinderId) {
        return profileService.rightMethod(tinderId);
    }
}
