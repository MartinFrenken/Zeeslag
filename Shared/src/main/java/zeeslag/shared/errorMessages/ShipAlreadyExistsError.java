package zeeslag.shared.errorMessages;

import zeeslag.shared.ShipType;

public class ShipAlreadyExistsError extends ErrorMessage{

    private final ShipType shipTypeThatAlreadyExisted;
    public ShipAlreadyExistsError(ShipType shipTypeThatAlreadyExisted)
    {
        this.shipTypeThatAlreadyExisted=shipTypeThatAlreadyExisted;
    }
    public String toString()
    {
        return "You have already placed a "+shipTypeThatAlreadyExisted;
    }
}
