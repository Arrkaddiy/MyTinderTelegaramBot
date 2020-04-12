package ru.league.tinder.states;

import ru.league.tinder.bot.BotContext;

import java.util.List;

public class SingUp extends BaseState {
    @Override
    public String getCode() {
        return "/sing_up";
    }

    @Override
    public String getStateId() {
        return "singUp";
    }

    @Override
    public void enter(BotContext context) {
        sendTextMessage(context, "Вы сударь иль сударыня? Как вас величать? Ваш секретный шифръ?");
    }

    @Override
    public void execute(BotContext context) {

    }

    @Override
    public void next(BotContext context) {

    }

    @Override
    public void help(BotContext context) {
        sendTextMessage(context, "Коль сударь иль сударыня заплутали:\n" +
                "/sing_in - Войти\n" +
                "/sing_up - Новая");
    }

    @Override
    public List<State> getPossibleState() {
        return null;
    }
}
