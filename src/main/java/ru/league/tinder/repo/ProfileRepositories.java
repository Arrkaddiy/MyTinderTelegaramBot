package ru.league.tinder.repo;

import org.springframework.data.repository.CrudRepository;
import ru.league.tinder.entity.Profile;

public interface ProfileRepositories extends CrudRepository<Profile, Long> {

    Profile findByNameAndHashPassword(String name, String pass);

}
