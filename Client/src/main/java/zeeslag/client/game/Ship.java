package zeeslag.client.game;

import org.jetbrains.annotations.NotNull;
import zeeslag.client.gui.ShipType;

public class Ship {

    public final int x;
    public final int y;
    public final Orientation orientation;
    public final ShipType type;


    public Ship(int x, int y, @NotNull Orientation orientation, @NotNull ShipType type) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
        this.type = type;
    }

}
