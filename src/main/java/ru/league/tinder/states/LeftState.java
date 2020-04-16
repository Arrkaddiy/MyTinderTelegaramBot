package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.league.tinder.bot.BotContext;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.service.ProfileService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Component
public class LeftState implements State, StateSendMessage {

    private static final Logger log = LoggerFactory.getLogger(LeftState.class);

    private Iterator<Profile> iterator;

    @Autowired
    private ProfileService profileService;

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        iterator = getProfiles(context).iterator();
        Profile profile = getNextProfile(context);
        String text = profile.getName() + ":\n" + profile.getAbout();
        sendTextMessage(context, text);
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
                return executeLeftCommand(context);
            }

            case RIGHT: {
                return executeRightCommand(context);
            }

            case PROFILE: {
                return executeProfileCommand();
            }

            case FAVORITES: {
                return executeFavoritesCommand();
            }

            default: {
                log.warn("Не задано исполение для команды - '{}'!", command);
                return StateType.LEFT;
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
                "/right - Подтвердить свой интерес\n" +
                "/profile - Создание и редактирование вашей анкеты\n" +
                "/favorites - Показать любимцев");

        return StateType.LEFT;
    }

    private StateType executeLeftCommand(BotContext context) {
        log.debug("Выполнение сценария \"Следующая анкета\" - (/left)");
        Profile profile = getNextProfile(context);
        String text = profile.getName() + ":\n" + profile.getAbout();
        sendTextMessage(context, text);
        return StateType.LEFT;
    }

    private StateType executeRightCommand(BotContext context) {
        log.debug("Выполнение сценария \"Подтверждения интереса\" - (/right)");
        return StateType.START;
    }

    private StateType executeProfileCommand() {
        log.debug("Выполнение сценария \"Анкета\" - (/profile)");
        return StateType.PROFILE;
    }

    private StateType executeFavoritesCommand() {
        log.debug("Выполнение сценария \"Любимцы\" - (/favorites)");
        return StateType.FAVORITES;
    }

    private List<Profile> getProfiles(BotContext context) {
        List<Profile> profiles = new ArrayList<>();
        if (context.getUser().isAuthority()) {
            String sex = context.getUser().getProfile().getSex();
            profiles.addAll(profileService.findAllBySex(sex.equals("M") ? "F" : "M"));
        } else {
            profiles.addAll(profileService.findAll());
        }

        return profiles;
    }

    private Profile getNextProfile(BotContext context) {
        while (iterator.hasNext()) {
            return iterator.next();
        }

        return (iterator = getProfiles(context).iterator()).next();
    }

    private enum Commands {
        HELP,
        LEFT,
        RIGHT,
        PROFILE,
        FAVORITES
    }
}
