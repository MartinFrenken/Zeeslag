package zeeslag.shared.net.boats;

import org.jetbrains.annotations.NotNull;
import zeeslag.shared.net.Direction;
import zeeslag.shared.net.Tile;

import java.util.HashSet;
import java.util.Set;

public abstract class Boat {

    protected final Set<Tile> destroyedTiles = new HashSet<>();
    protected final Set<Tile> occupiedTiles = new HashSet<>();

    public abstract void place(@NotNull Tile tile, @NotNull Direction direction);

    public void remove() {
        for (Tile tile : occupiedTiles)
            tile.setBoat(null);
    }

    @NotNull
    public Set<Tile> getDestroyedTiles() {
        return destroyedTiles;
    }

    @NotNull
    public Set<Tile> getOccupiedTiles() {
        return occupiedTiles;
    }
}
