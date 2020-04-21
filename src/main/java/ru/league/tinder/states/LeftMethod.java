package ru.league.tinder.states;

import java.util.Arrays;
import java.util.List;

public enum LeftMethod {

    LEFT("/left"),
    RIGHT("/right"),
    PROFILE("/profile"),
    FAVORITES("/favorites");

    private String code;

    LeftMethod(String code) {
        this.code = code;
    }

    public static List<String> getAuthorityCode() {
        return Arrays.asList(LEFT.code, RIGHT.code, PROFILE.code, FAVORITES.code);
    }

    public static List<String> getNotAuthorityCode() {
        return Arrays.asList(LEFT.code, PROFILE.code, FAVORITES.code);
    }
}
