package com.naronco.thm2018.state.game;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.Sprite3D;
import com.naronco.thm2018.Sprites;
import com.naronco.thm2018.alley.Alley;
import com.naronco.thm2018.alley.Bridge;
import com.naronco.thm2018.alley.House;
import com.naronco.thm2018.graphics.Viewport;
import com.naronco.thm2018.maze.Way;
import com.naronco.thm2018.state.GameState;
import com.sun.org.apache.xalan.internal.xsltc.runtime.InternalRuntimeError;

public class ObstaclesGameState implements IGameState {
	private GameState game;
	private double time = 0;

	private double speedUpdateTime = 0;
	private double nextSpeedUpdate = 0;

	private double length;

	private static final double TURN_SPEED = 3.5;

	private List<Obstacle> obstacles = new ArrayList<>();

	private Alley environment = new Alley();

	private Way way;

	public ObstaclesGameState(GameState game) {
		this.game = game;

		this.environment = new Alley();
	}

	@Override
	public int getCeilingColor(double x, double y) {
		return environment.getCeilingColor(x, y);
	}

	@Override
	public void render2D(Screen screen) {

	}

	@Override
	public int getFloorColor(double x, double y) {
		return game.getBaseFloorColor(x, y);
	}

	public void preload(Way way) {
		this.way = way;
	}

	@Override
	public void load() {
		game.getCar().setPosition(new Vector2d(0, 0));

		Random random = new Random(way.getSeed());
		length = 200;

		double y = 50;
		while (y < length) {
			int buildingType = random.nextInt(3);
			double width = 20 + random.nextDouble() * 20;
			double gap = 5 + random.nextDouble() * 20;
			if (buildingType == 0) {
				environment.addObject(new Bridge(new Vector2d(-15, y), new Vector2d(15, y + width)));
			} else if (buildingType == 1) {
				if (random.nextBoolean()) {
					environment.addObject(new House(new Vector2d(-6, y), new Vector2d(-12, y + width)));
				} else {
					environment.addObject(new House(new Vector2d(12, y), new Vector2d(6, y + width)));
				}
			} else if (buildingType == 2) {
				double shiftFactor = random.nextDouble() * 0.4 + 0.5;
				environment.addObject(new House(new Vector2d(-6, y), new Vector2d(-12, y + width * shiftFactor)));
				environment.addObject(new House(new Vector2d(12, y + width * (1 - shiftFactor)), new Vector2d(6, y + width * shiftFactor)));
			} else throw new InternalRuntimeError("Unexpected building type");

			if (y + width + gap >= length) {
				length = y + width;
				break;
			} else {
				y += width + gap;
			}
		}

		y = 40.0;
		while (y < length) {
			obstacles.add(new Obstacle(new Sprite3D(new Vector2d((random.nextDouble() - 0.5) * 7.75, y), Sprites.gulli, 10, 5, 0, 0), 0.1));
			y += 8.0 + random.nextDouble() * 8.0;
		}
	}

	@Override
	public void unload() {
		obstacles.clear();
		environment.clear();
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

		game.getCar().drive(game.getKeyboard().isPressed(KeyEvent.VK_SPACE) ? delta * 10 : delta);

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
