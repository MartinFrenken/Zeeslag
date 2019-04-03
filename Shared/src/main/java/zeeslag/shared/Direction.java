package zeeslag.shared;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public enum Direction {
    NORTH,
    EAST,
    SOUTH,
    WEST;
    @NotNull
    private static final List<Direction> ALL_CARDINALS = new ArrayList<>();


    static {
        ALL_CARDINALS.add(NORTH);
        ALL_CARDINALS.add(SOUTH);
        ALL_CARDINALS.add(EAST);
        ALL_CARDINALS.add(WEST);
    }


    @NotNull
    public Direction invertDirection() {
        switch (this) {
            case NORTH:
                return SOUTH;
            case EAST:
                return WEST;
            case SOUTH:
                return NORTH;
            case WEST:
                return EAST;
            default:
                throw new IllegalStateException("Unknown direction " + this);
        }
    }
}
