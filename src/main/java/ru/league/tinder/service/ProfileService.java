package ru.league.tinder.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.repo.ProfileRepositories;

import java.util.List;

@Service
public class ProfileService {

    private ProfileRepositories profileRepositories;

    public ProfileService(ProfileRepositories profileRepositories) {
        this.profileRepositories = profileRepositories;
    }

    @Transactional(readOnly = true)
    public Profile findByName(String name) {
        return profileRepositories.findByNameIgnoreCase(name);
    }

    @Transactional(readOnly = true)
    public boolean isBusy(String name) {
        return profileRepositories.findByNameIgnoreCase(name) != null;
    }

    @Transactional(readOnly = true)
    public List<Profile> findAll() {
        return (List<Profile>) profileRepositories.findAll();
    }

    @Transactional(readOnly = true)
    public List<Profile> findAllBySex(String sex) {
        return profileRepositories.findAllBySex(sex);
    }

    @Transactional(readOnly = true)
    public Profile authority(String name, String pass) {
        return profileRepositories.findByNameAndHashPassword(name, pass);
    }

    @Transactional
    public void save(Profile profile) {
        profileRepositories.save(profile);
    }
}
