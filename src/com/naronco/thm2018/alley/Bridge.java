package com.naronco.thm2018.alley;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.math.Vector2d;
import com.deviotion.ld.eggine.math.Vector3d;
import com.naronco.thm2018.graphics.Viewport;

public class Bridge extends AlleyObject {
	private Vector2d start, end;
	
	public Bridge(Vector2d start, Vector2d end) {
		this.start = start;
		this.end = end;
	}
	
	@Override
	public void render(Screen screen, Viewport viewport) {
		viewport.renderWall(screen, new Vector2d(start.getX() - 20, start.getY()), new Vector2d(start.getX(), start.getY()), 0xAAAAAA);
		viewport.renderWall(screen, new Vector2d(start.getX(), start.getY()), new Vector2d(start.getX(), end.getY()), 0x777777);
		viewport.renderWall(screen, new Vector2d(end.getX(), start.getY()), new Vector2d(end.getX() + 20, start.getY()), 0xAAAAAA);
		viewport.renderWall(screen, new Vector2d(end.getX(), end.getY()), new Vector2d(end.getX(), start.getY()), 0x777777);
		
		viewport.renderWall(screen, new Vector3d(start.getX() - 20, start.getY(), Viewport.CEILING_HEIGHT), new Vector3d(end.getX() + 20, start.getY(), Viewport.CEILING_HEIGHT + 3), 0xAAAAAA);
	}
	
	public int getCeilingColor(double x, double y) {
		if (x >= start.getX() && x < end.getX() && y >= start.getY() && y < end.getY()) {
			return 0x777777;
		} else {
			return -1;
		}
	}
}
