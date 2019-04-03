package zeeslag.client.game;

import zeeslag.shared.HitType;

public class ZeeslagWebSocketEventHandler implements ZeeslagWebSocketEventListener {

    private final ZeeslagGameImpl zeeslagGame;


    ZeeslagWebSocketEventHandler(ZeeslagGameImpl zeeslagGame) {
        this.zeeslagGame = zeeslagGame;
    }


    @Override
    public void onAttackResult(int to, int x, int y, HitType hitType) {
        zeeslagGame.onAttackResult(to, x, y, hitType);
    }


    @Override
    public void onReady() {
        zeeslagGame.notifyWhenReady();
    }


    @Override
    public void onStart() {
        zeeslagGame.startGame();
    }


    @Override
    public void onReset() {
        zeeslagGame.onReset();
    }

}
