package ru.league.tinder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.entity.User;
import ru.league.tinder.service.ProfileService;
import ru.league.tinder.service.UserService;
import ru.league.tinder.states.StateType;

@RestController
@RequestMapping("tinder/user/{tinderId}/my_profile")
public class ProfileController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @GetMapping
    public String myProfileMethod(@PathVariable Long tinderId) {
        log.debug("Поиск пользователя по Id - '{}'", tinderId);
        User user = userService.findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);

        if (!user.getState().getAuthorityCode().contains("/profile") ||
                !user.getState().getNotAuthorityCode().contains("/profile")) {
            log.warn("Переход с данного состояния - '{}', не возможен!", user.getState());
            return "Неудача";
        }

        log.debug("Выполнение сценария перехода на состояние");
        user.setState(StateType.PROFILE);
        userService.save(user);
        return "Создание и редактирование вашей анкеты";
    }

    @PostMapping("/sing_in")
    public String singInMethod(@PathVariable Long tinderId, @RequestParam String name, @RequestParam String pass) {
        log.debug("Поиск пользователя по Id - '{}'", tinderId);
        User user = userService.findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);

        if (!user.getState().getNotAuthorityCode().contains("/sing_in")) {
            log.warn("Переход с данного состояния - '{}', не возможен!", user.getState());
            return "Неудача";
        }

        Profile profileBuf = new Profile("E", name, pass);
        Profile profile = profileService.authority(profileBuf.getName(), profileBuf.getHashPassword());
        log.debug("Авторизация профиля - '{}'", profile);

        if (profile != null) {
            log.debug("Авторизация прошла успешно по name - '{}'", name);
            user.setProfile(profile);
            user.setLastLookProfile(null);
            user.setState(StateType.LEFT);
            userService.save(user);
            log.debug("Сохранение профиля пользователя - '{}'", user);
            return "Успехъ";

        } else {
            log.debug("Авторизация не прошла по name - '{}'", name);
            return "Неудача";
        }
    }

    @PostMapping("/sing_up")
    public String singUpMethod(@PathVariable Long tinderId,
                               @RequestParam String name,
                               @RequestParam String pass,
                               @RequestParam String sex) {
        log.debug("Поиск пользователя по Id - '{}'", tinderId);
        User user = userService.findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);

        if (!user.getState().getNotAuthorityCode().contains("/sing_up")) {
            log.warn("Переход с данного состояния - '{}', не возможен!", user.getState());
            return "Неудача";
        }

        if (sex.equalsIgnoreCase("сударь") || sex.equalsIgnoreCase("сударыня")) {
            Profile profile = new Profile(sex.equalsIgnoreCase("сударь") ? "M" : "F", name, pass);
            log.debug("Создан новый профиль - '{}'", profile);
            log.debug("Попытка зарегестрировать - '{}'", profile);

            if (!profileService.isBusy(profile.getName())) {
                profileService.save(profile);
                log.debug("Сохранен новый профиль - '{}'", profile);

                user.setProfile(profile);
                user.setLastLookProfile(null);
                user.setState(StateType.LEFT);
                userService.save(user);
                log.debug("Сохранение профиля под пользователем - '{}'", user);
                return "Успехъ";

            } else {
                log.warn("Данное имя уже занято! - '{}'", profile.getName());
                return "Неудача. Данное имя уже занято!";
            }

        } else {
            log.warn("Некорректный ввод");
            return "Неудача:\n" + "сударь МечтательныйАнархистъ д0л0йцарR";
        }
    }

    @GetMapping("/sing_out")
    public String singOutMethod(@PathVariable Long tinderId) {
        log.debug("Поиск пользователя по Id - '{}'", tinderId);
        User user = userService.findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);

        if (!user.getState().getAuthorityCode().contains("/sing_out")) {
            log.warn("Переход с данного состояния - '{}', не возможен!", user.getState());
            return "Неудача";
        }

        log.debug("Выполнение сценария \"Выйти\" - (/sing_out)");
        if (user.isAuthority()) {
            user.setProfile(null);
            user.setLastLookProfile(null);
            user.setState(StateType.LEFT);
            userService.save(user);
            log.debug("Сохранение пользователя - '{}'", user);
        }
        return "Выход выполнен";
    }

    @PostMapping("/update")
    public String updateMethod(@PathVariable Long tinderId, @RequestParam String about) {
        log.debug("Поиск пользователя по Id - '{}'", tinderId);
        User user = userService.findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);

        if (!user.getState().getAuthorityCode().contains("/update")) {
            log.warn("Переход с данного состояния - '{}', не возможен!", user.getState());
            return "Неудача";
        }

        log.debug("Выполнение сценария \"Редактирование\" - (/update)");
        Profile profile = user.getProfile();
        profile.setAbout(about);
        profileService.save(profile);
        log.debug("Сохранение профиля - '{}'", profile);

        return "Успехъ";
    }
}