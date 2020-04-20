package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.league.tinder.bot.RequestContext;
import ru.league.tinder.bot.ResponseContext;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.entity.User;
import ru.league.tinder.service.ProfileService;
import ru.league.tinder.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SingUpState implements State {

    private static final Logger log = LoggerFactory.getLogger(SingUpState.class);

    private UserService userService;
    private ProfileService profileService;

    public SingUpState(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    @Override
    public String enter(RequestContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        return "Вы сударь иль сударыня? Как вас величать? Ваш секретный шифръ?";
    }

    @Override
    public ResponseContext nextState(RequestContext context) {
        log.debug("Обработка контекста - '{}'", context);
        Commands inputCommand = getCommand(context.getInput()).orElse(Commands.HELP);
        log.debug("Определена команда - '{}'", inputCommand);
        return execute(inputCommand, context);
    }

    private ReplyKeyboardMarkup getButton() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("/exit"));

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("/help"));

        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);

        keyboardMarkup.setKeyboard(keyboardRowList);
        return keyboardMarkup;
    }

    private ResponseContext execute(Commands command, RequestContext context) {
        log.debug("Получена команда - '{}'. Определение сценария выполнения.", command);
        switch (command) {
            case HELP: {
                return executeHelpCommand(context);
            }

            case SING_UP: {
                return executeSingUpCommand(context);
            }

            case EXIT: {
                return executeExitCommand();
            }

            default: {
                log.warn("Не задано исполение для команды - '{}'!", command);
                return new ResponseContext(StateType.SING_UP, "NaN");
            }
        }
    }

    private Optional<Commands> getCommand(String input) {
        try {
            if (input.split("\\s").length == 3) {
                return Optional.of(Commands.SING_UP);
            }
            return Optional.of(Commands.valueOf(input.toUpperCase().replaceFirst("/", "")));
        } catch (IllegalArgumentException e) {
            log.warn("Введена некоректная команда - '{}'!", input);
            return Optional.empty();
        }
    }

    private ResponseContext executeHelpCommand(RequestContext context) {
        log.debug("Выполнение сценария \"Подсказки\" - (/help)");
        return new ResponseContext(StateType.SING_UP, "Коль сударь иль сударыня заплутали:\n" +
                "---------------------------------------\n" +
                "Вы сударь иль сударыня? Как вас величать? Ваш секретный шифръ?\n" +
                "---------------------------------------\n" +
                "/exit - Вернуться");
    }

    private ResponseContext executeSingUpCommand(RequestContext context) {
        log.debug("Выполнение сценария \"Регистрация\" - (/sing_up)");
        String[] params = context.getInput().split("\\s");
        String sex = params[0];
        log.debug("Пол - '{}'", sex);
        String name = params[1];
        log.debug("Имя - '{}'", name);
        String pass = params[2];

        if (sex.equalsIgnoreCase("сударь") || sex.equalsIgnoreCase("сударыня")) {
            Profile profile = new Profile(sex.equalsIgnoreCase("сударь") ? "M" : "F", name, pass);
            log.debug("Создан новый профиль - '{}'", profile);
            return singUp(profile, context);

        } else {
            log.warn("Некорректный ввод - '{}'", context.getInput());
            return new ResponseContext(StateType.SING_UP, "Неудача:\n" + "сударь МечтательныйАнархистъ д0л0йцарR");
        }
    }

    private ResponseContext singUp(Profile profile, RequestContext context) {
        log.debug("Попытка зарегестрировать - '{}'", profile);
        if (!profileService.isBusy(profile.getName())) {
            profileService.save(profile);
            log.debug("Сохранен новый профиль - '{}'", profile);

            User user = context.getUser();
            user.setProfile(profile);
            user.setLastLookProfile(null);
            userService.save(user);
            log.debug("Сохранение профиля под пользователем - '{}'", user);
            return new ResponseContext(StateType.LEFT, "Успехъ");

        } else {
            log.warn("Данное имя уже занято! - '{}'", profile.getName());
            return new ResponseContext(StateType.SING_UP, "Неудача");
        }
    }

    private ResponseContext executeExitCommand() {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
        return new ResponseContext(StateType.PROFILE, "Nan");
    }

    private enum Commands {
        START,
        HELP,
        SING_UP,
        EXIT
    }
}
