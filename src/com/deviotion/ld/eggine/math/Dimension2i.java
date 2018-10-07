package com.deviotion.ld.eggine.math;

/**
 * Eggine
 * A last minute game engine for Ludum Dare.
 *
 * @author Alex Nicholson (TechnoCF)
 *
 */

public class Dimension2i {

    private int width;
    private int height;

    public Dimension2i(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getWidth () {
        return this.width;
    }
    public int getHeight () {
        return this.height;
    }

    public void setWidth (int width) {
        this.width = width;
    }
    public void setHeight (int height) {
        this.height = height;
    }

	public Dimension2i copy() {
        return new Dimension2i(width, height);
	}
}