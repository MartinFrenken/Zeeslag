package zeeslag.client.game;

import com.sun.javafx.application.PlatformImpl;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import zeeslag.client.gui.ZeeslagClient;
import zeeslag.client.gui.ZeeslagGui;
import zeeslag.shared.net.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ZeeslagGameImplTest
{
    ZeeslagGui gui = new MockGui();

    @Test

    public void PlaceShipIsOkay()
    {
        ZeeslagGameImpl game = new ZeeslagGameImpl(gui);

        //Ship Input properties
        int x = 1;
        int y =1;
        Boolean horizontal=true;
        ShipType shipType= ShipType.SUBMARINE;
        //
        game.placeShip(shipType,x,y,horizontal);


        ShipType actualShipType =game.getGrid().getTile(x,y).getShip().getType();
        assertEquals(ShipType.SUBMARINE,actualShipType);


    }
    @Test
    public void PlaceShipsAutomaticallyIsOkay()
    {
        ZeeslagGameImpl game = new ZeeslagGameImpl(gui);
        game.placeShipsAutomatically();
       Set<Ship> ships= game.getGrid().getShips();
       int expectedAmountOfShips = ShipType.values().length;
       int actualAmountOfShips = ships.size();
       assertEquals(expectedAmountOfShips,actualAmountOfShips);
    }

}