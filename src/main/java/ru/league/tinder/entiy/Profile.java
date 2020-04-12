package ru.league.tinder.entiy;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String hashPassword;

    private void passwordGenerator(String name, String password) {
        this.hashPassword = String.valueOf(Objects.hash(name) + Objects.hash(password) + Objects.hash(382875775));
    }
}
