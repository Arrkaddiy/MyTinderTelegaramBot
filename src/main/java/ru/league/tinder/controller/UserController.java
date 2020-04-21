package ru.league.tinder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.entity.User;
import ru.league.tinder.model.ProfileModel;
import ru.league.tinder.model.StateModel;
import ru.league.tinder.service.UserService;
import ru.league.tinder.states.StateType;

@RestController
@RequestMapping("tinder/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/{tinderId}")
    public Long getUserById(@PathVariable Long tinderId) {
        log.debug("Поиск пользователя по Id - '{}'", tinderId);
        User user = userService.findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);
        return user.getId();
    }

    @GetMapping("/{tinderId}/lastlook")
    public Profile getUserLastlook(@PathVariable Long tinderId) {
        log.debug("Поиск пользователя по Id - '{}'", tinderId);
        User user = userService.findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);
        return user.getLastLookProfile();
    }

    @GetMapping("/{tinderId}/methods")
    public StateModel getPossibleMethods(@PathVariable Long tinderId) {
        log.debug("Передача доступных методов стадии пользователя по Id - '{}'", tinderId);
        User user = userService.findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);

        if (user.isAuthority()) {
            return new StateModel(user.getState().getAuthorityCode());
        } else {
            return new StateModel(user.getState().getNotAuthorityCode());
        }
    }

    @PostMapping("/create")
    public Long createUser() {
        log.debug("Создание нового пользователя");
        User user = new User(StateType.START);
        userService.save(user);
        log.debug("Получен пользователь - '{}'", user);
        return user.getId();
    }

    @DeleteMapping("/{tinderId}")
    public void deleteUser(@PathVariable Long tinderId) {
        log.debug("Удаление пользователя по Id - '{}'", tinderId);
        User user = userService.findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);
        userService.delete(user);
    }
}
