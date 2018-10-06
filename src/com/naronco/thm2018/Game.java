package com.naronco.thm2018;

import com.deviotion.ld.eggine.Eggine;
import com.deviotion.ld.eggine.graphics.*;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.graphics.IViewportDataSource;
import com.naronco.thm2018.graphics.Viewport;

import java.awt.event.KeyEvent;

public class Game extends Eggine implements IViewportDataSource {
	private Viewport viewport;

	private Vector2d carPos = new Vector2d(0, 0);

	private double crossingX = 0;
	private double crossingY = 64;

	private static final double TURN_TIME = 1.5;

	private boolean animatingTurn = false;
	private double animatingTurnTime = 0;

	public Game() {
		super(60, 60, new Window("InfinityJam", new Dimension2d(160, 120), 4));

		this.viewport = new Viewport(getWindow().getDimension());
		viewport.setDataSource(this);
	}

	double time = 0;

	@Override
	public void render(Screen screen) {
		screen.fillScreen(0xF2F2F2);

		viewport.render(screen);
		viewport.renderSpriteLOD(screen, 3, 50 - viewport.getCameraPosition().getY(), 1, 32, 32, Sprites.gulli, 0, 12);
		int vibration = (int) Math.round(Math.sin(time * 5) * Math.cos(time * 3 + 10) * 0.5 + 0.5);
		if (animatingTurn)
			viewport.renderSprite3D(screen, carPos.getX() - viewport.getCameraPosition().getX(), carPos.getY() - viewport.getCameraPosition().getY(), 0, 0, 94, 61, Sprites.car, 0, -vibration);
		else
			viewport.renderSprite3D(screen, carPos.getX() - viewport.getCameraPosition().getX(), carPos.getY() - viewport.getCameraPosition().getY(), 94, 0, 60, 61, Sprites.car, 0, -vibration);
		viewport.postProcess(screen);
	}

	@Override
	public void update(double delta) {
		time += delta;
		if (carPos.getY() < crossingY - 9) {
			carPos.setY(carPos.getY() + delta * 50 / 3.6);
		} else {
			if (!animatingTurn) {
				if (animatingTurnTime > 0) {
					carPos.setX(0);
					carPos.setY(-100);
					viewport.setRotation(0);
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

			carPos.setX(-dist + Math.cos(angle) * dist);
			carPos.setY(crossingY - dist + Math.sin(angle) * dist);

			viewport.setRotation(-angle);
		}

		Vector2d camPos = carPos.add(new Vector2d(Math.sin(-viewport.getRotation()), -Math.cos(-viewport.getRotation())).multiply(7));
		viewport.setCameraPosition(camPos);
	}

	@Override
	public int getSkyColor() {
		return 0x0080ff;
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
			return 0x00ff00;
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
