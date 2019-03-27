package zeeslag.server;

import org.jetbrains.annotations.NotNull;
import zeeslag.shared.net.Grid;

import java.util.HashMap;
import java.util.Map;

class Zeeslag {

    @NotNull
    public GameState gameState = GameState.PREPARING;
    private final Grid[] grids = new Grid[]{
            new Grid(),
            new Grid()
    };
    private final Map<Integer, PlayerState> playerStates = new HashMap<>();


    public Grid getGrid(int userId) {
        return grids[userId];
    }


    public void addPlayer(int userId) {
        playerStates.put(userId, PlayerState.PLACING);
    }


    public void removePlayer(int userId) {
        playerStates.remove(userId);
    }


    public PlayerState getPlayerState(int userId) {
        return playerStates.get(userId);
    }


    public void setPlayerState(int userId, PlayerState state) {
        playerStates.put(userId, state);
    }


    public void setPlayerReady(int userId) {
        playerStates.put(userId, PlayerState.READY);

        if (!allPlayersReady()) return;

        playerStates.put(0, PlayerState.ATTACKING);
        playerStates.put(1, PlayerState.WAITING);

        gameState = gameState.getNextState();
    }


    private boolean allPlayersReady() {
        if (playerStates.size() != 2) return false;

        var allPlayersReady = true;
        for (PlayerState state : playerStates.values())
            allPlayersReady = allPlayersReady && state == PlayerState.READY;
        return allPlayersReady;
    }

}
