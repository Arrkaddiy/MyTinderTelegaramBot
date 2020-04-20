package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.league.tinder.bot.RequestContext;
import ru.league.tinder.bot.ResponseContext;
import ru.league.tinder.entity.User;
import ru.league.tinder.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ProfileState implements State {

    private static final Logger log = LoggerFactory.getLogger(StartState.class);

    private UserService userService;

    public ProfileState(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String enter(RequestContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        if (context.getUser().isAuthority()) {
            return "Создание и редактирование вашей анкеты.\n" +
                    "/update - Редактирование профиля\n" +
                    "/sing_out - Выйти\n" +
                    "/exit - Вернуться";
        } else {
            return "Создание и редактирование вашей анкеты.\n" +
                    "/sing_in - Войти\n" +
                    "/sing_up - Новая\n" +
                    "/exit - Вернуться";
        }
    }

    @Override
    public ResponseContext nextState(RequestContext context) {
        log.debug("Обработка контекста - '{}'", context);
        Commands inputCommand = getCommand(context.getInput()).orElse(Commands.HELP);
        log.debug("Определена команда - '{}'", inputCommand);
        return execute(inputCommand, context);
    }

    private ResponseContext execute(Commands command, RequestContext context) {
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
                return new ResponseContext(StateType.PROFILE, "NaN");
            }
        }
    }

    private ReplyKeyboardMarkup getButtonAuthority() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("/update"));

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("/sing_out"));

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("/exit"));

        KeyboardRow keyboardRow4 = new KeyboardRow();
        keyboardRow4.add(new KeyboardButton("/help"));

        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);
        keyboardRowList.add(keyboardRow3);
        keyboardRowList.add(keyboardRow4);


        keyboardMarkup.setKeyboard(keyboardRowList);
        return keyboardMarkup;
    }

    private ReplyKeyboardMarkup getButtonNotAuthority() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("/sing_in"));

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("/sing_up"));

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("/exit"));

        KeyboardRow keyboardRow4 = new KeyboardRow();
        keyboardRow4.add(new KeyboardButton("/help"));

        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);
        keyboardRowList.add(keyboardRow3);
        keyboardRowList.add(keyboardRow4);

        keyboardMarkup.setKeyboard(keyboardRowList);
        return keyboardMarkup;
    }

    private Optional<Commands> getCommand(String input) {
        try {
            return Optional.of(Commands.valueOf(input.toUpperCase().replaceFirst("/", "")));
        } catch (IllegalArgumentException e) {
            log.warn("Введена некоректная команда - '{}'!", input);
            return Optional.empty();
        }
    }

    private ResponseContext executeHelpCommand(RequestContext context) {
        log.debug("Выполнение сценария \"Подсказки\" - (/help)");
        String answer;
        if (context.getUser().isAuthority()) {
            answer = "Создание и редактирование вашей анкеты.\n" +
                    "/update - Редактирование профиля\n" +
                    "/sing_out - Выйти\n" +
                    "/exit - Вернуться";
        } else {
            answer = "Создание и редактирование вашей анкеты.\n" +
                    "/sing_in - Войти\n" +
                    "/sing_up - Новая\n" +
                    "/exit - Вернуться";
        }

        return new ResponseContext(StateType.PROFILE, answer);
    }

    private ResponseContext executeUpdateCommand(RequestContext context) {
        log.debug("Выполнение сценария \"Редактирование\" - (/update)");
        if (context.getUser().isAuthority()) {
            return new ResponseContext(StateType.PROFILE_UPDATE, "NaN");
        } else {
            return new ResponseContext(StateType.PROFILE, "NaN");
        }
    }

    private ResponseContext executeSingInCommand(RequestContext context) {
        log.debug("Выполнение сценария \"Войти\" - (/sing_In)");
        if (!context.getUser().isAuthority()) {
            return new ResponseContext(StateType.SING_IN, "NaN");
        } else {
            return new ResponseContext(StateType.PROFILE, "NaN");
        }
    }

    private ResponseContext executeSingUpCommand(RequestContext context) {
        log.debug("Выполнение сценария \"Новая\" - (/sing_up)");
        if (!context.getUser().isAuthority()) {
            return new ResponseContext(StateType.SING_UP, "NaN");
        } else {
            return new ResponseContext(StateType.PROFILE, "NaN");
        }
    }

    private ResponseContext executeSingOutCommand(RequestContext context) {
        log.debug("Выполнение сценария \"Выйти\" - (/sing_out)");
        if (context.getUser().isAuthority()) {
            User user = context.getUser();
            user.setProfile(null);
            user.setLastLookProfile(null);
            userService.save(user);
            log.debug("Сохранение пользователя - '{}'", user);
        }
        return new ResponseContext(StateType.PROFILE, "Выход выполнен");
    }

    private ResponseContext executeExitCommand() {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
        return new ResponseContext(StateType.START, "NaN");
    }

    private enum Commands {
        START,
        HELP,
        UPDATE,
        SING_IN,
        SING_UP,
        SING_OUT,
        EXIT
    }
}
