package ru.league.tinder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.league.tinder.entity.User;
import ru.league.tinder.repo.UserRepositories;

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
}
