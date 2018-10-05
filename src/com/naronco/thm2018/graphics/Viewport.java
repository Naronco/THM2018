package com.naronco.thm2018.graphics;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Sprite;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.Dither;

public class Viewport {
	private IViewportDataSource dataSource;

	private Vector2d cameraPosition;
	private double rotation;

	public Viewport() {
		this.cameraPosition = new Vector2d(0, 0);
	}

	public void renderSprite3D(Screen screen, double x, double y, int startX, int startY, int width, int height, Sprite sprite) {
		double x0 = screen.getDimension().getWidth() * 0.5;
		double y0 = screen.getDimension().getHeight() * 1.0 / 3.0;

		double sin = Math.sin(rotation);
		double cos = Math.cos(rotation);

		double xs = x * cos + y * -sin;
		double ys = x * sin + y * cos;

		double theta = 4 / ys;
		double phi = xs / ys;

		double xp = phi * screen.getDimension().getWidth() + x0;
		double yp = theta * screen.getDimension().getHeight() + y0;

		screen.renderSprite((int)(xp - width / 2), (int)(yp - height), startX, startY, width, height, sprite);
	}

	public void render(Screen screen) {
		double x0 = screen.getDimension().getWidth() * 0.5;
		double y0 = screen.getDimension().getHeight() * 1.0 / 3.0;

		double sin = Math.sin(rotation);
		double cos = Math.cos(rotation);

		for (int yp = 0; yp < screen.getDimension().getHeight(); ++yp) {
			double theta = -(yp - y0) / screen.getDimension().getHeight();
			double z = 4 / (theta - 0.01);

			for (int xp = 0; xp < screen.getDimension().getWidth(); ++xp) {
				double phi = (xp - x0) / screen.getDimension().getWidth();
				double x = phi * z;

				double xs = x * cos + z * sin - cameraPosition.getX();
				double zs = x * -sin + z * cos - cameraPosition.getY();

				int color;
				if (yp - y0 < 0) {
					color = dataSource.getSkyColor();
				} else {
					color = dataSource.getFloorColor(xs, zs);
				}
				screen.setPixel(xp, yp, Dither.lookupColor(xp, yp, color));
			}
		}
	}

	public IViewportDataSource getDataSource() {
		return dataSource;
	}

	public void setDataSource(IViewportDataSource dataSource) {
		this.dataSource = dataSource;
	}

	public Vector2d getCameraPosition() {
		return cameraPosition;
	}

	public void setCameraPosition(Vector2d cameraPosition) {
		this.cameraPosition = cameraPosition;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
}
