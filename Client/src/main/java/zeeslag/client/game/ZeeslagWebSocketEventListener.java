package zeeslag.client.game;

interface ZeeslagWebSocketEventListener {

    void onAttack(int userId, int x, int y);

    void onReady(int userId);

    void onStart();

    void onEnd();

}
