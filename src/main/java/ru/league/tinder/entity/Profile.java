package ru.league.tinder.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "PROFILE")
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

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    private String passwordGenerator(String name, String password) {
        return String.valueOf((Objects.hash(name.toUpperCase()) * Objects.hash(382875775))
                * (Objects.hash(password) * Objects.hash(382875775)));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return id.equals(profile.id) &&
                name.equals(profile.name) &&
                hashPassword.equals(profile.hashPassword) &&
                sex.equals(profile.sex) &&
                Objects.equals(about, profile.about);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, hashPassword, sex, about);
    }

    @Override
    public String toString() {
        return "Profile {" +
                "id = '" + id + '\'' +
                ", name = '" + name + '\'' +
                ", sex = '" + sex + '\'' +
                ", about = '" + about + '\'' +
                '}';
    }

    public int sort(Profile profile) {
        return this.id.compareTo(profile.id);
    }
}
