package com.naronco.thm2018.alley;

import java.util.ArrayList;
import java.util.List;

import com.deviotion.ld.eggine.graphics.Screen;
import com.naronco.thm2018.graphics.Viewport;

public class Alley {
	private List<AlleyObject> objects = new ArrayList<>();
	
	public void render(Screen screen, Viewport viewport) {
		for (AlleyObject object : objects) {
			object.render(screen, viewport);
		}
	}
	
	public void addObject(AlleyObject object) {
		objects.add(object);
	}
	
	public int getCeilingColor(double x, double y) {
		for (AlleyObject object : objects) {
			int color = object.getCeilingColor(x, y);
			if (color != -1) {
				return color;
			}
		}
		return -1;
	}
}
