package ru.league.tinder.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Profile")
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String hashPassword;

    private String sex;
    private String about;

    public Profile() {
    }

    public Profile(String sex, String name, String password) {
        this.sex = sex;
        this.name = name;
        this.hashPassword = passwordGenerator(name, password);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    public String getSex() {
        return sex;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public static String passwordGenerator(String name, String password) {
        return String.valueOf((Objects.hash(name) * Objects.hash(382875775))
                * (Objects.hash(password) * Objects.hash(382875775)));
    }

    @Override
    public String toString() {
        return "Profile {" +
                "id = '" + id + '\'' +
                ", name = '" + name + '\'' +
                ", hashPassword = '" + hashPassword + '\'' +
                ", sex = '" + sex + '\'' +
                ", about = '" + about + '\'' +
                '}';
    }
}
