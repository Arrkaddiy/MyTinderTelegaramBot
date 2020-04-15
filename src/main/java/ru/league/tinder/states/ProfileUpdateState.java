package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.league.tinder.bot.BotContext;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.service.ProfileService;

import java.util.Optional;

@Component
public class ProfileUpdateState implements State, StateSendMessage {

    private static final Logger log = LoggerFactory.getLogger(SingUpState.class);

    private StateType state;

    private ProfileService profileService;

    public ProfileUpdateState(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        state = StateType.PROFILE_UPDATE;
        if (context.getUser().getProfile().getSex().equalsIgnoreCase("M")) {
            sendTextMessage(context, "Сударь, опишите себя");
        } else {
            sendTextMessage(context, "Сударыня, опишите себя");
        }

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

            case UPDATE: {
                executeUpdateCommand(context);
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
            return Optional.of(Commands.UPDATE);
        }
    }

    private void executeHelpCommand(BotContext context) {
        log.debug("Выполнение сценария \"Подсказки\" - (/help)");
        state = StateType.PROFILE_UPDATE;
        sendTextMessage(context, "Коль сударь иль сударыня заплутали:\n" +
                "---------------------------------------\n" +
                "Опишите себя\n" +
                "---------------------------------------\n" +
                "/exit - Вернуться");
    }

    private void executeUpdateCommand(BotContext context) {
        log.debug("Выполнение сценария \"Редактирование\" - (/update)");
        state = StateType.PROFILE;
        Profile profile = context.getUser().getProfile();
        profile.setAbout(context.getInput());
        profileService.save(profile);
    }

    private void executeExitCommand() {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
        state = StateType.PROFILE;
    }

    private enum Commands {
        HELP,
        UPDATE,
        EXIT
    }
}
