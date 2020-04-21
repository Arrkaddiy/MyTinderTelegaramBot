package ru.league.tinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.league.tinder.service.StartService;

@RestController
@RequestMapping("tinder/user/{tinderId}/start")
public class StartController {

    @Autowired
    private StartService startService;

    @GetMapping
    public String start(@PathVariable Long tinderId) {
        return startService.start(tinderId);
    }
}
