package zeeslag.shared.net;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Grid {
    public final int width;
    public final int height;
    public final Tile[][] tiles;

    public Grid(final int width, final int height) {
        this.width = width;
        this.height = height;

        tiles = new Tile[height][];

        for (int x = 0; x < width; ++x)
        {
            for(int y=0;y<height;y++) {
                if (tiles[x]==null) tiles[x] = new Tile[height];
                tiles[x][y] = new Tile(new Position(x,y),this);
            }
        }
    }

    @Nullable
    public Tile at(@NotNull final Position position) {
        final Position normalized = normalize(position);
        return tiles[normalized.y][normalized.x];
    }

    public int calculateDistance(@NotNull final Position source, @NotNull final Position target) {
        final Position normalizedSource = normalize(source);
        final Position normalizedTarget = normalize(target);

        final int dx = Math.abs(normalizedSource.x - normalizedTarget.x);
        final int dy = Math.abs(normalizedSource.y - normalizedTarget.y);

        final int toroidal_dx = Math.min(dx, width - dx);
        final int toroidal_dy = Math.min(dy, height - dy);

        return toroidal_dx + toroidal_dy;
    }

    @NotNull
    public Position normalize(@NotNull final Position position) {
        final int x = ((position.x % width) + width) % width;
        final int y = ((position.y % height) + height) % height;
        return new Position(x, y);
    }


}