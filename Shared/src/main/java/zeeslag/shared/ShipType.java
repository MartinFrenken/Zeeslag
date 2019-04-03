/*
 * Sea Battle Start project.
 */
package zeeslag.shared;

/**
 * Indicate type of ship.
 *
 * @author Nico Kuijpers
 */
public enum ShipType {
    AIRCRAFT_CARRIER,  // Aircraft carrier (size 5)
    BATTLESHIP,       // Battle ship (size 4)
    CRUISER,          // Cruister (size 3)
    SUBMARINE,        // Submarine (size 3)
    MINESWEEPER;      // Mine sweeper (size 2)


    public int getSize() {
        if (this == ShipType.AIRCRAFT_CARRIER) {
            return 5;
        }
        if (this == ShipType.BATTLESHIP) {
            return 4;
        }
        if (this == ShipType.CRUISER) {
            return 3;
        }
        if (this == ShipType.SUBMARINE) {
            return 3;
        }
        if (this == ShipType.MINESWEEPER) {
            return 2;
        }
        throw new IllegalStateException();
    }
}
