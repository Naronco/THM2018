package com.naronco.thm2018.alley;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.graphics.Viewport;

public class House extends AlleyObject {
	private Vector2d start, end;
	
	public House(Vector2d start, Vector2d end) {
		this.start = start;
		this.end = end;
	}
	
	public void render(Screen screen, Viewport viewport) {
		viewport.renderWall(screen, new Vector2d(start.getX(), start.getY()), new Vector2d(start.getX(), end.getY()), 0xEEC39A);
		viewport.renderWall(screen, new Vector2d(start.getX(), end.getY()), new Vector2d(end.getX(), end.getY()), 0xDF7126);
		viewport.renderWall(screen, new Vector2d(end.getX(), end.getY()), new Vector2d(end.getX(), start.getY()), 0xEEC39A);
		viewport.renderWall(screen, new Vector2d(end.getX(), start.getY()), new Vector2d(start.getX(), start.getY()), 0xDF7126);
	}
}
