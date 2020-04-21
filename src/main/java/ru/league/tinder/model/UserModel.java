package ru.league.tinder.model;

public class UserModel {

    private Long tinderId;
    private String stateName;

    public UserModel() {
    }

    public UserModel(Long tinderId, String stateName) {
        this.tinderId = tinderId;
        this.stateName = stateName;
    }

    public Long getTinderId() {
        return tinderId;
    }

    public void setTinderId(Long tinderId) {
        this.tinderId = tinderId;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "tinderId=" + tinderId +
                ", stateName='" + stateName + '\'' +
                '}';
    }
}
