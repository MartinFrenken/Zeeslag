package zeeslag.shared.net;

import org.jetbrains.annotations.Nullable;
import zeeslag.shared.net.boats.Boat;

public class Tile {

    private final Position position;
    private final Grid grid;
    @Nullable
    private Boat boat;
    private boolean hasBeenHit;


    public Tile(Position position, Grid grid) {
        this.position = position;
        this.grid = grid;
    }

    @Nullable
    public Boat getBoat() {
        return boat;
    }

    public void setBoat(@Nullable Boat boat) {
        this.boat = boat;
    }
    
    public boolean hasBoat() {
        return boat != null;
    }

    public boolean hasBeenHit() {
        return hasBeenHit;
    }

    public void setHasBeenHit(boolean hasBeenHit) {
        this.hasBeenHit = hasBeenHit;
    }
}
