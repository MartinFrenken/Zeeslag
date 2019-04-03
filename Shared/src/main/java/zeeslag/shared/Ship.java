package zeeslag.shared;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Ship {

    private final int x;
    private final int y;
    private final Orientation orientation;
    private final ShipType type;
    private transient final Set<Tile> destroyedTiles;
    private transient final Set<Tile> occupiedTiles;
    @Nullable
    private transient Grid grid;
    private transient boolean hasSunk = false;


    //Only used by json deserializer
    public Ship() {
        x = 0;
        y = 0;
        orientation = null;
        type = null;
        destroyedTiles = new HashSet<>();
        occupiedTiles = new HashSet<>();
    }


    public Ship(int x, int y, @NotNull Orientation orientation, @NotNull ShipType type) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.type = type;
        destroyedTiles = new HashSet<>();
        occupiedTiles = new HashSet<>();
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
        return getType().getSize();
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public @NotNull Orientation getOrientation() {
        if (orientation == null) throw new NullPointerException();
        return orientation;
    }


    public @NotNull ShipType getType() {
        if (type == null) throw new NullPointerException();
        return type;
    }


    public void destroyTile(Tile tile) {
        destroyedTiles.add(tile);
        if (destroyedTiles.size() == occupiedTiles.size() && occupiedTiles.size() > 0)
            hasSunk = true;
    }


    public boolean hasSunk() {
        return hasSunk;
    }

}
