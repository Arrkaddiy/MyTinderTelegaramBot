package ru.league.tinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bot")
public class BotController {

    @Autowired
    private StateController stateController;

    @GetMapping
    public String update(@RequestParam String chatId, @RequestParam String input) {
        System.out.println(chatId);
        System.out.println(input);
        return stateController.of(Long.parseLong(chatId), input);
    }
}
