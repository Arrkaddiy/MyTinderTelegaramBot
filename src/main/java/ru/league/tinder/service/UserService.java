package ru.league.tinder.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.league.tinder.entity.User;
import ru.league.tinder.repo.UserRepositories;

import java.util.List;

@Service
public class UserService {

    private final UserRepositories userRepositories;

    public UserService(UserRepositories userRepositories) {
        this.userRepositories = userRepositories;
    }

    @Transactional(readOnly = true)
    public User findByChatId(long chatId) {
        return userRepositories.findByChatId(chatId);
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        return userRepositories.findAll();
    }

    @Transactional
    public void save(User user) {
        userRepositories.save(user);
    }
}
