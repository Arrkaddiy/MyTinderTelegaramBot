package ru.league.tinder.service;

import org.springframework.stereotype.Service;
import ru.league.tinder.entity.Mach;
import ru.league.tinder.repo.MachRepositories;

import java.util.List;

@Service
public class MachService {

    private MachRepositories machRepositories;

    public MachService(MachRepositories machRepositories) {
        this.machRepositories = machRepositories;
    }

    public List<Mach> findAllMach(Long id) {
        return machRepositories.findAllByProfileFrom(id);
    }
}
