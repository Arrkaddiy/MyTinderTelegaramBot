package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.league.tinder.bot.BotContext;
import ru.league.tinder.service.StateService;

import java.util.Optional;

@Component
public class StartState extends BaseState {

    private static final Logger log = LoggerFactory.getLogger(StartState.class);

    private StateService stateService;

    @Override
    public void of(BotContext context) {
        log.debug("Обработка контекста - '{}'", context);
        Optional<Commands> inputCommand = getCommand(context.getInput());
        inputCommand.ifPresent(command -> execute(command, context));
    }

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние - '{}'", context.getUser().getState());
        sendTextMessage(context, "Мстительный авантюрист мечтает\n" +
                "отойти от дел в уютной усадьбе\n" +
                "с любимой женщиной\n" +
                "------------------------------\n" +
                "/help - вам в помощь");
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
                executeProfileCommand(context);
                break;
            }

            case FAVORITES: {
                executeFavoritesCommand(context);
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

    }

    private void executeRightCommand(BotContext context) {
        log.debug("Выполнение сценария \"Подтверждения интереса\" - (/right)");

    }

    private void executeProfileCommand(BotContext context) {
        log.debug("Выполнение сценария \"Анкета\" - (/profile)");

    }

    private void executeFavoritesCommand(BotContext context) {
        log.debug("Выполнение сценария \"Любимцы\" - (/favorites)");

    }

    private enum Commands {
        HELP,
        LEFT,
        RIGHT,
        PROFILE,
        FAVORITES;
    }
}
