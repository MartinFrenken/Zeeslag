package zeeslag.server;

import org.jetbrains.annotations.NotNull;

interface Ai {

    void setGame(@NotNull Zeeslag game);

    void attack();

    int getId();

    void placeShips();

}
