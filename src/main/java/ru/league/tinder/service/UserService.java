package ru.league.tinder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.entity.User;
import ru.league.tinder.model.StateModel;
import ru.league.tinder.repo.UserRepositories;
import ru.league.tinder.states.StateType;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepositories userRepositories;

    public User findById(Long id) {
        log.debug("Получение пользователя по Id - '{}'", id);
        return userRepositories.findById(id).orElseThrow(IllegalArgumentException::new);
    }

    public void save(User user) {
        log.debug("Сохранение пользователя - '{}'", user);
        userRepositories.save(user);
    }

    public void delete(User user) {
        log.debug("Удаление пользователя - '{}'", user);
        userRepositories.delete(user);
    }

    public Long getUserById(Long tinderId) {
        log.debug("Поиск пользователя по Id - '{}'", tinderId);
        User user = findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);
        return user.getId();
    }

    public Profile getUserLastlook(Long tinderId) {
        log.debug("Поиск пользователя по Id - '{}'", tinderId);
        User user = findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);
        return user.getLastLookProfile();
    }

    public StateModel getPossibleMethods(Long tinderId) {
        log.debug("Передача доступных методов стадии пользователя по Id - '{}'", tinderId);
        User user = findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);

        if (user.isAuthority()) {
            return new StateModel(user.getState().getAuthorityCode());
        } else {
            return new StateModel(user.getState().getNotAuthorityCode());
        }
    }

    public Long createUser() {
        log.debug("Создание нового пользователя");
        User user = new User(StateType.START);
        save(user);
        log.debug("Получен пользователь - '{}'", user);
        return user.getId();
    }

    public void deleteUser(Long tinderId) {
        log.debug("Удаление пользователя по Id - '{}'", tinderId);
        User user = findById(tinderId);
        log.debug("Получен пользователь - '{}'", user);
        delete(user);
    }
}