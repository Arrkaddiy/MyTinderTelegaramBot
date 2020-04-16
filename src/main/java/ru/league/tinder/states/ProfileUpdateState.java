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

    private ProfileService profileService;

    public ProfileUpdateState(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        if (context.getUser().getProfile().getSex().equalsIgnoreCase("M")) {
            sendTextMessage(context, "Сударь, опишите себя");
        } else {
            sendTextMessage(context, "Сударыня, опишите себя");
        }
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

            case UPDATE: {
                return executeUpdateCommand(context);
            }

            case EXIT: {
                return executeExitCommand();
            }

            default: {
                log.warn("Не задано исполение для команды - '{}'!", command);
                return StateType.PROFILE_UPDATE;
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

    private StateType executeHelpCommand(BotContext context) {
        log.debug("Выполнение сценария \"Подсказки\" - (/help)");
        sendTextMessage(context, "Коль сударь иль сударыня заплутали:\n" +
                "---------------------------------------\n" +
                "Опишите себя\n" +
                "---------------------------------------\n" +
                "/exit - Вернуться");

        return StateType.PROFILE_UPDATE;
    }

    private StateType executeUpdateCommand(BotContext context) {
        log.debug("Выполнение сценария \"Редактирование\" - (/update)");
        Profile profile = context.getUser().getProfile();
        profile.setAbout(context.getInput());
        log.debug("Усиановка описания профиля - '{}'", context.getInput());

        profileService.save(profile);
        log.debug("Сохранение профиля - '{}'", profile);

        return StateType.PROFILE;
    }

    private StateType executeExitCommand() {
        log.debug("Выполнение сценария \"Вернуться назад\" - (/exit)");
        return StateType.PROFILE;
    }

    private enum Commands {
        HELP,
        UPDATE,
        EXIT
    }
}
