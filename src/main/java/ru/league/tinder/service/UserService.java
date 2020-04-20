package ru.league.tinder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.league.tinder.entity.User;
import ru.league.tinder.repo.UserRepositories;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    private final UserRepositories userRepositories;

    public UserService(UserRepositories userRepositories) {
        this.userRepositories = userRepositories;
    }

    @Transactional(readOnly = true)
    public User findByChatId(long chatId) {
        log.debug("Получение пользователя по Chat Id - '{}'", chatId);
        return userRepositories.findByChatId(chatId);
    }

    @Transactional
    public void save(User user) {
        log.debug("Сохранение пользователя - '{}'", user);
        userRepositories.save(user);
    }

    @Transactional
    public void delete(Long chatId) {
        log.debug("Удаление пользователя - '{}'", chatId);
        userRepositories.deleteByChatId(chatId);
    }
}
