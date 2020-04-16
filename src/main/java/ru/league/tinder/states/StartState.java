package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.league.tinder.bot.BotContext;

import java.util.Optional;

@Component
public class StartState implements State, StateSendMessage {

    private static final Logger log = LoggerFactory.getLogger(StartState.class);

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние - '{}'", context.getUser().getState());
        sendTextMessage(context, "Мстительный авантюрист мечтает\n" +
                "отойти от дел в уютной усадьбе\n" +
                "с любимой женщиной\n" +
                "------------------------------\n" +
                "/left - Следующая анкета\n" +
                "/profile - Создание и редактирование вашей анкеты\n" +
                "/favorites - Показать любимцев\n" +
                "------------------------------\n" +
                "/help - вам в помощь");
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
                return StateType.START;
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

    private StateType executeHelpCommand(BotContext context) {
        log.debug("Выполнение сценария \"Подсказки\" - (/help)");
        sendTextMessage(context, "Коль сударь иль сударыня заплутали:\n" +
                "/left - Следующая анкета\n" +
                "/profile - Создание и редактирование вашей анкеты\n" +
                "/favorites - Показать любимцев");

        return StateType.START;
    }

    private StateType executeLeftCommand() {
        log.debug("Выполнение сценария \"Следующая анкета\" - (/left)");
        StateType state = StateType.LEFT;
        log.debug("Переход на сдадию - '{}'", state);
        return state;
    }

    private StateType executeProfileCommand() {
        log.debug("Выполнение сценария \"Анкета\" - (/profile)");
        StateType state = StateType.PROFILE;
        log.debug("Переход на сдадию - '{}'", state);
        return state;
    }

    private StateType executeFavoritesCommand() {
        log.debug("Выполнение сценария \"Любимцы\" - (/favorites)");
        StateType state = StateType.FAVORITES;
        log.debug("Переход на сдадию - '{}'", state);
        return state;
    }

    private enum Commands {
        HELP,
        LEFT,
        PROFILE,
        FAVORITES
    }
}
