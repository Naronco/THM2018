package com.naronco.thm2018.state.game;

import java.awt.event.KeyEvent;
import java.io.File;

import com.deviotion.ld.eggine.graphics.Font;
import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Sprite;
import com.deviotion.ld.eggine.graphics.SpriteSheet;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.Sprite3D;
import com.naronco.thm2018.Sprites;
import com.naronco.thm2018.graphics.Viewport;
import com.naronco.thm2018.maze.Point;
import com.naronco.thm2018.maze.Way;
import com.naronco.thm2018.state.GameState;

public class DecisionGameState implements IGameState {
	private double crossingX = 0;
	private double crossingY = 60;
	private SpriteSheet arrows;

	private double getConstantEndMarker() {
		return crossingY - 19;
	}

	private double getHaltMarker() {
		return crossingY - 9;
	}

	private Way left, ahead, right;
	private Way chosen;

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
		arrows = new SpriteSheet(new Sprite(new File("res/arrows.png"), 0xFFFF00FF), new Dimension2d(29, 29));
	}

	@Override
	public void load() {
		choosing = true;
		animatingTurn = false;
		animatingStraight = false;
		chosen = null;
		animatingTime = 0;
	}

	@Override
	public void unload() {
		game.getCar().setDrifting(false);
		game.getCar().setSpeed(70);
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

		boolean moveForward = game.getCarPos().getY() < getHaltMarker();

		double speed;
		if (game.getCarPos().getY() < getConstantEndMarker())
			speed = 70;
		else if (game.getCarPos().getY() < getHaltMarker()) // 40-50 (constantEndMarker - haltMarker)
			speed = 70 - (game.getCarPos().getY() - getConstantEndMarker()) / (getHaltMarker() - getConstantEndMarker()) * 65;
		else
			speed = 0;
		game.getCar().setSpeed(speed);
		game.getCar().drive(delta);

		if (moveForward) {
			game.getCar().fadeX(0, 0.5, delta);
		} else if (choosing) {
			if (game.getKeyboard().isPressed(KeyEvent.VK_LEFT) && left != null) {
				animateLeft = true;
				choosing = false;
				chosen = left;
			} else if (game.getKeyboard().isPressed(KeyEvent.VK_UP) && ahead != null) {
				animatingStraight = true;
				choosing = false;
				chosen = ahead;
			} else if (game.getKeyboard().isPressed(KeyEvent.VK_RIGHT) && right != null) {
				animateLeft = false;
				choosing = false;
				chosen = right;
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

			game.getCar().setSpeed((1 - (1 - p) * (1 - p)) * 70);
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
			game.getCar().setSpeed(p * 70);
			game.getCar().drive(delta);

			if (p >= 1.0) {
				game.transitionIntoNextState();
			}
		}
	}

	@Override
	public int getCeilingColor(double x, double y) {
		return -1;
	}

	@Override
	public void render2D(Screen screen) {
		if (game.getCarPos().getY() > getConstantEndMarker() && choosing) {
			// arrows

			if (left != null) {
				screen.renderSpriteTile(8, (int) (screen.getDimension().getHeight() - arrows.getSpriteSize().getHeight()) - 34, arrows, 0);
			}
			if (ahead != null) {
				screen.renderSpriteTile((int) (screen.getDimension().getWidth() - arrows.getSpriteSize().getWidth()) / 2, 18, arrows, 1);
			}
			if (right != null) {
				screen.renderSpriteTile((int) (screen.getDimension().getWidth() - arrows.getSpriteSize().getWidth()) - 8, (int) (screen.getDimension().getHeight() - arrows.getSpriteSize().getHeight()) - 34, arrows, 2);
			}

			// text

			if (left != null) {
				renderTextOutlined(screen, 4, (int) screen.getDimension().getHeight() - 12, left.getName());
			}
			if (ahead != null) {
				renderTextOutlined(screen, (int) (screen.getDimension().getWidth() - Font.standard.sizeOfText(ahead.getName()).getWidth()) / 2, 7, ahead.getName());
			}
			if (right != null) {
				renderTextOutlined(screen, (int) (screen.getDimension().getWidth() - Font.standard.sizeOfText(right.getName()).getWidth()) - 4, (int) screen.getDimension().getHeight() - 24, right.getName());
			}
		}
	}

	private void renderTextOutlined(Screen screen, int x, int y, String text) {
		screen.renderText(x - 1, y, Font.black, text);
		screen.renderText(x + 1, y, Font.black, text);
		screen.renderText(x, y - 1, Font.black, text);
		screen.renderText(x, y + 1, Font.black, text);
		screen.renderText(x, y, Font.standard, text);
	}

	@Override
	public int getFloorColor(double x, double y) {
		double dx = x - crossingX;
		double dy = y - crossingY;

		double adx = Math.abs(dx);
		double ady = Math.abs(dy);

		double stripeLength = 5.0;

		boolean isStreet = (dy < 6 && adx <= 6) || (adx <= 6 && ahead != null) || (ady <= 6 && dx > 0 && left != null) || (ady <= 6 && dx < 0 && right != null);
		if (!isStreet) {
			return game.getGrassFloorColor(x, y);
		}

		boolean isCrossing = adx <= 6 && ady <= 6;
		if (isCrossing) {
			if (right == null && dx < 0)
				return GameState.getRoadColorByWidth(adx);
			if (left == null && dx > 0)
				return GameState.getRoadColorByWidth(adx);
			if (ahead == null && dy > 0)
				return GameState.getRoadColorByWidth(ady);
			return GameState.getRoadColorByWidth(adx, ady);
		}

		boolean isVerticalRoad = adx <= 6;
		if (isVerticalRoad) {
			boolean isStripe = (adx < 0.4) && ((int) Math.floor(ady / stripeLength) & 1) == 1;
			return isStripe ? 0xffffff : GameState.getRoadColorByWidth(adx);
		} else {
			boolean isStripe = (ady < 0.4) && ((int) Math.floor(adx / stripeLength) & 1) == 1;
			return isStripe ? 0xffffff : GameState.getRoadColorByWidth(ady);
		}
	}

	public Way getLeft() {
		return left;
	}

	public void setLeft(Way left) {
		this.left = left;
	}

	public Way getAhead() {
		return ahead;
	}

	public void setAhead(Way ahead) {
		this.ahead = ahead;
	}

	public Way getRight() {
		return right;
	}

	public void setRight(Way right) {
		this.right = right;
	}

	public Way getChosen() {
		return chosen;
	}
}
