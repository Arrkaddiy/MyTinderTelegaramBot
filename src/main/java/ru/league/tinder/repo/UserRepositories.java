package ru.league.tinder.repo;

import org.springframework.data.repository.CrudRepository;
import ru.league.tinder.entity.User;

public interface UserRepositories extends CrudRepository<User, Long> {

    User findByChatId(long chatId);

    long deleteByChatId(long chatId);
}
