package com.naronco.thm2018.state;

import com.deviotion.ld.eggine.graphics.Font;
import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.input.Keyboard;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.Sprites;
import com.naronco.thm2018.graphics.IViewportDataSource;
import com.naronco.thm2018.graphics.Viewport;
import com.naronco.thm2018.maze.Level;
import com.naronco.thm2018.maze.MazeGenerator;
import com.naronco.thm2018.maze.Point;
import com.naronco.thm2018.maze.Way;
import com.naronco.thm2018.state.game.DecisionGameState;
import com.naronco.thm2018.state.game.IGameState;
import com.naronco.thm2018.state.game.ObstaclesGameState;
import com.naronco.thm2018.state.game.PlayerCar;

import java.util.List;
import java.util.Random;

public class GameState implements IState, IViewportDataSource {
	private StateManager parts = new StateManager();
	private PlayerCar car = new PlayerCar();
	private Viewport viewport;
	private Keyboard keyboard;

	private DecisionGameState decisionState;
	private ObstaclesGameState obstaclesState;

	private Level level;

	double time = 0;

	public GameState(Dimension2d size, Keyboard keyboard) {
		this.viewport = new Viewport(size);
		this.keyboard = keyboard;
		viewport.setDataSource(this);
	}

	@Override
	public void load() {
		decisionState = new DecisionGameState(this);
		obstaclesState = new ObstaclesGameState(this);

		level = new MazeGenerator(new Random()).generate(10, 20);
		obstaclesState.preload(new Way(level.getPoint(), 0, "Ausfahrt"));

		parts.setState(obstaclesState);
	}

	@Override
	public void unload() {
	}

	@Override
	public void render(Screen screen) {
		viewport.render(screen);

		parts.render(screen);

		int vibration = (int) Math.round(Math.sin(time * 5) * Math.cos(time * 3 + 10) * 0.5 + 0.5);
		if (getCar().isDrifting())
			viewport.renderSprite3D(screen, getCarPos(), getCar().isLeft() ? 0 : (94 + 60), 0, 94, 61, Sprites.car, 0, -vibration);
		else
			viewport.renderSprite3D(screen, getCarPos(), 94, 0, 60, 61, Sprites.car, 0, -vibration);

		viewport.postProcess(screen);

		((IGameState)parts.getCurrentState()).render2D(screen);
	}

	@Override
	public void update(double delta) {
		time += delta;

		parts.update(delta);

		Vector2d camPos = getCar().getPosition().add(new Vector2d(Math.sin(-viewport.getRotation()), -Math.cos(-viewport.getRotation())).multiply(7));
		viewport.setCameraPosition(camPos);
	}

	@Override
	public int getSkyColor() {
		return 0xf0f0f0;
	}

	@Override
	public int getCeilingColor(double x, double y) {
		return ((IGameState) parts.getCurrentState()).getCeilingColor(x, y);
	}

	public int getGrassFloorColor(double x, double y) {
		return 0x6ABE30;
	}

	public int getBaseFloorColor(double x, double y) {
		double stripeLength = 5.0;

		x = Math.abs(x);

		if (x <= 4) {
			boolean isStripe = (x < 0.4) && ((int) Math.floor(y / stripeLength) & 1) == 1;
			return isStripe ? 0xffffff : 0x696A6A;
		} else {
			return getGrassFloorColor(x, y);
		}
	}

	@Override
	public int getFloorColor(double x, double y) {
		return ((IGameState) parts.getCurrentState()).getFloorColor(x, y);
	}

	public Viewport getViewport() {
		return viewport;
	}

	public Vector2d getCarPos() {
		return getCar().getPosition();
	}

	public void setCarPos(Vector2d carPos) {
		getCar().setPosition(carPos);
	}

	public PlayerCar getCar() {
		return car;
	}

	public void transitionIntoNextState() {
		getCar().getPosition().setY(0);
		getViewport().setRotation(0);
		if (parts.getCurrentState() == obstaclesState) {
			if (level.getNextPoints() == null) {
				throw new Error("Win!");
			} else {
				Way[] targets = level.getPoint().getTargets();
				decisionState.setLeft(targets[0]);
				decisionState.setAhead(targets[1]);
				decisionState.setRight(targets[2]);
				parts.setState(decisionState);
			}
		} else {
			Way chosen = decisionState.getChosen();
			level.jump(chosen);
			obstaclesState.preload(chosen);
			parts.setState(obstaclesState);
		}
	}

	public Keyboard getKeyboard() {
		return keyboard;
	}
}
