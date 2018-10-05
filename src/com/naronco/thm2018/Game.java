package com.naronco.thm2018;

import com.deviotion.ld.eggine.Eggine;
import com.deviotion.ld.eggine.graphics.*;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.naronco.thm2018.graphics.IViewportDataSource;
import com.naronco.thm2018.graphics.Viewport;

import java.awt.event.KeyEvent;

public class Game extends Eggine implements IViewportDataSource {
	private Viewport viewport;

	private double crossingX = 0;
	private double crossingY = 64;

	public Game() {
		super(60, 60, new Window("InfinityJam", new Dimension2d(160, 120), 4));

		this.viewport = new Viewport();
		viewport.setDataSource(this);
	}

	double time = 0;

	@Override
	public void render(Screen screen) {
		screen.fillScreen(0xF2F2F2);

		viewport.render(screen);
		viewport.renderSprite3D(screen, 0, 7, 94, 0, 60, 61, Sprites.car);
	}

	@Override
	public void update(double delta) {
		time += delta;
		double y = viewport.getCameraPosition().getY();
		if (y < crossingY - 16) {
			viewport.getCameraPosition().setY(y + delta * 50 / 3.6);
		}
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
			boolean isStripe = (adx < 0.4) && ((int)Math.floor(ady / stripeLength) & 1) == 1;
			return isStripe ? 0xffffff : 0x696A6A;
		} else {
			boolean isStripe = (ady < 0.4) && ((int)Math.floor(adx / stripeLength) & 1) == 1;
			return isStripe ? 0xffffff : 0x696A6A;
		}
	}
}
