/*
 * Sea Battle Start project.
 */
package zeeslag.client.game;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zeeslag.shared.net.ShipType;
import zeeslag.client.gui.ZeeslagGui;
import zeeslag.shared.net.Grid;
import zeeslag.shared.net.Orientation;
import zeeslag.shared.net.boats.Ship;

/**
 * The Sea Battle game. To be implemented.
 *
 * @author Nico Kuijpers
 */
public class ZeeslagGameImpl implements ZeeslagGame {

    private static final Logger log = LoggerFactory.getLogger(ZeeslagGameImpl.class);
    private static final ZeeslagApi api = new ZeeslagApi("http://localhost:3000/api");
    private final ZeeslagGui gui;
    private Ship[] friendlyShips;
    private Ship[] enemyShips;
    int amountOfFriendlyShipsPlaces = 0;
    int amountOfEnemyShipsPlaces = 0;
    boolean friendlyPlayerReady=false;
    boolean enemyPlayerReady=false;
   private static final int totalAmountOfShips = 5;

    public static final int CarrierSize = 5;
    public static final int BattleShipSize = 4;
    public static final int CruiserSize = 3;
    public static final int SubmarineSize = 2;
    public static final int MineSweperSize = 1;
    @Nullable
    private ZeeslagWebSocketClient webSocketClient;


    public ZeeslagGameImpl(ZeeslagGui gui)
    {
        this.gui = gui;
        friendlyShips=new Ship[totalAmountOfShips];
        enemyShips = new Ship[totalAmountOfShips];

        Grid keepo = new Grid(10,10);
        Grid dansgame = keepo;
    }


    @Override
    public void loginPlayer(String name, String password, boolean singlePlayerMode) {
        log.debug("Register Player {} - password {}", name, password);
        var authData = api.login(name, password);
        if (authData == null) {//TODO show error message
            return;
        }

        gui.setPlayerNumber(authData.id, name);
        webSocketClient = new ZeeslagWebSocketClient("ws://localhost:3000/ws", authData.token, 0, new ZeeslagWebSocketEventHandler(this));
        webSocketClient.emitAttack(3, 5);
    }


    @Override
    public void placeShipsAutomatically(int playerNr) {
        throw new UnsupportedOperationException("Method placeShipsAutomatically() not implemented.");
    }


    @Override
    public boolean placeShip(int playerNr, ShipType shipType, int bowX, int bowY, boolean horizontal) {
                if(playerNr==0)
           {

           if(amountOfFriendlyShipsPlaces<totalAmountOfShips)
           {
               if(horizontal)
                   friendlyShips[amountOfFriendlyShipsPlaces]= new Ship(bowX,bowY, Orientation.HORIZONTAL,shipType);
               if(!horizontal)
                   friendlyShips[amountOfFriendlyShipsPlaces]= new Ship(bowX,bowY,Orientation.VERTICAL,shipType);
               amountOfFriendlyShipsPlaces++;
           }
           else
               {
                   friendlyPlayerReady=true;
               }
       }
        if(playerNr==1)
        {

            if(amountOfEnemyShipsPlaces<totalAmountOfShips)
            {
                if(horizontal)
                    enemyShips[amountOfEnemyShipsPlaces]= new Ship(bowX,bowY,Orientation.HORIZONTAL,shipType);
                if(!horizontal)
                    enemyShips[amountOfEnemyShipsPlaces]= new Ship(bowX,bowY,Orientation.VERTICAL,shipType);
                amountOfEnemyShipsPlaces++;
            }
            else
                {
                    enemyPlayerReady=true;
                }
        }
        else
            {
                System.out.println(playerNr);
            }
        return true;
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
    public Ship[] getFriendlyShips() {
        return friendlyShips;
    }

    @Override
    public Ship[] getEnemyShips() {
        return enemyShips;
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
