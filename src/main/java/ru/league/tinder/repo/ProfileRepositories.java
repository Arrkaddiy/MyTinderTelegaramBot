package ru.league.tinder.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.league.tinder.entiy.Profile;

public interface ProfileRepositories extends JpaRepository<Profile, Long> {

}
