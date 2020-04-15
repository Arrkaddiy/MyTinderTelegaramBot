package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.league.tinder.bot.BotContext;

import java.util.Optional;

@Component
public class LookProfileState implements State, StateSendMessage {

    private static final Logger log = LoggerFactory.getLogger(LookProfileState.class);

    private StateType state;

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        state = StateType.LOOK_PROFILE;
        sendTextMessage(context, getAboutProfile(context));
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

    private String getAboutProfile(BotContext context) {
        int profileNumber = Integer.parseInt(context.getInput());
        return null;
    }

    private void execute(Commands command, BotContext context) {
        log.debug("Получена команда - '{}'. Определение сценария выполнения.", command);
        switch (command) {
            case HELP: {
                executeHelpCommand(context);
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
        state = StateType.LOOK_PROFILE;
        sendTextMessage(context, "Коль сударь иль сударыня заплутали:\n" +
                "/exit - Вернуться");
    }


    private void executeExitCommand() {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
        state = StateType.FAVORITES;
    }

    private enum Commands {
        HELP,
        EXIT
    }
}
