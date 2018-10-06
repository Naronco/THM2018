package com.naronco.thm2018.state.game;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.input.Keyboard;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.Sprite3D;
import com.naronco.thm2018.Sprites;
import com.naronco.thm2018.graphics.Viewport;
import com.naronco.thm2018.state.GameState;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

public class ObstaclesGameState implements IGameState {
	private GameState game;
	private double time = 0;

	private double length = 64;

	private static final double TURN_SPEED = 3.5;

	private List<Obstacle> obstacles = new ArrayList<>();

	public ObstaclesGameState(GameState game) {
		this.game = game;
		obstacles.add(new Obstacle(new Sprite3D(new Vector2d(2, 50), Sprites.gulli, 1.3, 0, 0), 1.0));
	}

	@Override
	public int getFloorColor(double x, double y) {
		return game.getBaseFloorColor(x, y);
	}

	@Override
	public void load() {
		game.getCar().setPosition(new Vector2d(0, 0));
	}

	@Override
	public void unload() {

	}

	@Override
	public void render(Screen screen) {
		Viewport viewport = game.getViewport();
		for (Obstacle obstacle : obstacles) {
			viewport.renderSprite3D(screen, obstacle.getSprite());
		}
	}

	@Override
	public void update(double delta) {
		time += delta;
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
