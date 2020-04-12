package ru.league.tinder.states;

import ru.league.tinder.bot.BotContext;

import java.util.List;

public interface State {

    String getStateId();

    String getCode();

    void enter(BotContext botContext);

    void execute(BotContext context);

    void next(BotContext context);

    void help(BotContext context);

    List<State> getPossibleState();
}
