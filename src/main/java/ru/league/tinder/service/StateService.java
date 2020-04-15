package ru.league.tinder.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.league.tinder.states.*;

import java.util.Optional;

@Service
public class StateService {

    private static final Logger log = LoggerFactory.getLogger(StateService.class);

    private StartState startState;
    private ProfileState profileState;
    private FavoritesState favoritesState;
    private LookProfileState lookProfileState;
    private SingInState singInState;
    private SingUpState singUpState;

    public StateService(StartState startState,
                        ProfileState profileState,
                        FavoritesState favoritesState,
                        LookProfileState lookProfileState,
                        SingInState singInState,
                        SingUpState singUpState) {
        this.startState = startState;
        this.profileState = profileState;
        this.favoritesState = favoritesState;
        this.lookProfileState = lookProfileState;
        this.singInState = singInState;
        this.singUpState = singUpState;
    }

    public Optional<State> getState(StateType stateType) {
        log.debug("Получение состояния - '{}'", stateType);
        switch (stateType) {
            case START: {
                return Optional.of(startState);
            }

            case PROFILE: {
                return Optional.of(profileState);
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
                return Optional.empty();
            }
        }
    }
}
