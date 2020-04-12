package ru.league.tinder.states;

import ru.league.tinder.bot.BotContext;

public interface StateSendMessage {
    void sendTextMessage(BotContext context, String text);
}
