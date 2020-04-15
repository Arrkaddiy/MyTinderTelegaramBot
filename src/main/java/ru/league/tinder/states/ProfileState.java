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

    private StateType state;

    private UserService userService;

    public ProfileState(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        state = StateType.PROFILE;
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
    public void handleInput(BotContext context) {
        log.debug("Обработка контекста - '{}'", context);
        Optional<Commands> inputCommand = getCommand(context.getInput());
        inputCommand.ifPresent(command -> execute(command, context));
    }

    @Override
    public StateType getState() {
        return state;
    }

    private void execute(Commands command, BotContext context) {
        log.debug("Получена команда - '{}'. Определение сценария выполнения.", command);
        switch (command) {
            case HELP: {
                executeHelpCommand(context);
                break;
            }

            case UPDATE: {
                executeUpdateCommand(context);
                break;
            }

            case SING_IN: {
                executeSingInCommand(context);
                break;
            }

            case SING_UP: {
                executeSingUpCommand(context);
                break;
            }

            case SING_OUT: {
                executeSingOutCommand(context);
                break;
            }

            case EXIT: {
                executeExitCommand();
                break;
            }

            default: {
                log.warn("Не задано исполение для команды - '{}'!", command);
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

    private void executeHelpCommand(BotContext context) {
        log.debug("Выполнение сценария \"Подсказки\" - (/help)");
        state = StateType.PROFILE;
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

    private void executeUpdateCommand(BotContext context) {
        log.debug("Выполнение сценария \"Редактирование\" - (/update)");
        if (context.getUser().isAuthority()) {
            state = StateType.PROFILE_UPDATE;
        } else {
            state = StateType.PROFILE;
        }
    }

    private void executeSingInCommand(BotContext context) {
        log.debug("Выполнение сценария \"Войти\" - (/sing_In)");
        if (!context.getUser().isAuthority()) {
            state = StateType.SING_IN;
        } else {
            state = StateType.PROFILE;
        }
    }

    private void executeSingUpCommand(BotContext context) {
        log.debug("Выполнение сценария \"Новая\" - (/sing_up)");
        if (!context.getUser().isAuthority()) {
            state = StateType.SING_UP;
        } else {
            state = StateType.PROFILE;
        }
    }

    private void executeSingOutCommand(BotContext context) {
        log.debug("Выполнение сценария \"Выйти\" - (/sing_out)");
        state = StateType.PROFILE;
        if (context.getUser().isAuthority()) {
            User user = context.getUser();
            user.setProfile(null);
            userService.save(user);
        }
    }

    private void executeExitCommand() {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
        state = StateType.START;
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
