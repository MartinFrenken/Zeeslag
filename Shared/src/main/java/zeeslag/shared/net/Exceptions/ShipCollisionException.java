package zeeslag.shared.net.Exceptions;

class ShipCollisionException extends Exception {

    public String toString()
    {
        return "You cant place ships on top of other ships";
    }
}
