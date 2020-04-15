package ru.league.tinder.states;

import ru.league.tinder.bot.BotContext;

public interface State {

    void of(BotContext context);

    void enter(BotContext context);
}
