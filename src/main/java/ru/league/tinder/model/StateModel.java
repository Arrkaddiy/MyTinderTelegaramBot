package ru.league.tinder.model;

import java.util.List;

public class StateModel {

    private List<String> possibleMethods;

    public StateModel() {
    }

    public StateModel(List<String> possibleMethods) {
        this.possibleMethods = possibleMethods;
    }

    public List<String> getPossibleMethods() {
        return possibleMethods;
    }

    public void setPossibleMethods(List<String> possibleMethods) {
        this.possibleMethods = possibleMethods;
    }
}
