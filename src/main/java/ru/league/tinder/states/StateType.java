package ru.league.tinder.states;

public enum StateType {
    START(new Start()),
    SING_UP(new SingUp()),
    PROFILE(new Profile());

    private State state;

    StateType(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public static StateType getStateTypeByStateId(String stateId) {
        for (StateType value : StateType.values()) {
            if (value.getState().getStateId().equalsIgnoreCase(stateId)) return value;
        }

        return null;
    }

    public static State getStateByCode(String code) {
        for (StateType value : StateType.values()) {
            if (value.getState().getCode().equalsIgnoreCase(code)) return value.getState();
        }

        return null;
    }
}
