package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.league.tinder.bot.BotContext;

import java.util.Optional;

@Component
public class SingUpState extends BaseState {

    private static final Logger log = LoggerFactory.getLogger(SingUpState.class);

    @Override
    public void of(BotContext context) {
        log.debug("Обработка контекста - '{}'", context);
        Optional<Commands> inputCommand = getCommand(context.getInput());
        inputCommand.ifPresent(command -> execute(command, context));
    }

    @Override
    public void enter(BotContext context) {

    }

    private void execute(Commands command, BotContext context) {
        log.debug("Получена команда - '{}'. Определение сценария выполнения.", command);
        switch (command) {
            case HELP: {
                executeHelpCommand(context);
                break;
            }

            case SING_UP: {
                executeSingUpCommand(context);
                break;
            }

            case EXIT: {
                executeExitCommand(context);
                break;
            }

            default: {
                log.warn("Не задано исполение для команды - '{}'!", command);
            }
        }
    }

    private Optional<Commands> getCommand(String input) {
        try {
            if (input.split(" ").length == 3) {
                return Optional.of(Commands.SING_UP);
            }
            return Optional.of(Commands.valueOf(input.toUpperCase().replaceFirst("/", "")));
        } catch (IllegalArgumentException e) {
            log.warn("Введена некоректная команда - '{}'!", input);
            return Optional.empty();
        }
    }

    private void executeHelpCommand(BotContext context) {
        log.debug("Выполнение сценария \"Подсказки\" - (/help)");
        sendTextMessage(context, "Коль сударь иль сударыня заплутали:\n" +
                "---------------------------------------\n" +
                "Сударь иль сударыня введите  логинъ  и пароль черезъ пробѣлъ:\n" +
                "---------------------------------------\n" +
                "/exit - Вернуться");
    }

    private void executeSingUpCommand(BotContext context) {
        log.debug("Выполнение сценария \"Регистрация\" - (/exit)");

    }


    private void executeExitCommand(BotContext context) {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
    }

    private enum Commands {
        HELP,
        SING_UP,
        EXIT;
    }
}
