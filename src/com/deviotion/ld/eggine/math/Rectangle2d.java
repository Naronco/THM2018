package com.deviotion.ld.eggine.math;

public class Rectangle2d {
    private Vector2d position;
    private Dimension2d size;

    public Rectangle2d(Vector2d position, Dimension2d size) {
        this.position = position;
        this.size = size;
    }

    public Vector2d getPosition() {
        return this.position;
    }

    public Dimension2d getSize() {
        return this.size;
    }

    public void setPosition(Vector2d position) {
        this.position = position;
    }

    public void setSize(Dimension2d size) {
        this.size = size;
    }

    public boolean intersects(double x, double y) {
        return x >= position.getX() && y >= position.getY() && x < position.getX() + size.getWidth() && y < position.getY() + size.getHeight();
    }

    public boolean intersects(Vector2d position) {
        return intersects(position.getX(), position.getY());
    }

    public boolean intersects(Rectangle2d rect) {
        if (position.getX() + size.getWidth() < rect.position.getX()) return false;
        if (position.getY() + size.getHeight() < rect.position.getY()) return false;
        if (position.getX() >= rect.position.getX() + rect.size.getWidth()) return false;
        if (position.getY() >= rect.position.getY() + rect.size.getHeight()) return false;
        return true;
    }

	public Rectangle2d copy() {
        return new Rectangle2d(position.copy(), size.copy());
	}
}
