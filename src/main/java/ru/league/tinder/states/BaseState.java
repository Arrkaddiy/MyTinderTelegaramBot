package ru.league.tinder.states;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.league.tinder.bot.BotContext;

import java.util.List;

public abstract class BaseState implements State, StateSendMessage {

    public abstract String getCode();

    public abstract String getStateId();

    public abstract void enter(BotContext context);

    public abstract void execute(BotContext context);

    public abstract void next(BotContext context);

    public abstract void help(BotContext context);

    public abstract List<State> getPossibleState();

    public void sendTextMessage(BotContext context, String text) {
        SendMessage message = new SendMessage()
                .setChatId(context.getUser().getChatId())
                .setText(text);

        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
