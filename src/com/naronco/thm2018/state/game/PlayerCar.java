package com.naronco.thm2018.state.game;

import com.deviotion.ld.eggine.math.Vector2d;

public class PlayerCar {
	private Vector2d position = new Vector2d(0, 0);
	private boolean left, drifting;
	
	public void onHit(Obstacle obstacle) {
	}

	public Vector2d getPosition() {
		return position;
	}

	public void setPosition(Vector2d position) {
		this.position = position;
	}
	
	public double getRadius() {
		return 2.0;
	}

	public boolean isLeft() {
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isDrifting() {
		return drifting;
	}

	public void setDrifting(boolean drifting) {
		this.drifting = drifting;
	}

	public void fadeX(double x, double factor, double delta) {
		position.setX((position.getX() - x) * Math.pow(1 - factor, delta) + x);
	}

	public void drive(double delta) {
		getPosition().setY(getPosition().getY() + delta * 50 / 3.6);
	}
}
