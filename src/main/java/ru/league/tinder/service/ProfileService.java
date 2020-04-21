package ru.league.tinder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.repo.ProfileRepositories;

import java.util.List;

@Service
public class ProfileService {

    private static final Logger log = LoggerFactory.getLogger(ProfileService.class);

    private ProfileRepositories profileRepositories;

    public ProfileService(ProfileRepositories profileRepositories) {
        this.profileRepositories = profileRepositories;
    }

    public boolean isBusy(String name) {
        log.debug("Получение профиля по имени - '{}'", name);
        return profileRepositories.findByNameIgnoreCase(name) != null;
    }

    public List<Profile> findAll() {
        log.debug("Получение всех профилей");
        return (List<Profile>) profileRepositories.findAll();
    }

    public List<Profile> findAllBySex(String sex) {
        log.debug("Получение всех профилей по полу - '{}'", sex);
        return profileRepositories.findAllBySex(sex);
    }

    public Profile authority(String name, String pass) {
        log.debug("Прохождение авторизации - '{}'", name);
        return profileRepositories.findByNameIgnoreCaseAndHashPassword(name, pass);
    }

    public void save(Profile profile) {
        log.debug("Сохранение профиля - '{}'", profile);
        profileRepositories.save(profile);
    }
}
