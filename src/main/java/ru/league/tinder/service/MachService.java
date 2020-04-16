package ru.league.tinder.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.league.tinder.entity.Mach;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.repo.MachRepositories;

import java.util.List;

@Service
public class MachService {

    private MachRepositories machRepositories;

    public MachService(MachRepositories machRepositories) {
        this.machRepositories = machRepositories;
    }

    @Transactional(readOnly = true)
    public List<Mach> findAllMach(Profile profile) {
        return machRepositories.findAllByFrom(profile);
    }

    @Transactional
    public void save(Mach mach) {
        machRepositories.save(mach);
    }
}
