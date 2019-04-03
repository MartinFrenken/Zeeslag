package zeeslag.shared;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import zeeslag.shared.errorMessages.ErrorMessage;
import zeeslag.shared.errorMessages.ShipAlreadyExistsError;
import zeeslag.shared.errorMessages.ShipCollisionError;
import zeeslag.shared.errorMessages.ShipOutOfBoundsError;

import java.util.HashSet;
import java.util.Set;

public class Grid {

    private final int width = 10;
    private final int height = 10;
    private final Tile[][] tiles;
    private final Set<Ship> ships = new HashSet<>();
    private final Set<ShipType> shipTypes = new HashSet<>();
    private ErrorMessage errorMessage;


    public Grid() {

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


    public @NotNull Set<Ship> getShips() {
        return ships;
    }


    @Nullable
    public Tile at(@NotNull final Position position) {
        final Position normalized = normalize(position);
        return tiles[normalized.y][normalized.x];
    }


    @NotNull
    private Position normalize(@NotNull final Position position) {
        final int x = ((position.x % width) + width) % width;
        final int y = ((position.y % height) + height) % height;
        return new Position(x, y);
    }


    public boolean tryPlace(@NotNull Ship ship) {

        if (ship.getX() < 0 || ship.getY() < 0) {
            errorMessage = new ShipOutOfBoundsError();
            return false;
        }
        if (ship.getOrientation() == Orientation.HORIZONTAL && ship.getX() + ship.getSize() > width) {
            errorMessage = new ShipOutOfBoundsError();
            return false;
        }
        if (ship.getOrientation() == Orientation.VERTICAL && ship.getY() + ship.getSize() > height) {
            errorMessage = new ShipOutOfBoundsError();
            return false;
        }
        if (shipTypes.contains(ship.getType())) {
            errorMessage = new ShipAlreadyExistsError(ship.getType());
            return false;
        }

        for (int i = 0; i < ship.getSize(); i++) {
            var x = ship.getX() + (ship.getOrientation() == Orientation.HORIZONTAL ? i : 0);
            var y = ship.getY() + (ship.getOrientation() == Orientation.VERTICAL ? i : 0);

            if (tiles[x][y].isOccupied()) {
                ship.remove();
                errorMessage = new ShipCollisionError();
                return false;
            }

            tiles[x][y].setShip(ship);
            ship.getOccupiedTiles().add(tiles[x][y]);
        }
        ship.setGrid(this);
        ships.add(ship);
        shipTypes.add(ship.getType());
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
        shipTypes.remove(ship.getType());
    }


    public ErrorMessage getErrorMessage() {
        return errorMessage;
    }

}