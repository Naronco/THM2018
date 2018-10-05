package com.naronco.thm2018.graphics;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Sprite;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.Dither;

public class Viewport {
	private IViewportDataSource dataSource;

	private Vector2d cameraPosition;
	private double rotation;

	private static final double HORIZON_OFFSET = 10.0;
	private static final double CAMERA_HEIGHT = 6.5;

	public Viewport() {
		this.cameraPosition = new Vector2d(0, 0);
	}

	public static Vector2d projectWorldToViewport(double width, double height, Vector2d input, double rotation) {
		double x0 = width * 0.5;
		double y0 = height * 1.0 / HORIZON_OFFSET;

		double sin = Math.sin(rotation);
		double cos = Math.cos(rotation);

		double xs = input.getX() * cos + input.getY() * -sin;
		double ys = input.getX() * sin + input.getY() * cos;

		double theta = CAMERA_HEIGHT / ys;
		double phi = xs / ys;

		double xp = phi * width + x0;
		double yp = theta * height + y0;

		return new Vector2d(xp, yp);
	}

	public Vector2d projectWorldToViewport(Screen screen, double x, double y) {
		return projectWorldToViewport(screen.getDimension().getWidth(), screen.getDimension().getHeight(), new Vector2d(x, y), rotation);
	}

	public void renderSpriteLOD(Screen screen, double x, double y, double scale, int width, int height, Sprite sprite, int offsetX, int offsetY) {
		if (y > 2.5 * scale) {
			double y1 = projectWorldToViewport(screen, x, y - 2.5 * scale).getY();
			double y2 = projectWorldToViewport(screen, x, y + 2.5 * scale).getY();

			int factor = (int) Math.round(height / (y1 - y2));
			width /= factor;
			height /= factor;
			offsetX /= factor;
			offsetY /= factor;
		}

		renderSprite3D(screen, x, y, width, 0, width, height, sprite, offsetX, offsetY);
	}

	public void renderSprite3D(Screen screen, double x, double y, int startX, int startY, int width, int height, Sprite sprite) {
		renderSprite3D(screen, x, y, startX, startY, width, height, sprite, 0, 0);
	}

	public void renderSprite3D(Screen screen, double x, double y, int startX, int startY, int width, int height, Sprite sprite, int offsetX, int offsetY) {
		Vector2d projected = projectWorldToViewport(screen, x, y);

		screen.renderSprite((int) (projected.getX() - width / 2) + offsetX, (int) (projected.getY() - height) + offsetY, startX, startY, width, height, sprite);
	}

	public void render(Screen screen) {
		double x0 = screen.getDimension().getWidth() * 0.5;
		double y0 = screen.getDimension().getHeight() * 1.0 / HORIZON_OFFSET;

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
