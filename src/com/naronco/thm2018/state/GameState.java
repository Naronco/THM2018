package com.naronco.thm2018.state;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.input.Keyboard;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.deviotion.ld.eggine.math.Dimension2i;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.Game;
import com.naronco.thm2018.Options;
import com.naronco.thm2018.Sprites;
import com.naronco.thm2018.graphics.IViewportDataSource;
import com.naronco.thm2018.graphics.SimplexNoise;
import com.naronco.thm2018.graphics.Viewport;
import com.naronco.thm2018.maze.Level;
import com.naronco.thm2018.maze.MazeGenerator;
import com.naronco.thm2018.maze.Way;
import com.naronco.thm2018.state.game.DecisionGameState;
import com.naronco.thm2018.state.game.IGameState;
import com.naronco.thm2018.state.game.ObstaclesGameState;
import com.naronco.thm2018.state.game.PlayerCar;
import net.arikia.dev.drpc.DiscordRPC;
import net.arikia.dev.drpc.DiscordRichPresence;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameState implements IState, IViewportDataSource, KeyListener {
	private StateManager parts = new StateManager();
	private PlayerCar car = new PlayerCar();
	private Viewport viewport;
	private Game game;

	private DecisionGameState decisionState;
	private ObstaclesGameState obstaclesState;

	private boolean discordReady = true;

	private Level level;
	private int turns;
	private int badTurns;

	double time = 0;

	public GameState(Dimension2i size, Game game) {
		this.viewport = new Viewport(size);
		this.game = game;
		viewport.setDataSource(this);
	}

	@Override
	public void load() {
		decisionState = new DecisionGameState(this);
		obstaclesState = new ObstaclesGameState(this);

		car.reset();

		int diffBase = Options.difficulty + 1;
		int numPoints = diffBase * 5;
		int numIntersections = (int) Math.pow(diffBase * 3, 2);
		Random random = new Random();
		level = new MazeGenerator(random).generate(numPoints, numIntersections);
		obstaclesState.preload(new Way(level.getPoint(), 0, "Ausfahrt"), 0);

		obstaclesState.setInit(true);
		parts.setState(obstaclesState);

		badTurns = 0;
		turns = 0;

		if (discordReady) {
			DiscordRichPresence rich = new DiscordRichPresence.Builder("Cruising the Streets")
					.setDetails("Entering the Highways")
					.setBigImage("cruising", "Avoiding obstacles")
					.build();
			DiscordRPC.discordUpdatePresence(rich);
		}

		game.getKeyboard().setActiveListener(this);
	}

	@Override
	public void unload() {
		if (discordReady) {
			DiscordRPC.discordClearPresence();
		}

		game.getKeyboard().setActiveListener(null);
	}

	@Override
	public void render(Screen screen) {
		viewport.render(screen);

		parts.render(screen);

		int vibration = (int) Math.round(Math.sin(time * 5) * Math.cos(time * 3 + 10) * 0.5 + 0.5);
		if (getCar().isHit() && getCar().getHitTimer() > PlayerCar.HIT_SPEEDUP_START && ((int) (getCar().getHitTimer() * 10) & 1) == 0) {
			// invulnerable invisible
		} else {
			if (getCar().isDrifting())
				viewport.renderSprite3D(screen, getCarPos(), getCar().isLeft() ? 0 : (94 + 60), 0, 94, 61, Sprites.car, 0, -vibration);
			else
				viewport.renderSprite3D(screen, getCarPos(), 94, 0, 60, 61, Sprites.car, 0, -vibration);
		}

		viewport.postProcess(screen);

		((IGameState) parts.getCurrentState()).render2D(screen);
	}

	@Override
	public void update(double delta) {
		time += delta;

		parts.update(delta);

		Vector2d camPos = getCar().getPosition().add(new Vector2d(Math.sin(-viewport.getRotation()), -Math.cos(-viewport.getRotation())).multiply(7));
		viewport.setCameraPosition(camPos);

		if (getCar().getHealth() <= 0) {
			game.openFailScene();
		}
		if (getCar().isHit() && getCar().getHitTimer() == 0) {
			game.playCrash();
		}
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
		double n = SimplexNoise.noise(x * 0.001 + 156.45, y * 0.003 + 984.54);
		if (n > 1)
			n = 1;
		else if (n < 0)
			n = 0;
		return fade(0x6ABE30, 0x30801A, n);
	}

	private int fade(int a, int b, double n) {
		int ar = (a >> 16) & 0xFF;
		int ag = (a >> 8) & 0xFF;
		int ab = (a) & 0xFF;

		int br = (b >> 16) & 0xFF;
		int bg = (b >> 8) & 0xFF;
		int bb = (b) & 0xFF;

		int cr = (int) (ar * (1 - n) + br * n);
		int cg = (int) (ag * (1 - n) + bg * n);
		int cb = (int) (ab * (1 - n) + bb * n);

		return (cr << 16) | (cg << 8) | cb;
	}

	public int getBaseFloorColor(double x, double y) {
		double stripeLength = 5.0;

		x = Math.abs(x);

		if (x <= 6) {
			boolean isStripe = (x < 0.4) && ((int) Math.floor(y / stripeLength) & 1) == 1;
			return isStripe ? 0xffffff : getRoadColorByWidth(x);
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
				if (discordReady) {
					DiscordRPC.discordClearPresence();
				}
				game.openWinScene(badTurns);
			} else {
				Way[] targets = level.getPoint().getTargets();
				decisionState.setLeft(targets[0]);
				decisionState.setAhead(targets[1]);
				decisionState.setRight(targets[2]);
				parts.setState(decisionState);

				if (discordReady) {
					DiscordRichPresence.Builder rich = new DiscordRichPresence.Builder("Some Intersection")
							.setDetails("Lost on the Streets")
							.setBigImage("cruising", "Avoiding obstacles");
					if (targets[0] != null && targets[1] != null && targets[2] != null) {
						rich.setBigImage("choosing3", "3 Way Intersection");
					} else {
						rich.setBigImage("choosing", "2 Way Intersection");
					}
					DiscordRPC.discordUpdatePresence(rich.build());
				}
			}
		} else {
			int oldIndex = level.getIndex();
			Way chosen = decisionState.getChosen();
			int newIndex = level.jump(chosen);

			if (newIndex <= oldIndex)
				badTurns++;
			turns++;

			obstaclesState.preload(chosen, turns);
			parts.setState(obstaclesState);

			if (discordReady) {
				DiscordRichPresence rich = new DiscordRichPresence.Builder("Cruising the Streets")
						.setDetails(chosen.getName())
						.setBigImage("cruising", "Avoiding obstacles")
						.build();
				DiscordRPC.discordUpdatePresence(rich);
			}
		}
	}

	public Keyboard getKeyboard() {
		return game.getKeyboard();
	}

	public void setDiscordReady(boolean discordReady) {
		this.discordReady = discordReady;
	}

	@Override
	public void keyTyped(KeyEvent keyEvent) {
	}

	@Override
	public void keyPressed(KeyEvent keyEvent) {
		if (keyEvent.getKeyCode() == KeyEvent.VK_ESCAPE) {
			game.openMainMenu();
		}
	}

	@Override
	public void keyReleased(KeyEvent keyEvent) {

	}

	public static int getRoadColorByWidth(double x) {
		int ret;
		if (x > 4)
			ret = 0x5a4242;
		else if (x > 3.8)
			ret = 0xFFFFFF;
		else
			ret = 0x696A6A;

		int r = (ret >> 16) & 0xFF;
		int g = (ret >> 8) & 0xFF;
		int b = (ret) & 0xFF;

		double lightness = 1 - x / 6.0;
		if (x > 4)
			lightness += 0.5;
		else if (x > 3.8)
			lightness += 0.2;
		if (lightness < 1) {
			r = (int) (r * lightness);
			g = (int) (g * lightness);
			b = (int) (b * lightness);
		}

		return (r << 16) | (g << 8) | b;
	}

	public static int getRoadColorByWidth(double a1, double a2) {
		int ret;
		if (a1 > 4 && a2 > 4)
			ret = 0x5a4242;
		else if (a1 > 3.8 && a2 > 3.8)
			ret = 0xFFFFFF;
		else
			ret = 0x696A6A;

		int r = (ret >> 16) & 0xFF;
		int g = (ret >> 8) & 0xFF;
		int b = (ret) & 0xFF;

		double lightness = Math.max(1 - a1 / 6.0, 1 - a2 / 6.0);
		if (a1 > 4 && a2 > 4)
			lightness += 0.5;
		else if (a1 > 3.8 && a2 > 3.8)
			lightness += 0.2;
		if (lightness <= 0)
			return 0;
		else if (lightness < 1) {
			r = (int) (r * lightness);
			g = (int) (g * lightness);
			b = (int) (b * lightness);
		}

		return (r << 16) | (g << 8) | b;
	}
}
