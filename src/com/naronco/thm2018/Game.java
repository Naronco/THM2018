package com.naronco.thm2018;

import com.deviotion.ld.eggine.Eggine;
import com.deviotion.ld.eggine.graphics.*;
import com.deviotion.ld.eggine.math.Dimension2d;

public class Game extends Eggine {
	public static Game instance;

	public Game() {
		super(60, 30, new Window("InfinityJam", new Dimension2d(160, 120), 4));
	}

	@Override
	public void render(Screen screen) {
		screen.fillScreen(0xF2F2F2);

		for (int y = 0; y < screen.getDimension().getHeight(); ++y) {

			for (int x = 0; x < screen.getDimension().getWidth(); ++x) {
			}
		}
		screen.setPixel(10, 10, 0xff0000);
	}

	@Override
	public void update(double delta) {
	}
}
