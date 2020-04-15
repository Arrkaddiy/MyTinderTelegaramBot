package ru.league.tinder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.league.tinder.bot.Bot;
import ru.league.tinder.bot.BotContext;
import ru.league.tinder.entity.User;
import ru.league.tinder.service.StateService;
import ru.league.tinder.service.UserService;
import ru.league.tinder.states.StateType;

@Controller
public class StateController {

    private static final Logger log = LoggerFactory.getLogger(StateController.class);

    private UserService userService;
    private StateService stateService;

    public StateController(UserService userService, StateService stateService) {
        this.userService = userService;
        this.stateService = stateService;
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

        if (user == null) {
            user = new User(chatId, StateType.START);
            userService.save(user);
            log.debug("Сохранение нового пользователя - '{}'", user);
            BotContext context = BotContext.of(bot, user, input);
            log.debug("Получен контекст - '{}'", context);
            stateService.getState(user.getState()).ifPresent(state -> state.enter(context));

        } else {
            BotContext context = BotContext.of(bot, user, input);
            log.debug("Получен контекст - '{}'", context);
            stateService.getState(user.getState()).ifPresent(state -> state.of(context));
        }
    }
}
