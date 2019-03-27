package zeeslag.server;

import org.jetbrains.annotations.NotNull;
import zeeslag.shared.net.Ship;

public interface WebSocketServerEventListener {

    void onPlaceShips(int userId, @NotNull Ship[] ships);

    void onPlayerJoin(int userId);

    void onPlayerLeave(int userId);

    void onAttack(int userId, int x, int y);

}
