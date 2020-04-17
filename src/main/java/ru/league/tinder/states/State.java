package ru.league.tinder.states;

import ru.league.tinder.bot.BotContext;

public interface State {

    void enter(BotContext context);

    StateType nextState(BotContext context);
}
