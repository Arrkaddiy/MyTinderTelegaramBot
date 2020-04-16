package ru.league.tinder.entity;

import javax.persistence.*;

@Entity
@Table(name = "MACH")
public class Mach {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Profile from;
    @OneToOne
    private Profile to;

    public Mach() {
    }

    public Mach(Profile from, Profile to) {
        this.from = from;
        this.to = to;
    }

    public Profile getFrom() {
        return from;
    }

    public void setFrom(Profile from) {
        this.from = from;
    }

    public Profile getTo() {
        return to;
    }

    public void setTo(Profile to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return "Mach{" +
                " from = " + from.getName() +
                ", to = " + to.getName() +
                '}';
    }
}
