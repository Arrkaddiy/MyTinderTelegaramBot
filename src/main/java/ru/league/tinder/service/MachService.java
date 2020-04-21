package ru.league.tinder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.league.tinder.entity.Mach;
import ru.league.tinder.entity.Profile;
import ru.league.tinder.repo.MachRepositories;

import java.util.List;

@Service
public class MachService {

    private static final Logger log = LoggerFactory.getLogger(MachService.class);

    private MachRepositories machRepositories;

    public MachService(MachRepositories machRepositories) {
        this.machRepositories = machRepositories;
    }

    public List<Mach> findAllMach(Profile profile) {
        log.debug("Получение всех связей по профилю - '{}'", profile);
        return machRepositories.findAllByFrom(profile);
    }

    public List<Mach> findAllMachTo(Profile profile) {
        log.debug("Получение всех связей по профилю - '{}'", profile);
        return machRepositories.findAllByTo(profile);
    }

    public void save(Mach mach) {
        log.debug("Сохранение связи - '{}'", mach);
        if (machRepositories.findByFromAndTo(mach.getFrom(), mach.getTo()) == null) {
            log.debug("Существование связи - '{}' не выявлено", mach);
            machRepositories.save(mach);
        } else {
            log.warn("Данная связь уже существует!");
        }
    }
}
