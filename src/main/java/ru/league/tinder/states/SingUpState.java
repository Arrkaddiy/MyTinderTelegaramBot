package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.league.tinder.bot.BotContext;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.entity.User;
import ru.league.tinder.service.ProfileService;
import ru.league.tinder.service.UserService;

import java.util.Optional;

@Component
public class SingUpState implements State, StateSendMessage {

    private static final Logger log = LoggerFactory.getLogger(SingUpState.class);

    private StateType state;

    private UserService userService;
    private ProfileService profileService;

    public SingUpState(UserService userService, ProfileService profileService) {
        this.userService = userService;
        this.profileService = profileService;
    }

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        state = StateType.SING_UP;
        sendTextMessage(context, "Вы сударь иль сударыня? Как вас величать? Ваш секретный шифръ?");
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

            case SING_UP: {
                executeSingUpCommand(context);
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
                "Вы сударь иль сударыня? Как вас величать? Ваш секретный шифръ?\n" +
                "---------------------------------------\n" +
                "/exit - Вернуться");
    }

    private void executeSingUpCommand(BotContext context) {
        log.debug("Выполнение сценария \"Регистрация\" - (/sing_up)");
        String[] params = context.getInput().split(" ");
        if (params[0].equalsIgnoreCase("сударь") || params[0].equalsIgnoreCase("сударыня")) {
            state = StateType.SING_UP;
            Profile profile = new Profile(params[0].equalsIgnoreCase("сударь") ? "M" : "F", params[1], params[2]);
            profileService.save(profile);
            User user = context.getUser();
            user.setProfile(profile);
            userService.save(user);
            sendTextMessage(context, "Успехъ");
//            state = StateType.LEFT;
            state = StateType.START;
        } else {
            state = StateType.SING_UP;
            sendTextMessage(context, "Неудача:\n" +
                    "сударь Мечтательный-анархистъ д0л0йцарR");
        }
    }


    private void executeExitCommand() {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
        state = StateType.PROFILE;
    }

    private enum Commands {
        HELP,
        SING_UP,
        EXIT
    }
}
