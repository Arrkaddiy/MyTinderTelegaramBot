package ru.league.tinder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.league.tinder.bot.Bot;
import ru.league.tinder.bot.BotContext;
import ru.league.tinder.config.StateConfig;
import ru.league.tinder.entity.User;
import ru.league.tinder.service.UserService;
import ru.league.tinder.states.State;
import ru.league.tinder.states.StateType;

import java.util.Optional;

@Controller
public class StateController {

    private static final Logger log = LoggerFactory.getLogger(StateController.class);

    private UserService userService;
    private StateConfig stateConfig;

    public StateController(UserService userService, StateConfig stateConfig) {
        this.userService = userService;
        this.stateConfig = stateConfig;
    }

    public void of(Bot bot, Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText())
            return;

        log.debug("Новый запрос:");
        final String input = update.getMessage().getText();
        log.debug("Получено тело сообщения - '{}'", input);
        final long chatId = update.getMessage().getChatId();
        log.debug("Получен код чата - '{}'", chatId);

        User user = userService.findByChatId(chatId);
        log.debug("Получен пользователь - '{}'", user);

        BotContext context;
        Optional<State> state;

        if (user == null) {
            user = new User(chatId, StateType.START);
            userService.save(user);
            log.debug("Сохранение нового пользователя - '{}'", user);
            context = BotContext.of(bot, user, input);
            log.debug("Получен контекст - '{}'", context);
            state = stateConfig.getState(user.getState());
            state.orElseThrow(IllegalAccessError::new).enter(context);
        } else {
            context = BotContext.of(bot, user, input);
            log.debug("Получен контекст - '{}'", context);
            state = stateConfig.getState(user.getState());
        }

        log.debug("Выполнение введенной команды");
        state.orElseThrow(IllegalAccessError::new).handleInput(context);

        StateType nextState = state.orElseThrow(IllegalAccessError::new).getState();
        log.debug("Получение следующего состояния - '{}'", nextState);

        if (!nextState.equals(user.getState())) {
            log.debug("Переход на новое состояние - '{}'", nextState);
            saveUserState(state.orElseThrow(IllegalAccessError::new).getState(), user);
            state = stateConfig.getState(user.getState());
            state.orElseThrow(IllegalAccessError::new).enter(context);
        } else {
            log.debug("Состояние не изменнено, остаемся на - '{}'", nextState);
        }
    }

    private void saveUserState(StateType nextState, User user) {
        log.debug("Сохрание нового состояния пользователя - '{}'", nextState);
        user.setState(nextState);
        userService.save(user);
    }
}
