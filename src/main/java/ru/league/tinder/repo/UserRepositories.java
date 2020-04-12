package ru.league.tinder.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.league.tinder.entiy.User;

public interface UserRepositories extends JpaRepository<User, Long> {

    User findByChatId(long chatId);
}
