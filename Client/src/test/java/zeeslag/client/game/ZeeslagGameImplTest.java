package zeeslag.client.game;

import org.junit.jupiter.api.Test;
import zeeslag.client.gui.ZeeslagGui;
import zeeslag.shared.Ship;
import zeeslag.shared.ShipType;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

        ShipType actualShipType = ZeeslagGameImpl.getGrid().getTile(x, y).getShip().getType();
        assertEquals(ShipType.SUBMARINE,actualShipType);


    }
    @Test
    public void PlaceShipsAutomaticallyIsOkay()
    {
        ZeeslagGameImpl game = new ZeeslagGameImpl(gui);
        game.placeShipsAutomatically();
        Set<Ship> ships = ZeeslagGameImpl.getGrid().getShips();
       
       int expectedAmountOfShips = ShipType.values().length;
       int actualAmountOfShips = ships.size();
       assertEquals(expectedAmountOfShips,actualAmountOfShips);
    }

}