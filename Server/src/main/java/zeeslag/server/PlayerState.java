package zeeslag.server;

import org.jetbrains.annotations.NotNull;

public enum PlayerState {

    PLACING,
    READY,
    ATTACKING,
    WAITING;

    @NotNull
    public PlayerState getNextState() {
        switch (this) {
            case PLACING:
                return READY;
            case READY:
                throw new IllegalStateException("Ready has no default next state");
            case ATTACKING:
                return WAITING;
            case WAITING:
                return ATTACKING;
            default:
                throw new IllegalStateException("Unknown player state: " + this);
        }
    }

}
