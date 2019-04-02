/*
 * Sea Battle Start project.
 */
package zeeslag.shared.net;

/**
 * Indicate the result of a shot.
 *
 * @author Nico Kuijpers
 */
public enum HitType {
    MISSED,   // Shot missed
    HIT,      // Shot hit
    SUNK,     // Ship sunk
    ALL_SUNK  // All ships sunk
}
