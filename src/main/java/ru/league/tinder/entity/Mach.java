package ru.league.tinder.entity;

import javax.persistence.*;

@Entity
@Table(name = "MACH")
public class Mach {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Profile profileFrom;
    @OneToOne
    private Profile profileTo;

    public Mach() {
    }

    public Mach(Profile profileFrom, Profile profileTo) {
        this.profileFrom = profileFrom;
        this.profileTo = profileTo;
    }

    public Profile getProfileFrom() {
        return profileFrom;
    }

    public void setProfileFrom(Profile profileFrom) {
        this.profileFrom = profileFrom;
    }

    public Profile getProfileTo() {
        return profileTo;
    }

    public void setProfileTo(Profile profileTo) {
        this.profileTo = profileTo;
    }
}
