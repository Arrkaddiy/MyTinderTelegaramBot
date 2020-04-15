package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.league.tinder.bot.BotContext;

import java.util.Optional;

@Component
public class ProfileState extends BaseState {

    private static final Logger log = LoggerFactory.getLogger(StartState.class);

    @Override
    public void of(BotContext context) {
        log.debug("Обработка контекста - '{}'", context);
        Optional<Commands> inputCommand = getCommand(context.getInput());
        inputCommand.ifPresent(command -> execute(command, context));
    }

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        sendTextMessage(context, "Создание и редактирование вашей анкеты.");
    }

    private void execute(Commands command, BotContext context) {
        log.debug("Получена команда - '{}'. Определение сценария выполнения.", command);
        switch (command) {
            case HELP: {
                executeHelpCommand(context);
                break;
            }

            case SING_IN: {
                executeSingInCommand(context);
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
            return Optional.of(Commands.valueOf(input.toUpperCase().replaceFirst("/", "")));
        } catch (IllegalArgumentException e) {
            log.warn("Введена некоректная команда - '{}'!", input);
            return Optional.empty();
        }
    }

    private void executeHelpCommand(BotContext context) {
        log.debug("Выполнение сценария \"Подсказки\" - (/help)");
        sendTextMessage(context, "Коль сударь иль сударыня заплутали:\n" +
                "/sing_in - Войти\n" +
                "/sing_up - Новая\n" +
                "/exit - Вернуться");
    }

    private void executeSingInCommand(BotContext context) {
        log.debug("Выполнение сценария \"Войти\" - (/profile)");
    }

    private void executeSingUpCommand(BotContext context) {
        log.debug("Выполнение сценария \"Новая\" - (/profile)");
    }

    private void executeExitCommand(BotContext context) {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
    }

    private enum Commands {
        HELP,
        SING_IN,
        SING_UP,
        EXIT;
    }
}
