package ru.league.tinder.states;

import ru.league.tinder.bot.RequestContext;
import ru.league.tinder.bot.ResponseContext;

public interface State {

    String enter(RequestContext context);

    ResponseContext nextState(RequestContext context);
}
