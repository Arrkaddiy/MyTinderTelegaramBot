package ru.league.tinder.states;

import ru.league.tinder.bot.BotContext;

import java.util.Arrays;
import java.util.List;

public class Profile extends BaseState {

    @Override
    public String getCode() {
        return "/profile";
    }

    @Override
    public String getStateId() {
        return "profile";
    }

    @Override
    public void enter(BotContext context) {
        sendTextMessage(context, "Создание и редактирование вашей анкеты");
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
        return Arrays.asList(StateType.SING_UP.getState());
    }
}
