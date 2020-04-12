package ru.league.tinder.states;

import org.springframework.stereotype.Component;
import ru.league.tinder.service.UserService;

@Component
public class StateUtils {

    private final UserService userService;

    public StateUtils(UserService userService) {
        this.userService = userService;
    }

}
