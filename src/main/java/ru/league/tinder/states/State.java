package ru.league.tinder.states;

import ru.league.tinder.bot.BotContext;

public interface State {

    void enter(BotContext context);

    void handleInput(BotContext context);

    StateType getState();

}
