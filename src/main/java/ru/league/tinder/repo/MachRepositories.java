package ru.league.tinder.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.league.tinder.entity.Mach;

import java.util.List;

public interface MachRepositories extends CrudRepository<Mach, Long> {

    @Query("")
    List<Mach> findAllByProfileFrom(Long id);
}
