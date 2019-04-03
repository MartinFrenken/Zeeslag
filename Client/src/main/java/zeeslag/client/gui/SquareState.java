/*
 * Sea Battle Start project.
 */
package zeeslag.client.gui;

import zeeslag.shared.HitType;

/**
 * Indicate the state of a square.
 *
 * @author Nico Kuijpers
 */
public enum SquareState {
    WATER,        // No ship is positioned at this square
    SHIP,         // A ship is positioned at this square
    SHOT_MISSED,   // A shot was fired at this square, but no hit
    SHOT_HIT,      // A shot was fired at this square and a ship was hit
    SHIP_SUNK;     // A shot was fired at this square and a ship is sunk


    public static SquareState getSquareState(HitType shotType) {
        switch (shotType) {
            case MISSED:
                return SHOT_MISSED;
            case HIT:
                return SHOT_HIT;
            case SUNK:
                return SHOT_HIT;
            case ALL_SUNK:
                return SHOT_HIT;
            default:
                throw new IllegalStateException("Invalid shot type " + shotType);
        }
    }
}
