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

import java.util.Objects;
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
    @Nullable
    private ZeeslagWebSocketClient webSocketClient;
    private String name;


    public ZeeslagGameImpl(ZeeslagGui gui) {
        this.gui = gui;
    }


    @Override
    public void loginPlayer(String name, String password, boolean singlePlayerMode) {
        log.debug("Register Player {} - password {}", name, password);
        this.name = name;
        var authData = api.login(name, password);
        if (authData == null) {
            gui.showErrorMessage(userId, "Failed to login");
            return;
        }

        userId = authData.id;

        gui.setPlayerNumber(userId, name);
        webSocketClient = new ZeeslagWebSocketClient("ws://localhost:3000/ws", authData.token, userId, new ZeeslagWebSocketEventHandler(this));
        if (singlePlayerMode)
            webSocketClient.emitSinglePlayer();
    }


    @Override
    public void placeShipsAutomatically() {
        clearGridAndGui();
        var random = new Random();
        for (ShipType shipType : ShipType.values()) {
            var isHorizontal = false;
            var x = 0;
            var y = 0;
            Ship ship = null;

            while (ship == null || !tryPlaceShipAndAddToGui(ship, true)) {
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


    private boolean tryPlaceShipAndAddToGui(Ship ship, boolean auto) {

        if (grid.tryPlace(ship)) {
            for (Tile tile : ship.getOccupiedTiles())
                gui.showSquarePlayer(userId, tile.getPosition().x, tile.getPosition().y, SquareState.SHIP);
        } else {
            gui.showErrorMessage(userId, grid.getErrorMessage().toString());
            return false;
        }
        if (!auto)
            gui.showErrorMessage(userId, "Can't place a ship there");
        return true;
    }


    @Override
    public void placeShip(ShipType shipType, int bowX, int bowY, boolean horizontal) {
        var ship = new Ship(bowX, bowY, horizontal ? Orientation.HORIZONTAL : Orientation.VERTICAL, shipType);
        tryPlaceShipAndAddToGui(ship, false);
    }


    @Override
    public void removeShip(int posX, int posY) {
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
    public void removeAllShips() {
        clearGridAndGui();
    }


    @Override
    public void notifyWhenReady() {
        if (grid.getShips().size() != ShipType.values().length) return;

        Objects.requireNonNull(webSocketClient).emitPlaceShips(grid.getShips());
        gui.waitForOtherPlayerToBeReady();
    }


    @Override
    public void fireShotGui(int posX, int posY) {
        Objects.requireNonNull(webSocketClient).emitAttack(posX, posY);
        gui.playerFiresShot(userId, HitType.HIT);
    }


    @Override
    public void resetGame() {
        Objects.requireNonNull(webSocketClient).emitReset();
    }


    @Override
    public void stop() {
        if (webSocketClient != null) webSocketClient.disconnect();
    }


    @Override
    public void onAttackResult(int to, int x, int y, HitType hitType) {
        if (userId == to) {
            gui.opponentFiresShot(userId, hitType);
            gui.showSquarePlayer(userId, x, y, SquareState.getSquareState(hitType));
        } else {
            gui.playerFiresShot(userId, hitType);
            gui.showSquareOpponent(userId, x, y, SquareState.getSquareState(hitType));
        }
    }


    void startGame() {
        gui.notifyStartGame(userId);
    }


    void onReset() {
        gui.setPlayerNumber(userId, name);
        clearGridAndGui();

        for (var x = 0; x < grid.getWidth(); x++)
            for (var y = 0; y < grid.getHeight(); y++)
                gui.showSquareOpponent(userId, x, y, SquareState.WATER);
    }


    public static Grid getGrid() {
        return grid;
    }

}
