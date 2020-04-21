package ru.league.tinder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.league.tinder.entity.Mach;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.entity.User;
import ru.league.tinder.model.ProfileModel;
import ru.league.tinder.service.MachService;
import ru.league.tinder.service.ProfileService;
import ru.league.tinder.service.UserService;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("tinder/user/{tinderId}/profiles")
public class ProfilesController {

    private static final Logger log = LoggerFactory.getLogger(ProfileController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private MachService machService;

    @GetMapping("/left")
    public ProfileModel leftMethod(@PathVariable Long tinderId) {
        log.debug("Поиск пользователя по Id - '{}'", tinderId);
        User user = userService.findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);

        if (!user.getState().getAuthorityCode().contains("/left") ||
                !user.getState().getNotAuthorityCode().contains("/left")) {
            log.warn("Переход с данного состояния - '{}', не возможен!", user.getState());
            return null;
        }

        Profile profile = getNextProfile(user);
        log.debug("Получен следующий профиль - '{}'", profile);

        String text;

        if (profile != null) {
            user.setLastLookProfile(profile);
            userService.save(user);

            String profileAbout = profile.getAbout();
            text = profile.getName() + ":\n" + (profileAbout == null ? "" : profileAbout);
            log.debug("Получено сообщение - '{}'", text);
            return new ProfileModel().setProfile(profile);
        } else {
            return new ProfileModel(0L, "Система", "Подходящих анкет больше нет!");
        }
    }

    @PostMapping("/right")
    public String rightMethod(@PathVariable Long tinderId) {
        log.debug("Поиск пользователя по Id - '{}'", tinderId);
        User user = userService.findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);

        if (!user.getState().getAuthorityCode().contains("/left") ||
                !user.getState().getNotAuthorityCode().contains("/left")) {
            log.warn("Переход с данного состояния - '{}', не возможен!", user.getState());
            return "Неудача";
        }

        log.debug("Выполнение сценария \"Подтверждения интереса\" - (/right)");
        String answer = null;

        if (user.isAuthority()) {
            if (user.getLastLookProfile() != null) {
                Mach mach = new Mach(user.getProfile(), user.getLastLookProfile());
                machService.save(mach);
                if (machService.findAllMach(user.getLastLookProfile()).stream()
                        .anyMatch(machFind -> machFind.getTo().equals(user.getProfile()))) {
                    return "Вы любимы";
                }
                answer = sendNextProfile(user);
            } else {
                log.warn("Анкета не выбрана!");
            }

        } else {
            answer = "Вы не авторизованы!";
        }

        return answer;
    }

    private String sendNextProfile(User user) {
        Profile profile = getNextProfile(user);
        log.debug("Получен следующий профиль - '{}'", profile);

        String text = "Подходяшие анкеты не найдены!";

        if (profile != null) {
            user.setLastLookProfile(profile);
            userService.save(user);

            String profileAbout = profile.getAbout();
            text = profile.getName() + ":\n" + (profileAbout == null ? "" : profileAbout);
            log.debug("Получено сообщение - '{}'", text);
        }

        return text;
    }

    private List<Profile> getProfiles(User user) {
        List<Profile> profiles = new ArrayList<>();
        if (user.isAuthority()) {
            log.debug("Получение всех профилей противоположного пола");
            String sex = user.getProfile().getSex();
            profiles.addAll(profileService.findAllBySex(sex.equals("M") ? "F" : "M"));
        } else {
            log.debug("Получение всех профилей");
            profiles.addAll(profileService.findAll());
        }

        return profiles.stream()
                .sorted(Profile::sort)
                .collect(Collectors.toList());
    }

    private Profile getNextProfile(User user) {
        List<Profile> profileList = getProfiles(user);
        log.debug("Получено количество профилей - '{}'шт.", profileList.size());

        if (user.getLastLookProfile() != null) {
            Iterator<Profile> iterator = profileList.iterator();

            while (iterator.hasNext()) {
                Profile profile = iterator.next();
                if (profile.equals(user.getLastLookProfile()) && iterator.hasNext()) {
                    return iterator.next();
                }
            }

            if (user.isAuthority()) {
                return null;
            } else {
                return profileList.get(0);
            }

        } else {
            return profileList.size() != 0 ? profileList.get(0) : null;
        }
    }
}
