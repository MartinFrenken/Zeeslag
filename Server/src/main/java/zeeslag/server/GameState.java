package zeeslag.server;

import org.jetbrains.annotations.NotNull;

public enum GameState {

    PREPARING,
    FIGHTING,
    ENDED;

    @NotNull
    public GameState getNextState() {
        switch (this) {
            case PREPARING:
                return FIGHTING;
            case FIGHTING:
                return ENDED;
            case ENDED:
                throw new IllegalStateException("Already in end state");
            default:
                throw new IllegalStateException("Unknown game state " + this);
        }
    }

}
