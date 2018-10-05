package com.naronco.thm2018;

import com.deviotion.ld.eggine.Eggine;
import com.deviotion.ld.eggine.graphics.*;
import com.deviotion.ld.eggine.math.Dimension2d;

public class Game extends Eggine {
	public static Game instance;

	public Game() {
		super(60, 60, new Window("InfinityJam", new Dimension2d(160, 120), 4));
	}

	double time=0;

	private int floorColor(double x, double y) {
		double stripeLength = 3.0;
		boolean isStripe = (Math.abs(x) < 0.15) && ((int)Math.floor(y / stripeLength) & 1) == 0;
		if (isStripe) {
			return 0xffffff;
		}
		if (Math.abs(x) > 4) {
			return 0x00ff00;
		} else {
			return 0x777777;
		}
	}

	@Override
	public void render(Screen screen) {
		screen.fillScreen(0xF2F2F2);

		double x0 = screen.getDimension().getWidth() * 0.5;
		double y0 = screen.getDimension().getHeight() * 1.0 / 3.0;

		double rot = time;

		double sin = Math.sin(rot);
		double cos = Math.cos(rot);

		double posZ = time * 10;

		int skyColor = 0x0080ff;
		int streetColor = 0x777777;

		for (int yp = 0; yp < screen.getDimension().getHeight(); ++yp) {
			double theta = -(yp - y0) / screen.getDimension().getHeight();
			double z = 4 / theta;

			for (int xp = 0; xp < screen.getDimension().getWidth(); ++xp) {
				double phi = (xp - x0) / screen.getDimension().getWidth();
				double x = phi * z;

				double xs = x * cos + z * sin;
				double zs = x * -sin + z * cos - posZ;

				int color;
				if (theta >= 0) {
					color = skyColor;
				} else {
					color = floorColor(xs, zs);
				}
				screen.setPixel(xp, yp, Dither.lookupColor(xp, yp, color));
			}
		}
	}

	@Override
	public void update(double delta) {
		time += delta;
	}
}
