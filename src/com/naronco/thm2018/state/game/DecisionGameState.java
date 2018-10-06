package com.naronco.thm2018.state.game;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.Sprites;
import com.naronco.thm2018.graphics.Viewport;
import com.naronco.thm2018.state.GameState;

public class DecisionGameState implements IGameState {
	private double crossingX = 0;
	private double crossingY = 80;

	private static final double TURN_TIME = 1.5;

	double time = 0;

	private boolean animatingTurn = false;
	private double animatingTurnTime = 0;

	private GameState game;

	public DecisionGameState(GameState game) {
		this.game = game;
	}

	@Override
	public void load() {
	}

	@Override
	public void unload() {
		game.getCar().setDrifting(false);
	}

	@Override
	public void render(Screen screen) {
		final Viewport viewport = game.getViewport();
		final Vector2d carPos = game.getCarPos();

		game.getCar().setDrifting(animatingTurn);
		game.getCar().setLeft(true);
	}

	@Override
	public void update(double delta) {
		time += delta;

		if (game.getCarPos().getY() < crossingY - 9) {
			game.getCar().drive(delta);
		} else {
			if (!animatingTurn) {
				if (animatingTurnTime > 0) {
					game.transitionIntoNextState();
					animatingTurnTime = 0;
				} else {
					animatingTurn = true;
					animatingTurnTime = 0;
				}
			}
		}

		if (animatingTurn) {
			animatingTurnTime += delta;
			double p = animatingTurnTime / TURN_TIME;
			if (p >= 1.0) {
				p = 1.0;
				animatingTurn = false;
			}

			double angle = p * Math.PI * 0.5;

			double dist = 9 - Math.sin(p * Math.PI) * 5;

			game.getCarPos().setX(-dist + Math.cos(angle) * dist);
			game.getCarPos().setY(crossingY - dist + Math.sin(angle) * dist);

			game.getViewport().setRotation(-angle);
		} else {
			game.getCar().fadeX(0, 0.5, delta);
		}
	}

	@Override
	public int getFloorColor(double x, double y) {
		double dx = x - crossingX;
		double dy = y - crossingY;

		double adx = Math.abs(dx);
		double ady = Math.abs(dy);

		double stripeLength = 5.0;

		boolean isStreet = adx <= 4 || ady <= 4;
		if (!isStreet) {
			return game.getBaseFloorColor(x, y);
		}

		boolean isCrossing = adx <= 4 && ady <= 4;
		if (isCrossing) {
			return 0x696A6A;
		}

		boolean isVerticalRoad = adx <= 4;
		if (isVerticalRoad) {
			boolean isStripe = (adx < 0.4) && ((int) Math.floor(ady / stripeLength) & 1) == 1;
			return isStripe ? 0xffffff : 0x696A6A;
		} else {
			boolean isStripe = (ady < 0.4) && ((int) Math.floor(adx / stripeLength) & 1) == 1;
			return isStripe ? 0xffffff : 0x696A6A;
		}
	}
}
