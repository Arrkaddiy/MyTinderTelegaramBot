package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.league.tinder.bot.BotContext;
import ru.league.tinder.entity.Mach;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.entity.User;
import ru.league.tinder.service.MachService;
import ru.league.tinder.service.UserService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FavoritesState implements State, StateSendMessage {

    private static final Logger log = LoggerFactory.getLogger(FavoritesState.class);

    private UserService userService;
    private MachService machService;

    public FavoritesState(UserService userService, MachService machService) {
        this.userService = userService;
        this.machService = machService;
    }

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        String favors = "";
        if (context.getUser().isAuthority()) {
            List<Mach> machList = getMach(context);
            if (machList.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < machList.size(); i++) {
                    stringBuilder.append(i + 1).append(". ").append(machList.get(i).getTo().getName()).append("\n");
                }
                favors = stringBuilder.toString();
            }
        }
        sendTextMessageWithKey(context, "Любимцы:\n" + favors, getButton());
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

            case NUMBER: {
                return executeNumberCommand(context);
            }

            case EXIT: {
                return executeExitCommand();
            }

            default: {
                log.warn("Не задано исполение для команды - '{}'!", command);
                return StateType.FAVORITES;
            }
        }
    }

    private Optional<Commands> getCommand(String input) {
        try {
            if (input.matches("\\d+")) {
                return Optional.of(Commands.NUMBER);
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
                "Введите номер профиля для просмотра анкеты\n" +
                "---------------------------------------\n" +
                "/exit - Вернуться");

        return StateType.FAVORITES;
    }

    private StateType executeNumberCommand(BotContext context) {
        log.debug("Выполнение сценария \"Просмотр анкеты\" - (/number)");
        int number = Integer.parseInt(context.getInput()) - 1;
        List<Mach> machList = getMach(context);
        if (machList.size() > number) {
            Profile profile = machList.get(number).getTo();
            log.debug("Получен интерисующий профиль - '{}'", profile);
            User user = context.getUser();
            user.setLastLookProfile(profile);
            log.debug("Сохранение интерисующего профиля - '{}'", profile);
            userService.save(user);
            return StateType.LOOK_PROFILE;
        } else {
            log.warn("Выбрана не существующая запись!");
            return StateType.FAVORITES;
        }
    }

    private StateType executeExitCommand() {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
        return StateType.START;
    }

    private List<Mach> getMach(BotContext context) {
        List<Mach> machList = machService.findAllMach(context.getUser().getProfile());
        log.debug("Получены записей любимцев текущего пользователя в количестве - '{}'шт.", machList.size());
        return machList.stream()
                .sorted(Comparator.comparing(mach -> mach.getTo().getName()))
                .collect(Collectors.toList());
    }

    private enum Commands {
        START,
        HELP,
        NUMBER,
        EXIT
    }
}
