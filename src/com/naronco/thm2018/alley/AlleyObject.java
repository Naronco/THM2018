package com.naronco.thm2018.alley;

import com.deviotion.ld.eggine.graphics.Screen;
import com.naronco.thm2018.graphics.Viewport;

public abstract class AlleyObject {
	public abstract void render(Screen screen, Viewport viewport);
	
	public int getCeilingColor(double x, double y) {
		return -1;
	}
}
