package java.zeeslag.client.game;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import zeeslag.client.game.ZeeslagGameImpl;
import zeeslag.client.gui.ZeeslagGui;
import zeeslag.shared.Ship;
import zeeslag.shared.ShipType;

import java.util.Objects;
import java.util.Set;
import java.zeeslag.client.game.MockGui;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZeeslagGameImplTest
{

    @NotNull
    private final ZeeslagGui gui = new MockGui();

    @Test
    void PlaceShipIsOkay()
    {
        ZeeslagGameImpl game = new ZeeslagGameImpl(gui);

        //Ship Input properties
        int x = 1;
        int y =1;
        ShipType shipType= ShipType.SUBMARINE;
        //
        game.placeShip(shipType, x, y, true);

        ShipType actualShipType = Objects.requireNonNull(ZeeslagGameImpl.getGrid().getTile(x, y).getShip()).getType();
        assertEquals(ShipType.SUBMARINE,actualShipType);


    }
    @Test
    void PlaceShipsAutomaticallyIsOkay()
    {
        ZeeslagGameImpl game = new ZeeslagGameImpl(gui);
        game.placeShipsAutomatically();
        Set<Ship> ships = ZeeslagGameImpl.getGrid().getShips();
       
       int expectedAmountOfShips = ShipType.values().length;
       int actualAmountOfShips = ships.size();
       assertEquals(expectedAmountOfShips,actualAmountOfShips);
    }

}