package ru.league.tinder.states;

import ru.league.tinder.bot.BotContext;

import java.util.Arrays;
import java.util.List;

public class Start extends BaseState {

    @Override
    public String getCode() {
        return "/start";
    }

    @Override
    public String getStateId() {
        return "start";
    }

    @Override
    public void enter(BotContext context) {
        sendTextMessage(context, "Мстительный авантюрист мечтает\n" +
                "отойти от дел в уютной усадьбе\n" +
                "с любимой женщиной");
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
                "/next - Следующая анкета\n" +
                "/profile - Создание и редактирование вашей анкеты\n" +
                "/favorites - Показать любимцев");
    }

    @Override
    public List<State> getPossibleState() {
        return Arrays.asList(StateType.PROFILE.getState());
    }
}
