package zeeslag.shared.net;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

public class Grid {

    private final int width;
    private final int height;
    private final Tile[][] tiles;
    private final Set<Ship> ships = new HashSet<>();
    private final Set<ShipType> shipTypes = new HashSet<>();


    public Grid(final int width, final int height) {
        this.width = width;
        this.height = height;

        tiles = new Tile[height][];

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; y++) {
                if (tiles[x] == null) tiles[x] = new Tile[height];
                tiles[x][y] = new Tile(new Position(x, y), this);
            }
        }
    }


    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }


    public Tile getTile(int x, int y) {
        return tiles[x][y];
    }


    public Set<Ship> getShips() {
        return ships;
    }


    @Nullable
    public Tile at(@NotNull final Position position) {
        final Position normalized = normalize(position);
        return tiles[normalized.y][normalized.x];
    }


    public int calculateDistance(@NotNull final Position source, @NotNull final Position target) {
        final Position normalizedSource = normalize(source);
        final Position normalizedTarget = normalize(target);

        final int dx = Math.abs(normalizedSource.x - normalizedTarget.x);
        final int dy = Math.abs(normalizedSource.y - normalizedTarget.y);

        final int toroidal_dx = Math.min(dx, width - dx);
        final int toroidal_dy = Math.min(dy, height - dy);

        return toroidal_dx + toroidal_dy;
    }


    @NotNull
    public Position normalize(@NotNull final Position position) {
        final int x = ((position.x % width) + width) % width;
        final int y = ((position.y % height) + height) % height;
        return new Position(x, y);
    }


    public boolean tryPlace(@NotNull Ship ship) {
        if (ship.orientation == Orientation.HORIZONTAL && ship.x + ship.getSize() > width) return false;
        if (ship.orientation == Orientation.VERTICAL && ship.y + ship.getSize() > height) return false;
        if (shipTypes.contains(ship.type)) return false;

        for (int i = 0; i < ship.getSize(); i++) {
            var x = ship.x + (ship.orientation == Orientation.HORIZONTAL ? i : 0);
            var y = ship.y + (ship.orientation == Orientation.VERTICAL ? i : 0);

            if (tiles[x][y].isOccupied()) {
                ship.remove();
                return false;
            }

            tiles[x][y].setShip(ship);
            ship.getOccupiedTiles().add(tiles[x][y]);
        }
        ship.setGrid(this);
        ships.add(ship);
        shipTypes.add(ship.type);
        return true;
    }


    public void clear() {
        ships.clear();
        shipTypes.clear();
        
        for (var x = 0; x < width; x++)
            for (var y = 0; y < height; y++)
                tiles[x][y].removeShip();
    }


    public void removeShip(@NotNull Ship ship) {
        ships.remove(ship);
        shipTypes.remove(ship.type);
    }

}