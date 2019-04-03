package zeeslag.shared;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Tile {

    private final Position position;
    private final Grid grid;
    @Nullable
    private Ship ship;
    private boolean hasBeenHit;


    public Tile(Position position, Grid grid) {
        this.position = position;
        this.grid = grid;
    }


    @Nullable
    public Ship getShip() {
        return ship;
    }


    public void setShip(@NotNull Ship ship) {
        this.ship = ship;
    }


    public boolean isOccupied() {
        return ship != null;
    }


    public void removeShip() {
        ship = null;
    }


    public boolean hasShip() {
        return ship != null;
    }


    public boolean hasBeenHit() {
        return hasBeenHit;
    }


    public void setHasBeenHit(boolean hasBeenHit) {
        this.hasBeenHit = hasBeenHit;
    }


    public Position getPosition() {
        return position;
    }


    public Grid getGrid() {
        return grid;
    }

}
