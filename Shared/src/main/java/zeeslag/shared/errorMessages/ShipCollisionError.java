package zeeslag.shared.errorMessages;

import org.jetbrains.annotations.NotNull;

public class ShipCollisionError extends ErrorMessage{

    @NotNull
    public String toString() {
        return "You cant place ships on top of other ships";
    }
}
