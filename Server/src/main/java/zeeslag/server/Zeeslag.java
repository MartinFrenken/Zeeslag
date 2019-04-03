package zeeslag.server;

import org.jetbrains.annotations.NotNull;
import zeeslag.shared.Grid;
import zeeslag.shared.HitType;
import zeeslag.shared.Ship;

import java.util.HashMap;
import java.util.Map;

class Zeeslag {

    @NotNull
    private GameState gameState = GameState.PREPARING;
    private final Grid[] grids = new Grid[]{
            new Grid(),
            new Grid()
    };
    private final Map<Integer, PlayerState> playerStates = new HashMap<>();
    @NotNull
    private final Ai ai;
    private boolean isSinglePlayer;


    Zeeslag(Ai ai) {
        this.ai = ai;
        ai.setGame(this);
    }


    Grid getGrid(int userId) {
        return grids[userId];
    }


    void addPlayer(int userId) {
        playerStates.put(userId, PlayerState.PLACING);
    }


    void removePlayer(int userId) {
        playerStates.remove(userId);
    }


    PlayerState getPlayerState(int userId) {
        return playerStates.get(userId);
    }


    void setPlayerState(int userId, PlayerState state) {
        playerStates.put(userId, state);
    }


    void setPlayerReady(int userId) {
        playerStates.put(userId, PlayerState.READY);

        if (!allPlayersReady()) return;

        playerStates.put(0, PlayerState.ATTACKING);
        playerStates.put(1, PlayerState.WAITING);

        setGameState(getGameState().getNextState());
    }


    private boolean allPlayersReady() {
        if (playerStates.size() != 2) return false;

        var allPlayersReady = true;
        for (PlayerState state : playerStates.values())
            allPlayersReady = allPlayersReady && state == PlayerState.READY;
        return allPlayersReady;
    }


    @NotNull
    HitType attack(int to, int x, int y) {
        var tile = grids[to].getTile(x, y);
        var ship = tile.getShip();

        for (int userId : playerStates.keySet())
            playerStates.put(userId, playerStates.get(userId).getNextState());

        if (ship == null) return HitType.MISSED;

        ship.destroyTile(tile);

        if (allShipsSunk(to)) {
            gameState = gameState.getNextState();
            return HitType.ALL_SUNK;
        }

        if (ship.hasSunk())
            return HitType.SUNK;

        return HitType.HIT;
    }


    @NotNull
    GameState getGameState() {
        return gameState;
    }


    void setGameState(@NotNull GameState gameState) {
        this.gameState = gameState;
    }


    private boolean allShipsSunk(int userId) {
        var allSunk = true;
        for (Ship ship : grids[userId].getShips()) {
            allSunk = allSunk && ship.hasSunk();
        }
        return allSunk;
    }


    void setIsSinglePlayer(boolean isSinglePlayer) {
        this.isSinglePlayer = isSinglePlayer;
    }


    boolean isSinglePlayer() {
        return isSinglePlayer;
    }


    @NotNull Ai getAi() {
        return ai;
    }


    void reset() {
        gameState = GameState.PREPARING;
        for (int i = 0; i < grids.length; i++) grids[i] = new Grid();
        for (int i : playerStates.keySet()) playerStates.put(i, PlayerState.PLACING);
    }

}
