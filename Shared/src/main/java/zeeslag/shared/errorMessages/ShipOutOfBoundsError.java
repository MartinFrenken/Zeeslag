package zeeslag.shared.errorMessages;

import org.jetbrains.annotations.NotNull;

public class ShipOutOfBoundsError extends ErrorMessage{

    @NotNull
    public String toString() {
        return "Ship was placed out of bounds";
    }

}
