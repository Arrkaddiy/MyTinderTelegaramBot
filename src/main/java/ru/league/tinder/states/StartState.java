package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.league.tinder.bot.RequestContext;
import ru.league.tinder.bot.ResponseContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class StartState implements State {

    private static final Logger log = LoggerFactory.getLogger(StartState.class);

    @Override
    public String enter(RequestContext context) {
        log.debug("Выполнение сценария перехода на состояние - '{}'", context.getUser().getState());
        return "Мстительный авантюрист мечтает\n" +
                "отойти от дел в уютной усадьбе\n" +
                "с любимой женщиной";
    }

    @Override
    public ResponseContext nextState(RequestContext context) {
        log.debug("Обработка контекста - '{}'", context);
        Commands inputCommand = getCommand(context.getInput()).orElse(Commands.HELP);
        log.debug("Определена команда - '{}'", inputCommand);
        return execute(inputCommand, context);
    }

    public ReplyKeyboardMarkup getButton() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("/left"));

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("/profile"));

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("/favorites"));

        KeyboardRow keyboardRow4 = new KeyboardRow();
        keyboardRow4.add(new KeyboardButton("/help"));

        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);
        keyboardRowList.add(keyboardRow3);
        keyboardRowList.add(keyboardRow4);

        keyboardMarkup.setKeyboard(keyboardRowList);
        return keyboardMarkup;
    }

    private ResponseContext execute(Commands command, RequestContext context) {
        log.debug("Получена команда - '{}'. Определение сценария выполнения.", command);
        switch (command) {
            case HELP: {
                return executeHelpCommand(context);
            }

            case LEFT: {
                return executeLeftCommand();
            }

            case PROFILE: {
                return executeProfileCommand();
            }

            case FAVORITES: {
                return executeFavoritesCommand();
            }

            default: {
                log.warn("Незадано исполение для команды - '{}'!", command);
                return new ResponseContext(StateType.START, "NaN");
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

    private ResponseContext executeHelpCommand(RequestContext context) {
        log.debug("Выполнение сценария \"Подсказки\" - (/help)");
        return new ResponseContext(StateType.START, "Коль сударь иль сударыня заплутали:\n" +
                "/left - Следующая анкета\n" +
                "/profile - Создание и редактирование вашей анкеты\n" +
                "/favorites - Показать любимцев\n" +
                "--------------------------------\n" +
                "/help - Вам в помощь");
    }

    private ResponseContext executeLeftCommand() {
        log.debug("Выполнение сценария \"Следующая анкета\" - (/left)");
        StateType state = StateType.LEFT;
        log.debug("Переход на сдадию - '{}'", state);
        return new ResponseContext(state, "NaN");
    }

    private ResponseContext executeProfileCommand() {
        log.debug("Выполнение сценария \"Анкета\" - (/profile)");
        StateType state = StateType.PROFILE;
        log.debug("Переход на сдадию - '{}'", state);
        return new ResponseContext(state, "NaN");
    }

    private ResponseContext executeFavoritesCommand() {
        log.debug("Выполнение сценария \"Любимцы\" - (/favorites)");
        StateType state = StateType.FAVORITES;
        log.debug("Переход на сдадию - '{}'", state);
        return new ResponseContext(state, "NaN");
    }

    private enum Commands {
        START,
        HELP,
        LEFT,
        PROFILE,
        FAVORITES
    }
}
