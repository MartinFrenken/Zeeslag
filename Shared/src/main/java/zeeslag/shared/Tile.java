package zeeslag.shared;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Tile {

    private final Position position;
    @Nullable
    private Ship ship;


    Tile(Position position, Grid grid) {
        this.position = position;
    }


    @Nullable
    public Ship getShip() {
        return ship;
    }


    public void setShip(@NotNull Ship ship) {
        this.ship = ship;
    }


    boolean isOccupied() {
        return ship != null;
    }


    void removeShip() {
        ship = null;
    }


    public Position getPosition() {
        return position;
    }

}
