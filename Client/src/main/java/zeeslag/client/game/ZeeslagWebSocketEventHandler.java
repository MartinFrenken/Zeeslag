package zeeslag.client.game;

public class ZeeslagWebSocketEventHandler implements ZeeslagWebSocketEventListener {

    private final ZeeslagGameImpl zeeslagGame;


    ZeeslagWebSocketEventHandler(ZeeslagGameImpl zeeslagGame) {
        this.zeeslagGame = zeeslagGame;
    }


    @Override
    public void onAttack(int userId, int x, int y) {
        zeeslagGame.fireShot(userId, x, y);
    }


    @Override
    public void onReady(int userId) {
        zeeslagGame.notifyWhenReady(userId);
    }


    @Override
    public void onStart() {
        zeeslagGame.startGame();
    }


    @Override
    public void onEnd() {
        zeeslagGame.endGame();
    }

}
