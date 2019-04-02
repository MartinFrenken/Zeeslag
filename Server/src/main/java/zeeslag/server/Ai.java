package zeeslag.server;

import org.jetbrains.annotations.NotNull;

public interface Ai {

    void setGame(@NotNull Zeeslag game);

    void attack();

    int getId();

    void placeShips();

}
