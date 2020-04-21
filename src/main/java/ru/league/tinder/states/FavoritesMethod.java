package ru.league.tinder.states;

import java.util.Arrays;
import java.util.List;

public enum FavoritesMethod {
    NUMBER("/number"),
    EXIT("/exit");

    private String code;

    FavoritesMethod(String code) {
        this.code = code;
    }

    public static List<String> getMethodsCode() {
        return Arrays.asList(NUMBER.code, EXIT.code);
    }
}
