package zeeslag.shared.net;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class Ship {

    public final int x;
    public final int y;
    public final Orientation orientation;
    public final ShipType type;
    private Set<Tile> destroyedTiles = new HashSet<>();
    private Set<Tile> occupiedTiles = new HashSet<>();


    public Ship(int x, int y, @NotNull Orientation orientation, @NotNull ShipType type) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.type = type;
    }


    public void remove() {
        for (Tile tile : occupiedTiles)
            tile.removeShip();
    }


    @NotNull
    public Set<Tile> getDestroyedTiles() {
        return destroyedTiles;
    }


    @NotNull
    public Set<Tile> getOccupiedTiles() {
        return occupiedTiles;
    }


    public int getSize() {
        return type.getSize();
    }

}
