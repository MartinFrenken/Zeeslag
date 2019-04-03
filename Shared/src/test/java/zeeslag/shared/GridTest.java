package zeeslag.shared;

import org.junit.jupiter.api.Test;
import zeeslag.shared.errorMessages.ShipOutOfBoundsError;

import static org.junit.jupiter.api.Assertions.*;

class GridTest {

    @Test
    public void tryPlaceIsOkay() {
        Grid grid = new Grid();

        //Ship properties
        int x = 1;
        int y = 1;
        Orientation orientation = Orientation.HORIZONTAL;
        ShipType shipType = ShipType.SUBMARINE;
        //

        Ship shipToPlace = new Ship(x, y, orientation, shipType);
        Boolean firstPlaceSucceeded = grid.tryPlace(shipToPlace);
        Boolean secondPlaceSucceeded = grid.tryPlace(shipToPlace);
        assertTrue(firstPlaceSucceeded);
        assertFalse(secondPlaceSucceeded);
    }


    @Test
    public void tryPlaceOutOfBoundsIsOkay() {
        Grid grid = new Grid();
        //Ship properties
        int x = -1;
        int y = -1;
        Orientation orientation = Orientation.HORIZONTAL;
        ShipType shipType = ShipType.SUBMARINE;
        //

        Ship shipToPlace = new Ship(x, y, orientation, shipType);
        Boolean firstPlaceSucceeded = grid.tryPlace(shipToPlace);
        Object expectedErrorMessage = new ShipOutOfBoundsError().getClass();
        Object actualErrorMessage = grid.getErrorMessage().getClass();
        assertFalse(firstPlaceSucceeded);
        assertEquals(expectedErrorMessage, actualErrorMessage);
    }


    @Test
    public void removeShipIsOkay() {
        Grid grid = new Grid();
        Ship shipToRemove = new Ship(1, 1, Orientation.HORIZONTAL, ShipType.AIRCRAFT_CARRIER);
        grid.tryPlace(shipToRemove);
        grid.removeShip(shipToRemove);
        int expectedAmountOfShips = 0;
        int actualAmountOfShips = grid.getShips().size();
        assertEquals(expectedAmountOfShips, actualAmountOfShips);
    }

}