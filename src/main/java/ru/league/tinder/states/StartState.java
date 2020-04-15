package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.league.tinder.bot.BotContext;

import java.util.Optional;

@Component
public class StartState implements State, StateSendMessage {

    private static final Logger log = LoggerFactory.getLogger(StartState.class);

    private StateType state;

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние - '{}'", context.getUser().getState());
        state = StateType.START;
        sendTextMessage(context, "Мстительный авантюрист мечтает\n" +
                "отойти от дел в уютной усадьбе\n" +
                "с любимой женщиной\n" +
                "------------------------------\n" +
                "/left - Следующая анкета\n" +
                "/right - Подтвердить свой интерес\n" +
                "/profile - Создание и редактирование вашей анкеты\n" +
                "/favorites - Показать любимцев\n" +
                "------------------------------\n" +
                "/help - вам в помощь");
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

            case LEFT: {
                executeLeftCommand(context);
                break;
            }

            case RIGHT: {
                executeRightCommand(context);
                break;
            }

            case PROFILE: {
                executeProfileCommand();
                break;
            }

            case FAVORITES: {
                executeFavoritesCommand();
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
                "/left - Следующая анкета\n" +
                "/right - Подтвердить свой интерес\n" +
                "/profile - Создание и редактирование вашей анкеты\n" +
                "/favorites - Показать любимцев");
    }

    private void executeLeftCommand(BotContext context) {
        log.debug("Выполнение сценария \"Следующая анкета\" - (/left)");
        state = StateType.START;
    }

    private void executeRightCommand(BotContext context) {
        log.debug("Выполнение сценария \"Подтверждения интереса\" - (/right)");
        state = StateType.START;
    }

    private void executeProfileCommand() {
        log.debug("Выполнение сценария \"Анкета\" - (/profile)");
        state = StateType.PROFILE;
    }

    private void executeFavoritesCommand() {
        log.debug("Выполнение сценария \"Любимцы\" - (/favorites)");
        state = StateType.FAVORITES;
    }

    private enum Commands {
        HELP,
        LEFT,
        RIGHT,
        PROFILE,
        FAVORITES
    }
}
