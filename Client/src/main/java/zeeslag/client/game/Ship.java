package zeeslag.client.game;

import org.jetbrains.annotations.NotNull;
import zeeslag.client.gui.ShipType;

import java.util.ArrayList;

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
