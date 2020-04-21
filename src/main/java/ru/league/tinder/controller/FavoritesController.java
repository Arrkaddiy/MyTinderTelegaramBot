package ru.league.tinder.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.league.tinder.entity.Mach;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.entity.User;
import ru.league.tinder.service.MachService;
import ru.league.tinder.service.UserService;
import ru.league.tinder.states.StateType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("tinder/user/{tinderId}/favorites")
public class FavoritesController {

    private static final Logger log = LoggerFactory.getLogger(FavoritesController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private MachService machService;

    @GetMapping
    public String favoritesMethod(@PathVariable Long tinderId) {
        log.debug("Поиск пользователя по Id - '{}'", tinderId);
        User user = userService.findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);

        if (!user.getState().getAuthorityCode().contains("/favorites")) {
            log.warn("Переход с данного состояния - '{}', не возможен!", user.getState());
            return "Неудача";
        }

        log.debug("Выполнение сценария перехода на состояние");
        String favors = "";
        if (user.isAuthority()) {
            List<Mach> machList = getMach(user);
            if (machList.size() > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < machList.size(); i++) {
                    stringBuilder.append(i + 1).append(". ").append(machList.get(i).getTo().getName()).append("\n");
                }
                favors = stringBuilder.toString();
            }
        }
        user.setState(StateType.FAVORITES);
        userService.save(user);
        return "Любимцы:\n" + favors;
    }

    @GetMapping("{number}")
    public String favoritesNumberMethod(@PathVariable Long tinderId, @PathVariable String number) {
        log.debug("Поиск пользователя по Id - '{}'", tinderId);
        User user = userService.findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);

        if (!user.getState().getAuthorityCode().contains("/number")) {
            log.warn("Переход с данного состояния - '{}', не возможен!", user.getState());
            return "Неудача";
        }

        log.debug("Выполнение сценария \"Просмотр анкеты\" - (/number)");
        int row = Integer.parseInt(number) - 1;
        List<Mach> machList = getMach(user);

        if (machList.size() > row) {
            Profile profile = machList.get(row).getTo();
            log.debug("Получен интерисующий профиль - '{}'", profile);
            return profile.getAbout();

        } else {
            log.warn("Выбрана не существующая запись!");
            return "Неудача";
        }
    }

    @GetMapping("exit")
    public String exitMethod(@PathVariable Long tinderId) {
        log.debug("Поиск пользователя по Id - '{}'", tinderId);
        User user = userService.findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);

        if (!user.getState().getAuthorityCode().contains("/exit")) {
            log.warn("Переход с данного состояния - '{}', не возможен!", user.getState());
            return "Неудача";
        }

        user.setState(StateType.LEFT);
        userService.save(user);
        return "Успехъ";
    }

    private List<Mach> getMach(User user) {
        List<Mach> machFromList = machService.findAllMach(user.getProfile());
        List<Mach> machToList = machService.findAllMachTo(user.getProfile());
        List<Mach> machList = new ArrayList<>();

        for (Mach mach : machFromList) {
            if (machToList.stream().anyMatch(machTo -> mach.getFrom().equals(machTo.getTo())
                    && mach.getTo().equals(machTo.getFrom()))) {
                machList.add(mach);
            }
        }


        return machList.stream()
                .sorted(Comparator.comparing(mach -> mach.getTo().getName()))
                .collect(Collectors.toList());
    }
}
