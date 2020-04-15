package ru.league.tinder.bot;

import ru.league.tinder.entity.User;

public class BotContext {

    private final Bot bot;
    private final User user;
    private final String input;

    public static BotContext of(Bot bot, User user, String input) {
        return new BotContext(bot, user, input);
    }

    private BotContext(Bot bot, User user, String input) {
        this.bot = bot;
        this.user = user;
        this.input = input;
    }

    public Bot getBot() {
        return bot;
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
