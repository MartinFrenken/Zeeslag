/*
 * Sea Battle Start project.
 */
package zeeslag.client.game;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zeeslag.client.gui.SquareState;
import zeeslag.client.gui.ZeeslagGui;
import zeeslag.shared.net.*;

/**
 * The Sea Battle game. To be implemented.
 *
 * @author Nico Kuijpers
 */
public class ZeeslagGameImpl implements ZeeslagGame {

    private static final Logger log = LoggerFactory.getLogger(ZeeslagGameImpl.class);
    private static final ZeeslagApi api = new ZeeslagApi("http://localhost:3000/api");
    private static final Grid grid = new Grid(10, 10);
    private final ZeeslagGui gui;
    private int userId;
    @Nullable
    private ZeeslagWebSocketClient webSocketClient;


    public ZeeslagGameImpl(ZeeslagGui gui) {
        this.gui = gui;
    }


    @Override
    public void loginPlayer(String name, String password, boolean singlePlayerMode) {
        log.debug("Register Player {} - password {}", name, password);
        var authData = api.login(name, password);
        if (authData == null) {//TODO show error message
            return;
        }

        userId = authData.id;

        gui.setPlayerNumber(userId, name);
        webSocketClient = new ZeeslagWebSocketClient("ws://localhost:3000/ws", authData.token, userId, new ZeeslagWebSocketEventHandler(this));
        webSocketClient.emitAttack(3, 5);
    }


    @Override
    public void placeShipsAutomatically(int playerNr) {
        throw new UnsupportedOperationException("Method placeShipsAutomatically() not implemented.");
    }


    @Override
    public void placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {
        var ship = new Ship(bowX, bowY, horizontal ? Orientation.HORIZONTAL : Orientation.VERTICAL, shipType);
        if (!grid.tryPlace(ship))
            return;

        for (Tile tile : ship.getOccupiedTiles())
            gui.showSquarePlayer(playerNr, tile.getPosition().x, tile.getPosition().y, SquareState.SHIP);
    }


    @Override
    public void removeShip(int playerNr, int posX, int posY) {
        throw new UnsupportedOperationException("Method removeShip() not implemented.");
    }


    @Override
    public void removeAllShips(int playerNr) {
        throw new UnsupportedOperationException("Method removeAllShips() not implemented.");
    }


    @Override
    public void notifyWhenReady(int playerNr) {
        throw new UnsupportedOperationException("Method notifyWhenReady() not implemented.");
    }


    @Override
    public void fireShot(int playerNr, int posX, int posY) {
        throw new UnsupportedOperationException("Method fireShot() not implemented.");
    }


    @Override
    public void resetGame(int playerNr) {
        throw new UnsupportedOperationException("Method resetGame() not implemented.");
    }


    @Override
    public void stop() {
        if (webSocketClient != null) webSocketClient.disconnect();
    }


    public void startGame() {
    }


    public void endGame() {
    }

}
