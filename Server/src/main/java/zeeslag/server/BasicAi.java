package zeeslag.server;

import org.jetbrains.annotations.NotNull;
import zeeslag.shared.Orientation;
import zeeslag.shared.Ship;
import zeeslag.shared.ShipType;

import java.util.ArrayList;
import java.util.Random;

public class BasicAi implements Ai {

    private static final int USER_ID = 1;
    private Zeeslag game;
    @NotNull
    private final Random random = new Random();

    @Override
    public void setGame(@NotNull Zeeslag game) {
        this.game = game;
    }


    @Override
    public void attack() {
        var x = (int) Math.floor(Math.random() * game.getGrid(0).getWidth());
        var y = (int) Math.floor(Math.random() * game.getGrid(0).getHeight());
        var hitResult = game.attack(1 - USER_ID, x, y);
        game.setPlayerState(USER_ID - 1, PlayerState.ATTACKING);
        ZeeslagServer.getWebSocketServlet().emitAttackResult(1 - game.getAi().getId(), x, y, hitResult);
    }


    @Override
    public int getId() {
        return USER_ID;
    }


    @Override
    public void placeShips() {
        var ships = new ArrayList<Ship>();
        
        for (ShipType shipType : ShipType.values()) {
            var isHorizontal = false;
            var x = 0;
            var y = 0;
            Ship ship = null;

            while (ship == null || !game.getGrid(USER_ID).tryPlace(ship)) {
                isHorizontal = random.nextBoolean();
                x = random.nextInt(game.getGrid(USER_ID).getWidth() - (isHorizontal ? shipType.getSize() : 0));
                y = random.nextInt(game.getGrid(USER_ID).getHeight() - (isHorizontal ? 0 : shipType.getSize()));
                ship = new Ship(x, y, isHorizontal ? Orientation.HORIZONTAL : Orientation.VERTICAL, shipType);
            }

            ships.add(ship);
        }

        game.setPlayerReady(USER_ID);
        game.setGameState(GameState.FIGHTING);

        ZeeslagServer.getWebSocketServlet().emitPlaceShips(USER_ID, ships.toArray(new Ship[]{}));
        ZeeslagServer.getWebSocketServlet().emitStart();
    }

}
