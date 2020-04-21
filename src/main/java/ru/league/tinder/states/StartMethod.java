package ru.league.tinder.states;

import java.util.Arrays;
import java.util.List;

public enum StartMethod {
    LEFT("/left"),
    PROFILE("/profile"),
    FAVORITES("/favorites");

    private String code;

    StartMethod(String code) {
        this.code = code;
    }

    public static List<String> getMethodsCode() {
        return Arrays.asList(LEFT.code, PROFILE.code, FAVORITES.code);
    }
}
