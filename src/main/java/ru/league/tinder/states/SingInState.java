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
public class SingInState implements State {

    private static final Logger log = LoggerFactory.getLogger(SingUpState.class);

    private UserService userService;
    private ProfileService profileService;

    public SingInState(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    @Override
    public String enter(RequestContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        return "Сударь иль сударыня, введите логинъ и пароль черезъ пробѣлъ:";
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
                return executeHelpCommand();
            }

            case AUTHORITY: {
                return executeAuthorityCommand(context);
            }

            case EXIT: {
                return executeExitCommand();
            }

            default: {
                log.warn("Не задано исполение для команды - '{}'!", command);
                return new ResponseContext(StateType.SING_IN, "NaN");
            }
        }
    }

    private Optional<Commands> getCommand(String input) {
        try {
            if (input.split("\\s").length == 2) {
                return Optional.of(Commands.AUTHORITY);
            }

            return Optional.of(Commands.valueOf(input.toUpperCase().replaceFirst("/", "")));
        } catch (IllegalArgumentException e) {
            log.warn("Введена некоректная команда - '{}'!", input);
            return Optional.empty();
        }
    }

    private ResponseContext executeHelpCommand() {
        log.debug("Выполнение сценария \"Подсказки\" - (/help)");
        return new ResponseContext(StateType.SING_IN, "Коль сударь иль сударыня заплутали:\n" +
                "---------------------------------------\n" +
                "сударь иль сударыня введите  логинъ  и пароль черезъ пробѣлъ:\n" +
                "---------------------------------------\n" +
                "/exit - Вернуться");
    }

    private ResponseContext executeAuthorityCommand(RequestContext context) {
        log.debug("Выполнение сценария \"Вход\" - (/sing_in)");
        String[] params = context.getInput().split("\\s");
        String name = params[0];
        log.debug("Имя - '{}'", name);
        String pass = params[1];

        Profile profileBuf = new Profile("E", name, pass);
        Profile profile = profileService.authority(profileBuf.getName(), profileBuf.getHashPassword());
        log.debug("Авторизация профиля - '{}'", profile);

        if (profile != null) {
            log.debug("Авторизация прошла успешно по name - '{}'", params[0]);
            User user = context.getUser();
            user.setProfile(profile);
            user.setLastLookProfile(null);
            userService.save(user);
            log.debug("Сохранение профиля пользователя - '{}'", user);

            return new ResponseContext(StateType.LEFT, "Успехъ");

        } else {
            log.debug("Авторизация не прошла по name - '{}'", params[0]);
            return new ResponseContext(StateType.SING_IN, "Неудача");
        }
    }

    private ResponseContext executeExitCommand() {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
        return new ResponseContext(StateType.PROFILE, "NaN");
    }

    private enum Commands {
        START,
        HELP,
        AUTHORITY,
        EXIT
    }

}
