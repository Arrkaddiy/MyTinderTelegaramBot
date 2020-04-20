package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.league.tinder.bot.BotContext;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.entity.User;
import ru.league.tinder.service.ProfileService;
import ru.league.tinder.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class SingInState implements State, StateSendMessage {

    private static final Logger log = LoggerFactory.getLogger(SingUpState.class);

    private UserService userService;
    private ProfileService profileService;

    public SingInState(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        sendTextMessageWithKey(context, "Сударь иль сударыня, введите логинъ и пароль черезъ пробѣлъ:", getButton());
    }

    @Override
    public StateType nextState(BotContext context) {
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

    private StateType execute(Commands command, BotContext context) {
        log.debug("Получена команда - '{}'. Определение сценария выполнения.", command);
        switch (command) {
            case HELP: {
                return executeHelpCommand(context);
            }

            case AUTHORITY: {
                return executeAuthorityCommand(context);
            }

            case EXIT: {
                return executeExitCommand();
            }

            default: {
                log.warn("Не задано исполение для команды - '{}'!", command);
                return StateType.SING_IN;
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

    private StateType executeHelpCommand(BotContext context) {
        log.debug("Выполнение сценария \"Подсказки\" - (/help)");
        sendTextMessage(context, "Коль сударь иль сударыня заплутали:\n" +
                "---------------------------------------\n" +
                "сударь иль сударыня введите  логинъ  и пароль черезъ пробѣлъ:\n" +
                "---------------------------------------\n" +
                "/exit - Вернуться");
        return StateType.SING_IN;
    }

    private StateType executeAuthorityCommand(BotContext context) {
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

            sendTextMessage(context, "Успехъ");
            return StateType.LEFT;

        } else {
            log.debug("Авторизация не прошла по name - '{}'", params[0]);
            sendTextMessage(context, "Неудача");
            return StateType.SING_IN;
        }
    }

    private StateType executeExitCommand() {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
        return StateType.PROFILE;
    }

    private enum Commands {
        HELP,
        AUTHORITY,
        EXIT
    }

}
