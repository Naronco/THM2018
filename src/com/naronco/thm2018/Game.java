package com.naronco.thm2018;

import com.deviotion.ld.eggine.Eggine;
import com.deviotion.ld.eggine.graphics.*;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.naronco.thm2018.graphics.IViewportDataSource;
import com.naronco.thm2018.graphics.Viewport;

import java.awt.event.KeyEvent;

public class Game extends Eggine implements IViewportDataSource {
	private Viewport viewport;

	public Game() {
		super(60, 60, new Window("InfinityJam", new Dimension2d(160, 120), 4));

		this.viewport = new Viewport();
		viewport.setDataSource(this);
	}

	double time = 0;
	double rot = 0;

	@Override
	public void render(Screen screen) {
		screen.fillScreen(0xF2F2F2);

		viewport.getCameraPosition().setY(time * 50 / 3.6);

		viewport.render(screen);
		viewport.renderSprite3D(screen, 0, 7, 94, 0, 60, 61, Sprites.car);
	}

	@Override
	public void update(double delta) {
		time += delta;
		rot = 0;
		if (getKeyboard().isPressed(KeyEvent.VK_RIGHT)) {
		}
	}

	@Override
	public int getSkyColor() {
		return 0x0080ff;
	}

	@Override
	public int getFloorColor(double x, double y) {
		double crossingX = 0;
		double crossingY = -64;

		double dx = x - crossingX;
		double dy = y - crossingY;

		double stripeLength = 5.0;
		boolean isStreet = Math.abs(dx) <= 4;
		boolean isStripeVertical = ((int)Math.floor(y / stripeLength) & 1) == 0;
		boolean isStripe = (Math.abs(x) < 0.15) && isStripeVertical;
		if (isStripe) {
			return 0xffffff;
		} else if (isStreet) {
			if (isStripeVertical) {
				return 0x696A6A;
			} else {
				return 0x595652;
			}
		} else {
			return 0x00ff00;
		}
	}
}
