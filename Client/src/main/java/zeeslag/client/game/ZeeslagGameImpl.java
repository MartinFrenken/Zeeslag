/*
 * Sea Battle Start project.
 */
package zeeslag.client.game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zeeslag.client.gui.ShipType;
import zeeslag.client.gui.ZeeslagGui;

/**
 * The Sea Battle game. To be implemented.
 *
 * @author Nico Kuijpers
 */
public class ZeeslagGameImpl implements ZeeslagGame {

    private static final Logger log = LoggerFactory.getLogger(ZeeslagGameImpl.class);


    @Override
    public void registerPlayer(String name, String password, ZeeslagGui application, boolean singlePlayerMode) {
        log.debug("Register Player {} - password {}", name, password);
        throw new UnsupportedOperationException("Method registerPlayer() not implemented.");
    }


    @Override
    public void placeShipsAutomatically(int playerNr) {
        throw new UnsupportedOperationException("Method placeShipsAutomatically() not implemented.");
    }


    @Override
    public void placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {
        throw new UnsupportedOperationException("Method placeShip() not implemented.");
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
    public void startNewGame(int playerNr) {
        throw new UnsupportedOperationException("Method startNewGame() not implemented.");
    }

}
