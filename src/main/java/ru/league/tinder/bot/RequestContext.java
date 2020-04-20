package ru.league.tinder.bot;

import ru.league.tinder.entity.User;

public class RequestContext {

    private final User user;
    private final String input;

    public static RequestContext of(User user, String input) {
        return new RequestContext(user, input);
    }

    private RequestContext(User user, String input) {
        this.user = user;
        this.input = input;
    }

    public User getUser() {
        return user;
    }

    public String getInput() {
        return input;
    }

    @Override
    public String toString() {
        return "BotContext {" +
                "user = '" + user + '\'' +
                ", input= '" + input + '\'' +
                '}';
    }
}
