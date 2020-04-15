package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.league.tinder.bot.BotContext;

import java.util.Optional;

@Component
public class FavoritesState implements State, StateSendMessage {

    private static final Logger log = LoggerFactory.getLogger(FavoritesState.class);

    private StateType state;

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        state = StateType.FAVORITES;
        sendTextMessage(context, "Любимцы:");
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

            case NUMBER: {
                executeNumberCommand(context);
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
            if (input.matches("\\d+")) {
                return Optional.of(Commands.NUMBER);
            }

            return Optional.of(Commands.valueOf(input.toUpperCase().replaceFirst("/", "")));
        } catch (IllegalArgumentException e) {
            log.warn("Введена некоректная команда - '{}'!", input);
            return Optional.empty();
        }
    }

    private void executeHelpCommand(BotContext context) {
        log.debug("Выполнение сценария \"Подсказки\" - (/help)");
        state = StateType.FAVORITES;
        sendTextMessage(context, "Коль сударь иль сударыня заплутали:\n" +
                "---------------------------------------\n" +
                "Введите номер профиля для просмотра анкеты\n" +
                "---------------------------------------\n" +
                "/exit - Вернуться");
    }

    private void executeNumberCommand(BotContext context) {
        log.debug("Выполнение сценария \"Просмотр анкеты\" - (/number)");
        state = StateType.LOOK_PROFILE;
    }

    private void executeExitCommand() {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
        state = StateType.START;
    }

    private enum Commands {
        HELP,
        NUMBER,
        EXIT
    }
}
