package ru.league.tinder.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import ru.league.tinder.states.*;

import java.util.Optional;

@Configuration
public class StateConfig {

    private static final Logger log = LoggerFactory.getLogger(StateConfig.class);

    @Autowired
    private StartState startState;
    @Autowired
    private LeftState leftState;
    @Autowired
    private ProfileState profileState;
    @Autowired
    private ProfileUpdateState profileUpdateState;
    @Autowired
    private FavoritesState favoritesState;
    @Autowired
    private LookProfileState lookProfileState;
    @Autowired
    private SingInState singInState;
    @Autowired
    private SingUpState singUpState;

    public Optional<State> getState(StateType stateType) {
        log.debug("Получение состояния согласно типу - '{}'", stateType);
        switch (stateType) {
            case START: {
                return Optional.of(startState);
            }

            case LEFT: {
                return Optional.of(leftState);
            }

            case PROFILE: {
                return Optional.of(profileState);
            }

            case PROFILE_UPDATE: {
                return Optional.of(profileUpdateState);
            }

            case FAVORITES: {
                return Optional.of(favoritesState);
            }

            case LOOK_PROFILE: {
                return Optional.of(lookProfileState);
            }

            case SING_IN: {
                return Optional.of(singInState);
            }

            case SING_UP: {
                return Optional.of(singUpState);
            }

            default: {
                log.warn("Для данного типа - '{}', не найдено совподение по состоянию!", stateType);
                return Optional.empty();
            }
        }
    }
}
