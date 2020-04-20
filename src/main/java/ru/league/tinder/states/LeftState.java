package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import ru.league.tinder.bot.BotContext;
import ru.league.tinder.entity.Mach;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.entity.User;
import ru.league.tinder.service.MachService;
import ru.league.tinder.service.ProfileService;
import ru.league.tinder.service.UserService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class LeftState implements State, StateSendMessage {

    private static final Logger log = LoggerFactory.getLogger(LeftState.class);

    private UserService userService;
    private ProfileService profileService;
    private MachService machService;

    public LeftState(UserService userService, ProfileService profileService, MachService machService) {
        this.userService = userService;
        this.profileService = profileService;
        this.machService = machService;
    }

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        sendNextProfile(context);
    }

    @Override
    public StateType nextState(BotContext context) {
        log.debug("Обработка контекста - '{}'", context);
        Commands inputCommand = getCommand(context.getInput()).orElse(Commands.HELP);
        log.debug("Определена команда - '{}'", inputCommand);
        return execute(inputCommand, context);
    }

    private ReplyKeyboardMarkup getButton() {
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setSelective(true);
        keyboardMarkup.setResizeKeyboard(true);
        keyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardRow1 = new KeyboardRow();
        keyboardRow1.add(new KeyboardButton("/left"));

        KeyboardRow keyboardRow2 = new KeyboardRow();
        keyboardRow2.add(new KeyboardButton("/right"));

        KeyboardRow keyboardRow3 = new KeyboardRow();
        keyboardRow3.add(new KeyboardButton("/profile"));

        KeyboardRow keyboardRow4 = new KeyboardRow();
        keyboardRow4.add(new KeyboardButton("/favorites"));

        KeyboardRow keyboardRow5 = new KeyboardRow();
        keyboardRow5.add(new KeyboardButton("/help"));

        keyboardRowList.add(keyboardRow1);
        keyboardRowList.add(keyboardRow2);
        keyboardRowList.add(keyboardRow3);
        keyboardRowList.add(keyboardRow4);
        keyboardRowList.add(keyboardRow5);

        keyboardMarkup.setKeyboard(keyboardRowList);
        return keyboardMarkup;
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
        sendNextProfile(context);
        return StateType.LEFT;
    }

    private StateType executeRightCommand(BotContext context) {
        log.debug("Выполнение сценария \"Подтверждения интереса\" - (/right)");

        if (context.getUser().isAuthority()) {
            if (context.getUser().getLastLookProfile() != null) {
                Mach mach = new Mach(context.getUser().getProfile(), context.getUser().getLastLookProfile());
                machService.save(mach);
                if (machService.findAllMach(context.getUser().getLastLookProfile()).stream()
                        .anyMatch(machFind -> machFind.getTo().equals(context.getUser().getProfile()))) {
                    sendTextMessage(context, "Вы любимы");
                }
                sendNextProfile(context);
            } else {
                log.warn("Анкета не выбрана!");
            }

        } else {
            sendTextMessage(context, "Вы не авторизованы!");
        }

        return StateType.LEFT;
    }

    private StateType executeProfileCommand() {
        log.debug("Выполнение сценария \"Анкета\" - (/profile)");
        return StateType.PROFILE;
    }

    private StateType executeFavoritesCommand() {
        log.debug("Выполнение сценария \"Любимцы\" - (/favorites)");
        return StateType.FAVORITES;
    }

    private void sendNextProfile(BotContext context) {
        Profile profile = getNextProfile(context);
        log.debug("Получен следующий профиль - '{}'", profile);

        String text = "Подходяшие анкеты не найдены!";

        if (profile != null) {
            User user = context.getUser();
            user.setLastLookProfile(profile);
            userService.save(user);

            String profileAbout = profile.getAbout();
            text = profile.getName() + ":\n" + (profileAbout == null ? "" : profileAbout);
            log.debug("Получено сообщение - '{}'", text);
        }

        sendTextMessageWithKey(context, text, getButton());
    }

    private List<Profile> getProfiles(BotContext context) {
        List<Profile> profiles = new ArrayList<>();
        if (context.getUser().isAuthority()) {
            log.debug("Получение всех профилей противоположного пола");
            String sex = context.getUser().getProfile().getSex();
            profiles.addAll(profileService.findAllBySex(sex.equals("M") ? "F" : "M"));
        } else {
            log.debug("Получение всех профилей");
            profiles.addAll(profileService.findAll());
        }

        return profiles.stream()
                .sorted(Profile::sort)
                .collect(Collectors.toList());
    }

    private Profile getNextProfile(BotContext context) {
        List<Profile> profileList = getProfiles(context);
        log.debug("Получено количество профилей - '{}'шт.", profileList.size());

        if (context.getUser().getLastLookProfile() != null) {
            Iterator<Profile> iterator = profileList.iterator();

            while (iterator.hasNext()) {
                Profile profile = iterator.next();
                if (profile.equals(context.getUser().getLastLookProfile()) && iterator.hasNext()) {
                    return iterator.next();
                }
            }

            if (context.getUser().isAuthority()) {
                return null;
            } else {
                return profileList.get(0);
            }

        } else {
            return profileList.size() != 0 ? profileList.get(0) : null;
        }
    }

    private enum Commands {
        START,
        HELP,
        LEFT,
        RIGHT,
        PROFILE,
        FAVORITES
    }
}
