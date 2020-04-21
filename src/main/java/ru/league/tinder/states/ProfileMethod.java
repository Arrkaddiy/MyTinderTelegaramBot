package ru.league.tinder.states;

import java.util.Arrays;
import java.util.List;

public enum ProfileMethod {
    LEFT("/left"),
    UPDATE("/update"),
    SING_IN("/sing_in"),
    SING_UP("/sing_up"),
    SING_OUT("/sing_out");

    private String code;

    ProfileMethod(String code) {
        this.code = code;
    }

    public static List<String> getAuthorityCode() {
        return Arrays.asList(LEFT.code, UPDATE.code, SING_OUT.code);
    }

    public static List<String> getNotAuthorityCode() {
        return Arrays.asList(LEFT.code, SING_IN.code, SING_UP.code);
    }
}
