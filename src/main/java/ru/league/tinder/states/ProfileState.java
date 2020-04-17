package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.league.tinder.bot.BotContext;
import ru.league.tinder.entity.User;
import ru.league.tinder.service.UserService;

import java.util.Optional;

@Component
public class ProfileState implements State, StateSendMessage {

    private static final Logger log = LoggerFactory.getLogger(StartState.class);

    private UserService userService;

    public ProfileState(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        if (context.getUser().isAuthority()) {
            sendTextMessage(context, "Создание и редактирование вашей анкеты.\n" +
                    "/update - Редактирование профиля\n" +
                    "/sing_out - Выйти\n" +
                    "/exit - Вернуться");
        } else {
            sendTextMessage(context, "Создание и редактирование вашей анкеты.\n" +
                    "/sing_in - Войти\n" +
                    "/sing_up - Новая\n" +
                    "/exit - Вернуться");
        }
    }

    @Override
    public StateType nextState(BotContext context) {
        log.debug("Обработка контекста - '{}'", context);
        Commands inputCommand = getCommand(context.getInput()).orElse(Commands.HELP);
        log.debug("Определена команда - '{}'", inputCommand);
        return execute(inputCommand, context);
    }

    private StateType execute(Commands command, BotContext context) {
        log.debug("Получена команда - '{}'. Определение сценария выполнения.", command);
        switch (command) {
            case HELP: {
                return executeHelpCommand(context);
            }

            case UPDATE: {
                return executeUpdateCommand(context);
            }

            case SING_IN: {
                return executeSingInCommand(context);
            }

            case SING_UP: {
                return executeSingUpCommand(context);
            }

            case SING_OUT: {
                return executeSingOutCommand(context);
            }

            case EXIT: {
                return executeExitCommand();
            }

            default: {
                log.warn("Не задано исполение для команды - '{}'!", command);
                return StateType.PROFILE;
            }
        }
    }

    private Optional<Commands> getCommand(String input) {
        try {
            return Optional.of(Commands.valueOf(input.toUpperCase().replaceFirst("/", "")));
        } catch (IllegalArgumentException e) {
            log.warn("Введена некоректная команда - '{}'!", input);
            return Optional.empty();
        }
    }

    private StateType executeHelpCommand(BotContext context) {
        log.debug("Выполнение сценария \"Подсказки\" - (/help)");
        if (context.getUser().isAuthority()) {
            sendTextMessage(context, "Создание и редактирование вашей анкеты.\n" +
                    "/update - Редактирование профиля\n" +
                    "/sing_out - Выйти\n" +
                    "/exit - Вернуться");
        } else {
            sendTextMessage(context, "Создание и редактирование вашей анкеты.\n" +
                    "/sing_in - Войти\n" +
                    "/sing_up - Новая\n" +
                    "/exit - Вернуться");
        }

        return StateType.PROFILE;
    }

    private StateType executeUpdateCommand(BotContext context) {
        log.debug("Выполнение сценария \"Редактирование\" - (/update)");
        if (context.getUser().isAuthority()) {
            return StateType.PROFILE_UPDATE;
        } else {
            return StateType.PROFILE;
        }
    }

    private StateType executeSingInCommand(BotContext context) {
        log.debug("Выполнение сценария \"Войти\" - (/sing_In)");
        if (!context.getUser().isAuthority()) {
            return StateType.SING_IN;
        } else {
            return StateType.PROFILE;
        }
    }

    private StateType executeSingUpCommand(BotContext context) {
        log.debug("Выполнение сценария \"Новая\" - (/sing_up)");
        if (!context.getUser().isAuthority()) {
            return StateType.SING_UP;
        } else {
            return StateType.PROFILE;
        }
    }

    private StateType executeSingOutCommand(BotContext context) {
        log.debug("Выполнение сценария \"Выйти\" - (/sing_out)");
        if (context.getUser().isAuthority()) {
            User user = context.getUser();
            user.setProfile(null);
            userService.save(user);
            log.debug("Сохранение пользователя - '{}'", user);
            sendTextMessage(context, "Выход выполнен");
        }
        return StateType.PROFILE;
    }

    private StateType executeExitCommand() {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
        return StateType.START;
    }

    private enum Commands {
        HELP,
        UPDATE,
        SING_IN,
        SING_UP,
        SING_OUT,
        EXIT
    }
}
