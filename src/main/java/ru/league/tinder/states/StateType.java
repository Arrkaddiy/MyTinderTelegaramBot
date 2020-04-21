package ru.league.tinder.states;

import java.util.List;

public enum StateType {
    START,
    LEFT,
    PROFILE,
    FAVORITES;

    public List<String> getNotAuthorityCode() {
        switch (this) {
            case START: {
                return StartMethod.getMethodsCode();
            }

            case LEFT: {
                return LeftMethod.getNotAuthorityCode();
            }

            case PROFILE: {
                return ProfileMethod.getNotAuthorityCode();
            }

            case FAVORITES: {
                return FavoritesMethod.getMethodsCode();
            }

            default: {
                throw new IllegalArgumentException();
            }
        }
    }

    public List<String> getAuthorityCode() {
        switch (this) {
            case START: {
                return StartMethod.getMethodsCode();
            }

            case LEFT: {
                return LeftMethod.getAuthorityCode();
            }

            case PROFILE: {
                return ProfileMethod.getAuthorityCode();
            }

            case FAVORITES: {
                return FavoritesMethod.getMethodsCode();
            }

            default: {
                throw new IllegalArgumentException();
            }
        }
    }
}
