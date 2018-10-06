package com.naronco.thm2018.state.game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.Sprite3D;
import com.naronco.thm2018.Sprites;
import com.naronco.thm2018.alley.Alley;
import com.naronco.thm2018.alley.Bridge;
import com.naronco.thm2018.alley.House;
import com.naronco.thm2018.graphics.Viewport;
import com.naronco.thm2018.state.GameState;

public class ObstaclesGameState implements IGameState {
	private GameState game;
	private double time = 0;
	
	private double speedUpdateTime = 0;
	private double nextSpeedUpdate = 0;

	private double length = 128;
	private double houseLength = 32;
	private double houseSpacing = 8;
	
	private static final double TURN_SPEED = 3.5;

	private List<Obstacle> obstacles = new ArrayList<>();

	private Alley environment = new Alley();
	
	public ObstaclesGameState(GameState game) {
		this.game = game;
		
		this.environment = new Alley();
		for (double y = 0; y < length - houseLength; y += houseSpacing + houseLength) {
			//environment.addObject(new House(new Vector2d(-6, y), new Vector2d(-12, y + houseLength)));
			//environment.addObject(new House(new Vector2d(12, y), new Vector2d(6, y + houseLength)));
			
			environment.addObject(new Bridge(new Vector2d(-6, y), new Vector2d(6, y + 10)));
		}
	}

	@Override
	public int getCeilingColor(double x, double y) {
		return environment.getCeilingColor(x, y);
	}

	@Override
	public int getFloorColor(double x, double y) {
		return game.getBaseFloorColor(x, y);
	}

	@Override
	public void load() {
		game.getCar().setPosition(new Vector2d(0, 0));
		
		double y = 30.0;
		while (y < length) {
			obstacles.add(new Obstacle(new Sprite3D(new Vector2d((Math.random() - 0.5) * 8.0, y), Sprites.gulli, 10, 5, 0, 0), 1.0));
			y += 10.0 + Math.random() * 10.0;
		}
	}

	@Override
	public void unload() {
		obstacles.clear();
	}

	@Override
	public void render(Screen screen) {
		Viewport viewport = game.getViewport();
		
		environment.render(screen, viewport);
		
		for (Obstacle obstacle : obstacles) {
			viewport.renderSprite3D(screen, obstacle.getSprite());
		}
	}

	@Override
	public void update(double delta) {
		time += delta;
		
		speedUpdateTime += delta;
		if (speedUpdateTime >= nextSpeedUpdate) {
			speedUpdateTime = 0;
			nextSpeedUpdate = 0.1 + Math.random() * 0.5;
			game.getCar().setSpeed(70 + (Math.random() - 0.5) * 7.5);
		}
		
		game.getCar().drive(delta);
		
		for (Obstacle obstacle : obstacles) {
			double distSquared = obstacle.getSprite().getPosition().subtract(game.getCarPos()).getLengthSquared();
			if (distSquared < (game.getCar().getRadius() + obstacle.getRadius()) * (game.getCar().getRadius() + obstacle.getRadius())) {
				game.getCar().onHit(obstacle);
			}
		}

		if (game.getCar().getPosition().getY() > length)
			game.transitionIntoNextState();

		if (game.getKeyboard().isPressed(KeyEvent.VK_LEFT)) {
			game.getCar().getPosition().setX(Math.max(game.getCar().getPosition().getX() - delta * TURN_SPEED, -4));
		} else if (game.getKeyboard().isPressed(KeyEvent.VK_RIGHT)) {
			game.getCar().getPosition().setX(Math.min(game.getCar().getPosition().getX() + delta * TURN_SPEED, 4));
		}
	}
}
