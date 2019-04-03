package zeeslag.client.game;

import zeeslag.shared.HitType;

interface ZeeslagWebSocketEventListener {

    void onAttackResult(int to, int x, int y, HitType hitType);

    void onReady(int userId);

    void onStart();

    void onReset();

}
