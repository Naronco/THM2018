package com.deviotion.ld.eggine.math;

/**
 * Eggine
 * A last minute game engine for Ludum Dare.
 *
 * @author Alex Nicholson (TechnoCF)
 *
 */

public class Dimension2d {

    private double width;
    private double height;

    public Dimension2d (double width, double height) {
        this.width = width;
        this.height = height;
    }

    public double getWidth () {
        return this.width;
    }
    public double getHeight () {
        return this.height;
    }

    public void setWidth (double width) {
        this.width = width;
    }
    public void setHeight (double height) {
        this.height = height;
    }

	public Dimension2d copy() {
        return new Dimension2d(width, height);
	}
}