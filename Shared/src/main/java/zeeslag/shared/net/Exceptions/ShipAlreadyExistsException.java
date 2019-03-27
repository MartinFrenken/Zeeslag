package zeeslag.shared.net.Exceptions;

import zeeslag.shared.net.ShipType;

class ShipAlreadyExistsException extends Exception {

    private final ShipType shipTypeThatAlreadyExisted;
    public ShipAlreadyExistsException(ShipType shipTypeThatAlreadyExisted)
    {
        this.shipTypeThatAlreadyExisted=shipTypeThatAlreadyExisted;
    }
    public String toString()
    {
        return "You have alraedy placed a "+shipTypeThatAlreadyExisted;
    }
}
