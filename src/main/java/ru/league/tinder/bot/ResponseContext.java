package ru.league.tinder.bot;

import ru.league.tinder.states.StateType;

public class ResponseContext {

    private StateType stateType;
    private String output;

    public ResponseContext(StateType stateType, String output) {
        this.stateType = stateType;
        this.output = output;
    }

    public StateType getStateType() {
        return stateType;
    }

    public void setStateType(StateType stateType) {
        this.stateType = stateType;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    @Override
    public String toString() {
        return "ResponseContext{" +
                "stateType=" + stateType +
                ", output='" + output + '\'' +
                '}';
    }
}
