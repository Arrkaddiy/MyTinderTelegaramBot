package ru.league.tinder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.league.tinder.entity.User;
import ru.league.tinder.service.UserService;
import ru.league.tinder.states.StateType;

@RestController
@RequestMapping("tinder/user/{tinderId}/start")
public class StartController {

    private static final Logger log = LoggerFactory.getLogger(StartController.class);

    @Autowired
    private UserService userService;

    @GetMapping
    public String start(@PathVariable Long tinderId) {
        log.debug("Выполнение сценария перехода на состояние - '/start'");
        User user = userService.findById(tinderId);
        user.setState(StateType.START);
        userService.save(user);
        return "Мстительный авантюрист мечтает\n" +
                "отойти от дел в уютной усадьбе\n" +
                "с любимой женщиной";
    }
}
