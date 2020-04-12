package ru.league.tinder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.league.tinder.bot.Bot;
import ru.league.tinder.bot.BotContext;
import ru.league.tinder.entiy.User;
import ru.league.tinder.service.UserService;
import ru.league.tinder.states.StateType;

import static ru.league.tinder.states.StateType.getStateTypeByStateId;

@Controller
public class BotController {

    public static final Logger log = LoggerFactory.getLogger(BotController.class);

    private UserService userService;
    private StateController stateController;

    public BotController(UserService userService, StateController stateController) {
        this.userService = userService;
        this.stateController = stateController;
    }

    public void of(Bot bot, Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText())
            return;

        log.debug("Новый запрос:");
        final String text = update.getMessage().getText();
        log.debug("Получено тело сообщения -'{}'", text);
        final long chatId = update.getMessage().getChatId();
        log.debug("Получен код чата -'{}'", chatId);

        User user = getUser(chatId);
        log.debug("Получен пользователь - '{}'", user);
        StateType state = getStateTypeByStateId(user.getStateId());
        log.debug("Получено текущее положение пользователя - '{}'", state);
        BotContext context = BotContext.of(bot, user, text);
        log.debug("Получен контекст - '{}'", context);
        processingRequest(context, state);
    }

    public void processingRequest(BotContext context, StateType stateType) {
        log.debug("Обработка запроса - '{}'", context.getInput());
        if (context.getInput().equalsIgnoreCase("/help")) {
            stateType.getState().help(context);
        } else {
            searchStateController(context, stateType);
        }
    }

    public void searchStateController(BotContext context, StateType stateType) {
        switch (stateType) {
            case START: {
                stateController.startState(context, stateType.getState());
                break;
            }

            case PROFILE: {
                stateController.profileState(context, stateType.getState());
                break;
            }

            case SING_UP: {
                stateController.singUpState(context, stateType.getState());
                break;
            }
        }
    }

    public User getUser(Long chatId) {
        User user = userService.findByChatId(chatId);
        log.debug("Нахождение пользователя в базе - '{}'", user);

        if (user == null) {
            user = new User(chatId);
            userService.save(user);
            log.debug("Зарегестрирован новый пользователь - '{}'", user);
        }

        return user;
    }
}
