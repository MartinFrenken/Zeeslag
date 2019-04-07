package zeeslag.client.game;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zeeslag.client.gui.SquareState;
import zeeslag.client.gui.ZeeslagGui;
import zeeslag.shared.HitType;
import zeeslag.shared.ShipType;

public class SpectatorGame implements ZeeslagGame {

    private static final Logger log = LoggerFactory.getLogger(ZeeslagGameImpl.class);
    private static final ZeeslagApi api = new ZeeslagApi("http://localhost:3000/api");
    private final ZeeslagGui gui;
    private final int userId = -1;
    @Nullable
    private ZeeslagSpectatorClient webSocketClient;


    public SpectatorGame(ZeeslagGui gui) {
        this.gui = gui;
    }


    @Override
    public void loginPlayer(String name, String password, boolean singlePlayerMode) {
        gui.setPlayerNumber(-1, name);

        webSocketClient = new ZeeslagSpectatorClient("ws://localhost:3000/ws", new ZeeslagWebSocketEventListener() {
            @Override
            public void onAttackResult(int to, int x, int y, HitType hitType) {
                if (to == 1) {
                    gui.playerFiresShot(userId, hitType);
                    gui.showSquareOpponent(userId, x, y, SquareState.getSquareState(hitType));
                } else {
                    gui.opponentFiresShot(userId, hitType);
                    gui.showSquarePlayer(userId, x, y, SquareState.getSquareState(hitType));
                }
            }


            @Override
            public void onReady() {

            }


            @Override
            public void onStart() {

            }


            @Override
            public void onReset() {
                for (var x = 0; x < 10; x++) {
                    for (var y = 0; y < 10; y++) {
                        gui.showSquareOpponent(userId, x, y, SquareState.WATER);
                        gui.showSquarePlayer(userId, x, y, SquareState.WATER);
                    }
                }
            }
        });
    }


    @Override
    public void placeShipsAutomatically() {

    }


    @Override
    public void placeShip(ShipType shipType, int bowX, int bowY, boolean horizontal) {

    }


    @Override
    public void removeShip(int posX, int posY) {

    }


    @Override
    public void removeAllShips() {

    }


    @Override
    public void notifyWhenReady() {

    }


    @Override
    public void fireShotGui(int posX, int posY) {

    }


    @Override
    public void resetGame() {

    }


    @Override
    public void stop() {

    }

}
