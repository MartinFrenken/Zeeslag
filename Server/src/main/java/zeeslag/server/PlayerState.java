package zeeslag.server;

import org.jetbrains.annotations.NotNull;

public enum PlayerState {

    PLACING,
    ATTACKING,
    WAITING;

    @NotNull
    public PlayerState getNextState() {
        switch (this) {
            case PLACING:
                return ATTACKING;
            case ATTACKING:
                return WAITING;
            case WAITING:
                return ATTACKING;
            default:
                throw new IllegalStateException("Unknown player state: " + this);
        }
    }

}
