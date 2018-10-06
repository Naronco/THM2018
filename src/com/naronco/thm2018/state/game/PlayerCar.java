package com.naronco.thm2018.state.game;

import com.deviotion.ld.eggine.math.Vector2d;

public class PlayerCar {
	private Vector2d position = new Vector2d(0, 0);
	private boolean left, drifting;
	private double speed = 70;
	
	public void onHit(Obstacle obstacle) {
		//throw new Error("Crash");
	}

	public Vector2d getPosition() {
		return position;
	}

	public void setPosition(Vector2d position) {
		this.position = position;
	}
	
	public double getRadius() {
		return 1.0;
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
		getPosition().setY(getPosition().getY() + delta * speed / 3.6);
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}
}
