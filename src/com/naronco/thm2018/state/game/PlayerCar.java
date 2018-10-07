package com.naronco.thm2018.state.game;

import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.Game;

public class PlayerCar {
	private Vector2d position = new Vector2d(0, 0);
	private boolean left, drifting;
	private double speed = 0;

	private boolean isHit;
	private double hitTimer = 0;
	private int health = 3;

	public static final double HIT_SLOWDOWN_END = 0.4;
	public static final double HIT_SPEEDUP_START = 0.8;
	public static final double HIT_DURATION = 3;

	public boolean onHit(Obstacle obstacle) {
		if (isHit || health == 0)
			return false;
		isHit = true;
		hitTimer = 0;
		health--;
		return true;
	}

	public void reset() {
		setPosition(new Vector2d(0, 0));
		setHealth(3);
		setHitTimer(0);
		setHit(false);
		setDrifting(false);
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
		if (isHit && hitTimer < HIT_SPEEDUP_START) {
			int step = (int) (hitTimer * 5);
			return (step & 1) == 0;
		}
		return left;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public boolean isDrifting() {
		if (isHit && hitTimer < HIT_SPEEDUP_START) {
			int step = (int) (hitTimer * 7);
			return (step & 1) == 0;
		}
		return drifting;
	}

	public void setDrifting(boolean drifting) {
		this.drifting = drifting;
	}

	public void fadeX(double x, double factor, double delta) {
		position.setX((position.getX() - x) * Math.pow(1 - factor, delta) + x);
	}

	public void drive(double delta) {
		hitTimer += delta;
		if (hitTimer > HIT_DURATION)
			isHit = false;
		getPosition().setY(getPosition().getY() + delta * getSpeed() / 3.6);
	}

	public double getSpeed() {
		double speedMul = 1;
		if (isHit) {
			if (hitTimer < HIT_SLOWDOWN_END)
				speedMul = 1 - hitTimer / HIT_SLOWDOWN_END;
			else if (hitTimer < HIT_SPEEDUP_START)
				speedMul = 0;
			else
				speedMul = (hitTimer - HIT_SPEEDUP_START) / (HIT_DURATION - HIT_SPEEDUP_START);
		}
		return speed * speedMul;
	}

	public double getRawSpeed() {
		return speed;
	}

	public void setSpeed(double speed) {
		this.speed = speed;
	}

	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public boolean isHit() {
		return isHit;
	}

	public void setHit(boolean hit) {
		isHit = hit;
	}

	public double getHitTimer() {
		return hitTimer;
	}

	public void setHitTimer(double hitTimer) {
		this.hitTimer = hitTimer;
	}
}
