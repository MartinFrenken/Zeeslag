package zeeslag.server;

import org.jetbrains.annotations.NotNull;
import zeeslag.shared.net.Ship;
import zeeslag.shared.net.ShipType;

public class WebSocketServerEventHandler implements WebSocketServerEventListener {

    private final Zeeslag game;


    public WebSocketServerEventHandler(Zeeslag game) {
        this.game = game;
    }


    @Override
    public void onPlaceShips(int userId, @NotNull Ship[] ships) {
        if (game.getPlayerState(userId) != PlayerState.PLACING) return;

        for (Ship ship : ships) {
            System.out.println(ship.getX());
            if (!game.getGrid(userId).tryPlace(ship)) {
                game.getGrid(userId).clear();
                return;
            }
        }

        if (game.getGrid(userId).getShips().size() != ShipType.values().length) {
            game.getGrid(userId).clear();
            return;
        }

        game.setPlayerReady(userId);

        if (game.gameState == GameState.FIGHTING)
            ZeeslagServer.getWebSocketServlet().emitStart();
    }


    @Override
    public void onPlayerJoin(int userId) {
        game.addPlayer(userId);
    }


    @Override
    public void onPlayerLeave(int userId) {
        game.removePlayer(userId);
    }

}
