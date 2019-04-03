package zeeslag.shared;

import org.jetbrains.annotations.Nullable;

public class Position {

    public final int x;
    public final int y;


    Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Position position = (Position) o;

        if (x != position.x) return false;
        return y == position.y;
    }


    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

}