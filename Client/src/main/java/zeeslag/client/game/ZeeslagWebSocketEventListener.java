package zeeslag.client.game;

import zeeslag.shared.HitType;
import zeeslag.shared.Ship;

interface ZeeslagWebSocketEventListener {

    void onAttackResult(int to, int x, int y, HitType hitType);

    void onReady();

    void onStart();

    void onReset();

    void onPlaceShips(int userId, Ship[] ships);

}
