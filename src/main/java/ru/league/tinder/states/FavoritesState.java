package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.league.tinder.bot.BotContext;

import java.util.Optional;

@Component
public class FavoritesState implements State, StateSendMessage {

    private static final Logger log = LoggerFactory.getLogger(FavoritesState.class);

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        sendTextMessage(context, "Любимцы:");
    }

    @Override
    public StateType handleInput(BotContext context) {
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
        return StateType.LOOK_PROFILE;
    }

    private StateType executeExitCommand() {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
        return StateType.START;
    }

    private enum Commands {
        HELP,
        NUMBER,
        EXIT
    }
}
