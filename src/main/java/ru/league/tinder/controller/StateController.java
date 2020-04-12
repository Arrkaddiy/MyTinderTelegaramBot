package ru.league.tinder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.league.tinder.bot.BotContext;
import ru.league.tinder.service.ProfileService;
import ru.league.tinder.service.UserService;
import ru.league.tinder.states.State;
import ru.league.tinder.states.StateType;

@Controller
public class StateController {

    public static final Logger log = LoggerFactory.getLogger(StateController.class);

    private final UserService userService;
    private final ProfileService profileService;

    public StateController(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    public void startState(BotContext context, State state) {

        enter(context, state);
        nextState(context, state);
    }

    public void profileState(BotContext context, State state) {
        nextState(context, state);
    }

    public void singUpState(BotContext context, State state) {

    }

    private void enter(BotContext context, State state) {
        if (context.getInput().equals(state.getCode())) {
            log.debug("Пользователь зашел - '{}'", state.getStateId());
            state.enter(context);
        }
    }

    private void nextState(BotContext context, State state) {
        State nextState = StateType.getStateByCode(context.getInput());
        log.debug("Найдено возможное переходное положения пользователя - '{}'", nextState.getStateId());
        if (state.getPossibleState().contains(nextState)) {
            state = nextState;
            log.debug("Получено новое положение пользователя - '{}'", state.getStateId());
            saveUserStateId(context, state);
            enter(context, state);
//            state.enter(context);
        } else {
            log.warn("Переход на данное положение не возможно!");
        }
    }

    private void saveUserStateId(BotContext context, State state) {
        context.getUser().setStateId(state.getStateId());
        userService.save(context.getUser());
        log.debug("Сохрание нового положения пользователя - '{}'", state.getStateId());
    }


}
