package ru.league.tinder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.league.tinder.bot.RequestContext;
import ru.league.tinder.bot.ResponseContext;
import ru.league.tinder.config.StateConfig;
import ru.league.tinder.entity.User;
import ru.league.tinder.service.UserService;
import ru.league.tinder.states.State;
import ru.league.tinder.states.StateType;

@Controller
public class StateController {

    private static final Logger log = LoggerFactory.getLogger(StateController.class);

    private UserService userService;
    private StateConfig stateConfig;

    public StateController(UserService userService, StateConfig stateConfig) {
        this.userService = userService;
        this.stateConfig = stateConfig;
    }

    public String of(Long chatId, String input) {
        log.debug("Новый запрос:");
        log.debug("Получено тело сообщения - '{}'", input);
        log.debug("Получен код чата - '{}'", chatId);

        if (input.equalsIgnoreCase("/start")) {
            rebase(chatId);
        }

        User user = userService.findByChatId(chatId);
        log.debug("Получен пользователь - '{}'", user);

        State state;
        RequestContext context;

        if (user == null) {
            user = new User(chatId, StateType.START);
            userService.save(user);
            log.debug("Сохранение нового пользователя - '{}'", user);

            context = RequestContext.of(user, input);
            log.debug("Получен контекст - '{}'", context);

            state = stateConfig.getState(user.getState()).orElseThrow(IllegalArgumentException::new);
            log.debug("Получено состояние - '{}'", state);
            log.debug("Выполняем вход на состояние - '{}'", state);
            state.enter(context);

        } else {
            context = RequestContext.of(user, input);
            log.debug("Получен контекст - '{}'", context);

            state = stateConfig.getState(user.getState()).orElseThrow(IllegalArgumentException::new);
            log.debug("Получено состояние - '{}'", state);
        }

        log.debug("Отправка контекста - '{}', на обработку в - '{}'", context, state);
        ResponseContext responseContext = state.nextState(context);
        log.debug("Получен ответ - '{}'", responseContext);
        StateType nextState = responseContext.getStateType();
        String answer = responseContext.getOutput();

        if (!nextState.equals(user.getState())) {
            log.debug("Переход на новое состояние - '{}'", nextState);
            saveUserState(nextState, user);
            state = stateConfig.getState(user.getState()).orElseThrow(IllegalArgumentException::new);
            log.debug("Получено состояние - '{}'", state);
            log.debug("Выполняем вход на состояние - '{}'", state);
            answer = state.enter(context);
        } else {
            log.debug("Состояние не изменнено, остаемся на - '{}'", nextState);
        }

        return answer;
    }

    private void saveUserState(StateType nextState, User user) {
        log.debug("Сохрание нового состояния пользователя - '{}'", nextState);
        user.setState(nextState);
        userService.save(user);
    }

    private void rebase(Long chatId) {
        log.debug("Перезапуск бота у пользователя - '{}'", chatId);
        userService.delete(chatId);
    }
}
