package zeeslag.server;

import org.jetbrains.annotations.NotNull;
import zeeslag.shared.Ship;
import zeeslag.shared.ShipType;

public class WebSocketServerEventHandler implements WebSocketServerEventListener {

    private final Zeeslag game;


    WebSocketServerEventHandler(Zeeslag game) {
        this.game = game;
    }


    @Override
    public void onSinglePlayer() {
        System.out.println("sp");
        game.setIsSinglePlayer(true);
        game.addPlayer(game.getAi().getId());
    }


    @Override
    public void onPlaceShips(int userId, @NotNull Ship[] ships) {
        if (game.getPlayerState(userId) != PlayerState.PLACING) throw new IllegalStateException();

        for (Ship ship : ships) {
            System.out.println(ship.getX());
            if (!game.getGrid(userId).tryPlace(ship)) {
                game.getGrid(userId).clear();
                throw new IllegalStateException();
            }
        }

        if (game.getGrid(userId).getShips().size() != ShipType.values().length) {
            game.getGrid(userId).clear();
            throw new IllegalStateException();
        }

        game.setPlayerReady(userId);

        if (game.getGameState() == GameState.FIGHTING)
            ZeeslagServer.getWebSocketServlet().emitStart();

        if (game.isSinglePlayer()) game.getAi().placeShips();

        ZeeslagServer.getWebSocketServlet().emitPlaceShips(userId, ships);
    }


    @Override
    public void onPlayerJoin(int userId) {
        game.addPlayer(userId);
    }


    @Override
    public void onPlayerLeave(int userId) {
        game.removePlayer(userId);
    }


    @Override
    public void onAttack(int sender, int x, int y) {
        if (game.getGameState() != GameState.FIGHTING) throw new IllegalStateException();
        if (game.getPlayerState(sender) != PlayerState.ATTACKING) throw new IllegalStateException();

        var hitType = game.attack(1 - sender, x, y);
        ZeeslagServer.getWebSocketServlet().emitAttackResult(1 - sender, x, y, hitType);

        if (game.isSinglePlayer()) game.getAi().attack();
    }


    @Override
    public void onReset() {
        game.reset();
        ZeeslagServer.getWebSocketServlet().emitReset();
    }

}
