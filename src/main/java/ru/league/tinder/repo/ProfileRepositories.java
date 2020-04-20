package ru.league.tinder.repo;

import org.springframework.data.repository.CrudRepository;
import ru.league.tinder.entity.Profile;

import java.util.List;

public interface ProfileRepositories extends CrudRepository<Profile, Long> {

    Profile findByNameIgnoreCaseAndHashPassword(String name, String pass);

    Profile findByNameIgnoreCase(String name);

    List<Profile> findAllBySex(String sex);

}
