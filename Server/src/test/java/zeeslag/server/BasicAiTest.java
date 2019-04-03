package zeeslag.server;

import org.junit.jupiter.api.Test;
import zeeslag.shared.Grid;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BasicAiTest
{
    @Test
    public void placeShipsAiIsOkay()
    {
        BasicAi ai = new BasicAi();
        Zeeslag game = new Zeeslag(ai);

        ai.setGame(game);
        ai.placeShips();
       Grid aiGrid = game.getGrid(ai.getId());
       int actualAmountOfShips = aiGrid.getShips().size();
       int expectedAmountOfShips =5;
       assertEquals(expectedAmountOfShips,actualAmountOfShips);

    }

}