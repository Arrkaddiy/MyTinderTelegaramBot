package ru.league.tinder.entity;

import ru.league.tinder.states.StateType;

import javax.persistence.*;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long chatId;
    private StateType state;

    public User() {
    }

    public User(Long chatId, StateType state) {
        this.chatId = chatId;
        this.state = state;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getChatId() {
        return chatId;
    }

    public void setChatId(Long chatId) {
        this.chatId = chatId;
    }

    public StateType getState() {
        return state;
    }

    public void setState(StateType state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "User {" +
                "id = " + id +
                ", chatId = " + chatId + '\'' +
                ", stateType = " + state +
                '}';
    }
}
