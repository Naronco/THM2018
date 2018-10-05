package com.deviotion.ld.eggine.map;

import com.deviotion.ld.eggine.math.Dimension2d;
import com.deviotion.ld.eggine.math.Vector2d;

import java.lang.reflect.Array;
import java.util.Arrays;

/**
 * Eggine
 * A last minute game engine for Ludum Dare.
 *
 * @author Alex Nicholson (TechnoCF)
 *
 */

public class Map {

    private int[] map;
    private Dimension2d dimension;

    public Map (Dimension2d dimension) {
        this.dimension = dimension;
        this.map = new int[(int) this.dimension.getWidth() * (int) this
                .dimension.getHeight()];

        Arrays.fill(this.map, -1);
    }

    public int[] getMap () {
        return this.map;
    }
    public Dimension2d getDimension() {
        return this.dimension;
    }

    public int getTile (int x, int y) {
        if (x >= 0 && y >= 0 && x < this.dimension.getWidth() && y < this
                .dimension.getHeight()) {
            return this.map[x + y * (int) this.dimension.getWidth()];
        }

        return -1;
    }
    public int getTile (Vector2d location) {
        return this.getTile((int) location.getX(), (int) location.getY());
    }

    public void setTile (int x, int y, int value) {
        if (x >= 0 && y >= 0 && x < this.dimension.getWidth() && y < this
                .dimension.getHeight()) {
            this.map[x + y * (int) this.dimension.getWidth()] = value;
        }
    }
    public void setTile (Vector2d location, int value) {
        this.setTile((int) location.getX(), (int) location.getY(), value);
    }

}
