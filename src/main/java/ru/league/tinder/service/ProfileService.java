package ru.league.tinder.service;

import org.springframework.stereotype.Service;
import ru.league.tinder.repo.ProfileRepositories;

@Service
public class ProfileService {

    private ProfileRepositories profileRepositories;

    public ProfileService(ProfileRepositories profileRepositories) {
        this.profileRepositories = profileRepositories;
    }


}
