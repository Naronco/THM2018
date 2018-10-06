package com.naronco.thm2018.state.game;

import com.naronco.thm2018.Sprite3D;

public class Obstacle {
	private Sprite3D sprite;
	private double radius;
	
	public Obstacle(Sprite3D sprite, double radius) {
		this.setSprite(sprite);
		this.setRadius(radius);
	}

	public Sprite3D getSprite() {
		return sprite;
	}

	public void setSprite(Sprite3D sprite) {
		this.sprite = sprite;
	}

	public double getRadius() {
		return radius;
	}

	public void setRadius(double radius) {
		this.radius = radius;
	}
}
