package zeeslag.shared.net.boats;

import org.jetbrains.annotations.NotNull;
import zeeslag.shared.net.Direction;
import zeeslag.shared.net.Orientation;
import zeeslag.shared.net.ShipType;
import zeeslag.shared.net.Tile;

import java.util.ArrayList;
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
    public int getSize()
    {
        if(type == ShipType.AIRCRAFT_CARRIER)
        {
            return 5;
        }
        if(type == ShipType.BATTLESHIP)
        {
            return 4;
        }
        if(type == ShipType.CRUISER)
        {
            return 3;
        }
        if(type == ShipType.SUBMARINE)
        {
            return 2;
        }
        else
            {
                return 1;
            }
    }


}
