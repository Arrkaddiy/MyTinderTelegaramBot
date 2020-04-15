package ru.league.tinder.states;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.league.tinder.bot.BotContext;
import ru.league.tinder.service.ProfileService;

@Component
public class LeftState implements State, StateSendMessage {

    private static final Logger log = LoggerFactory.getLogger(LeftState.class);

    private StateType state;

    private ProfileService profileService;

    public LeftState(ProfileService profileService) {
        this.profileService = profileService;
    }

    @Override
    public void enter(BotContext context) {
        log.debug("Выполнение сценария перехода на состояние");
        state = StateType.LEFT;
        sendTextMessage(context, "Любимцы:");
    }

    @Override
    public void handleInput(BotContext context) {

    }

    @Override
    public StateType getState() {
        return state;
    }
}
