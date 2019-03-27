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

import java.util.Random;

/**
 * The Sea Battle game. To be implemented.
 *
 * @author Nico Kuijpers
 */
public class ZeeslagGameImpl implements ZeeslagGame {

    private static final Logger log = LoggerFactory.getLogger(ZeeslagGameImpl.class);
    private static final ZeeslagApi api = new ZeeslagApi("http://localhost:3000/api");
    private static final Grid grid = new Grid();
    private final ZeeslagGui gui;
    private int userId;
    private String errorMessage;
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
    }


    @Override
    public void placeShipsAutomatically(int playerNr) {
        clearGridAndGui();
        var random = new Random();
        for (ShipType shipType : ShipType.values()) {
            var isHorizontal = false;
            var x = 0;
            var y = 0;
            Ship ship = null;

            while (ship == null || !tryPlaceShipAndAddToGui(ship)) {
                isHorizontal = random.nextBoolean();
                x = random.nextInt(grid.getWidth() - (isHorizontal ? shipType.getSize() : 0));
                y = random.nextInt(grid.getHeight() - (isHorizontal ? 0 : shipType.getSize()));
                ship = new Ship(x, y, isHorizontal ? Orientation.HORIZONTAL : Orientation.VERTICAL, shipType);
            }
        }
    }


    private void clearGridAndGui() {
        grid.clear();

        for (var x = 0; x < grid.getWidth(); x++)
            for (var y = 0; y < grid.getHeight(); y++)
                gui.showSquarePlayer(userId, x, y, SquareState.WATER);
    }


    private boolean tryPlaceShipAndAddToGui(Ship ship) {
       try
       {
           grid.tryPlace(ship);
       }catch (Exception e)
       {
           errorMessage = e.toString();
           gui.showErrorMessage(1,errorMessage);
           return false;
       }
        for (Tile tile : ship.getOccupiedTiles())
            gui.showSquarePlayer(userId, tile.getPosition().x, tile.getPosition().y, SquareState.SHIP);
        return true;
    }


    @Override
    public void placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {
        var ship = new Ship(bowX, bowY, horizontal ? Orientation.HORIZONTAL : Orientation.VERTICAL, shipType);
        tryPlaceShipAndAddToGui(ship);
    }


    @Override
    public void removeShip(int playerNr, int posX, int posY) {
        var ship = grid.getTile(posX, posY).getShip();
        if (ship == null) return;
        removeShipFromGui(ship);
        ship.remove();
    }


    private void removeShipFromGui(Ship ship) {
        for (Tile tile : ship.getOccupiedTiles())
            gui.showSquarePlayer(userId, tile.getPosition().x, tile.getPosition().y, SquareState.WATER);
    }


    @Override
    public void removeAllShips(int playerNr) {
        clearGridAndGui();
    }


    @Override
    public void notifyWhenReady(int playerNr) {
        if (webSocketClient == null) return;

        if (grid.getShips().size() != ShipType.values().length) return;

        webSocketClient.emitPlaceShips(grid.getShips());
        gui.waitForOtherPlayerToBeReady();
    }


    @Override
    public void fireShot(int playerNr, int posX, int posY) {

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
        gui.notifyStartGame(userId);
    }


    public void endGame() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
