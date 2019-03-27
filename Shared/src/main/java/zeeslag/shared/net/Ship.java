package zeeslag.shared.net;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Ship {

    public final int x;
    public final int y;
    public final Orientation orientation;
    public final ShipType type;
    private final Set<Tile> destroyedTiles = new HashSet<>();
    private final Set<Tile> occupiedTiles = new HashSet<>();
    @Nullable
    private Grid grid;


    public Ship(int x, int y, @NotNull Orientation orientation, @NotNull ShipType type) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.type = type;
    }


    public void remove() {
        for (Tile tile : occupiedTiles) {
            tile.removeShip();
        }

        if (grid != null)
            grid.removeShip(this);
    }


    public void setGrid(@NotNull Grid grid) {
        this.grid = grid;
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
