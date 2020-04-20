package ru.league.tinder.repo;

import org.springframework.data.repository.CrudRepository;
import ru.league.tinder.entity.Mach;
import ru.league.tinder.entity.Profile;

import java.util.List;

public interface MachRepositories extends CrudRepository<Mach, Long> {

    List<Mach> findAllByFrom(Profile profile);

    List<Mach> findAllByTo(Profile profile);

    Mach findByFromAndTo(Profile from, Profile to);
}
