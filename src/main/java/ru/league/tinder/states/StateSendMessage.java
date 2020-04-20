package ru.league.tinder.states;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.league.tinder.bot.BotContext;

public interface StateSendMessage {

    default void sendTextMessage(BotContext context, String text) {
        SendMessage message = new SendMessage()
                .setChatId(context.getUser().getChatId())
                .setText(text);

        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    default void sendTextMessageWithKey(BotContext context, String text, ReplyKeyboardMarkup keyboardMarkup) {
        SendMessage message = new SendMessage()
                .setChatId(context.getUser().getChatId())
                .setReplyMarkup(keyboardMarkup)
                .setText(text);

        try {
            context.getBot().execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
