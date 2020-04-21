package ru.league.tinder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.league.tinder.entity.User;
import ru.league.tinder.states.StateType;

@Service
public class StartService {

    private static final Logger log = LoggerFactory.getLogger(StartService.class);

    @Autowired
    private UserService userService;

    public String start(Long tinderId) {
        log.debug("Выполнение сценария перехода на состояние - '/start'");
        User user = userService.findById(tinderId);
        user.setState(StateType.START);
        userService.save(user);
        return "Мстительный авантюрист мечтает\n" +
                "отойти от дел в уютной усадьбе\n" +
                "с любимой женщиной";
    }
}
