package zeeslag.client.game;

import zeeslag.client.gui.SquareState;
import zeeslag.client.gui.ZeeslagGui;
import zeeslag.shared.net.HitType;

public class MockGui implements ZeeslagGui
{
    public void setPlayerNumber(int playerNr, String name){}
    public void setOpponentName(int playerNr, String name){}
    public void notifyStartGame(int playerNr){}
    public void waitForOtherPlayerToBeReady(){}
    public void playerFiresShot(int playerNr, HitType shotType){}
    public void opponentFiresShot(int playerNr, HitType shotType){}
    public void showSquarePlayer(int playerNr, int posX, int posY, SquareState squareState){}
    public void showSquareOpponent(int playerNr, int posX, int posY, SquareState squareState){}
    public void showErrorMessage(int playerNr, String errorMessage){}
}
