/*
 * Sea Battle Start project.
 */
package seabattlegui;

/**
 * Interface provided by the graphical user interface of the sea battle game.
 *
 * @author Nico Kuijpers
 */
public interface ISeaBattleGUI {

    /**
     * Set player number.
     *
     * @param playerNr identification of player
     * @param name     player's name
     */
    void setPlayerNumber(int playerNr, String name);

    /**
     * Set the name of the opponent in the GUI.
     *
     * @param playerNr identification of player
     * @param name     opponent's name
     */
    void setOpponentName(int playerNr, String name);

    /**
     * Notification that the game has started.
     *
     * @param playerNr identification of player
     */
    void notifyStartGame(int playerNr);

    /**
     * Communicate the result of a shot fired by the player.
     * The result of the shot will be one of the following:
     * MISSED  - No ship was hit
     * HIT     - A ship was hit
     * SUNK    - A ship was sunk
     * ALLSUNK - All ships are sunk
     *
     * @param playerNr identification of player
     * @param shotType result of shot fired by player
     */
    void playerFiresShot(int playerNr, ShotType shotType);

    /**
     * Communicate the result of a shot fired by the opponent.
     * The result of the shot will be one of the following:
     * MISSED  - No ship was hit
     * HIT     - A ship was hit
     * SUNK    - A ship was sunk
     * ALLSUNK - All ships are sunk
     *
     * @param playerNr identification of player
     * @param shotType result of shot fired by opponent
     */
    void opponentFiresShot(int playerNr, ShotType shotType);

    /**
     * Show state of a square in the ocean area.
     * The new state of the square will be shown in the area where
     * the ships of the player are placed (ocean area).
     *
     * @param playerNr    identification of player
     * @param posX        x-position of square
     * @param posY        y-position of square
     * @param squareState state of square
     */
    void showSquarePlayer(int playerNr, int posX, int posY, SquareState squareState);

    /**
     * Show state of a square in the target area.
     * The new state of the square will be shown in the area where
     * the ships of the opponent are placed (target area)
     *
     * @param playerNr    identification of player
     * @param posX        x-position of square
     * @param posY        y-position of square
     * @param squareState state of square
     */
    void showSquareOpponent(int playerNr, int posX, int posY, SquareState squareState);

    /**
     * Show error message.
     *
     * @param playerNr     identification of player
     * @param errorMessage error message
     */
    void showErrorMessage(int playerNr, String errorMessage);
}
