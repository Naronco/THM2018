package com.naronco.thm2018.state.game;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.Sprite3D;
import com.naronco.thm2018.Sprites;
import com.naronco.thm2018.graphics.Viewport;
import com.naronco.thm2018.state.GameState;

import java.awt.event.KeyEvent;

public class DecisionGameState implements IGameState {
	private double crossingX = 0;
	private double crossingY = 80;

	private boolean hasLeft, hasAhead, hasRight;

	private static final double TURN_TIME = 1.5;

	private double time = 0;

	private boolean animatingTurn = false;
	private boolean animatingStraight = false;
	private double animatingTime = 0;
	private boolean animateLeft;
	private boolean choosing;

	private Sprite3D trafficLight;

	private GameState game;

	public DecisionGameState(GameState game) {
		this.game = game;
		trafficLight = new Sprite3D(new Vector2d(crossingX + 5, crossingY - 6), Sprites.trafficLight, 0, 20, -26, 0);
	}

	@Override
	public void load() {
		choosing = true;
		animatingTurn = false;
		animatingStraight = false;
		animatingTime = 0;
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
		game.getCar().setLeft(animateLeft);

		viewport.renderSprite3D(screen, trafficLight);
	}

	@Override
	public void update(double delta) {
		time += delta;

		if (game.getCarPos().getY() < crossingY - 9) {
			game.getCar().drive(delta);
			game.getCar().fadeX(0, 0.5, delta);
		} else if (choosing) {
			if (game.getKeyboard().isPressed(KeyEvent.VK_LEFT) && hasLeft) {
				animateLeft = true;
				choosing = false;
			} else if (game.getKeyboard().isPressed(KeyEvent.VK_UP) && hasAhead) {
				animatingStraight = true;
				choosing = false;
			} else if (game.getKeyboard().isPressed(KeyEvent.VK_RIGHT) && hasRight) {
				animateLeft = false;
				choosing = false;
			}
		} else {
			if (!animatingStraight && !animatingTurn) {
				if (animatingTime > 0) {
					animatingTime = 0;
				} else {
					animatingTurn = true;
					animatingTime = 0;
				}
			}
		}

		if (animatingTurn) {
			animatingTime += delta;
			double p = animatingTime / TURN_TIME;
			if (p >= 1.0) {
				p = 1.0;
			}

			double angle = p * Math.PI * 0.5;

			double dist = 9 - Math.sin(p * Math.PI) * 5;

			game.getCarPos().setX((-dist + Math.cos(angle) * dist) * (animateLeft ? 1 : -1));
			game.getCarPos().setY(crossingY - dist + Math.sin(angle) * dist);

			game.getViewport().setRotation(animateLeft ? -angle : angle);

			if (p >= 1.0) {
				game.transitionIntoNextState();
			}
		} else if (animatingStraight) {
			double p = animatingTime / TURN_TIME;
			if (p >= 1.0) {
				p = 1.0;
			}
			animatingTime += delta;
			game.getCar().drive(delta * p);

			if (p >= 1.0) {
				game.transitionIntoNextState();
			}
		}
	}

	@Override
	public int getFloorColor(double x, double y) {
		double dx = x - crossingX;
		double dy = y - crossingY;

		double adx = Math.abs(dx);
		double ady = Math.abs(dy);

		double stripeLength = 5.0;

		boolean isStreet = (dy < 4 && adx <= 4) || (adx <= 4 && hasAhead) || (ady <= 4 && dy > 0 && hasLeft) || (ady <= 4 && dy < 0 && hasRight);
		if (!isStreet) {
			return game.getGrassFloorColor(x, y);
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

	public boolean isHasLeft() {
		return hasLeft;
	}

	public void setHasLeft(boolean hasLeft) {
		this.hasLeft = hasLeft;
	}

	public boolean isHasAhead() {
		return hasAhead;
	}

	public void setHasAhead(boolean hasAhead) {
		this.hasAhead = hasAhead;
	}

	public boolean isHasRight() {
		return hasRight;
	}

	public void setHasRight(boolean hasRight) {
		this.hasRight = hasRight;
	}
}
