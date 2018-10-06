package com.naronco.thm2018.graphics;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Sprite;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.deviotion.ld.eggine.math.Vector2d;
import com.naronco.thm2018.Dither;
import com.naronco.thm2018.Sprite3D;

public class Viewport {
	private Dimension2d size;

	private IViewportDataSource dataSource;

	private Vector2d cameraPosition;
	private double rotation;

	private double[] zBuffer;

	private static final double HORIZON_OFFSET = 6.0;
	private static final double CAMERA_HEIGHT = 5;

	public Viewport(Dimension2d size) {
		this.size = size;
		this.cameraPosition = new Vector2d(0, 0);

		this.zBuffer = new double[(int) size.getWidth() * (int) size.getHeight()];
	}

	public static Vector2d projectWorldToViewport(double width, double height, Vector2d input, Vector2d cameraPosition, double rotation) {
		double x0 = width * 0.5;
		double y0 = height * 1.0 / HORIZON_OFFSET;

		double sin = Math.sin(rotation);
		double cos = Math.cos(rotation);
		
		input = input.subtract(cameraPosition);
		
		double xs = input.getX() * cos + input.getY() * -sin;
		double ys = input.getX() * sin + input.getY() * cos;

		double theta = CAMERA_HEIGHT / ys;
		double phi = xs / ys;

		double xp = phi * width + x0;
		double yp = theta * height + y0;

		return new Vector2d(xp, yp);
	}

	public Vector2d projectWorldToViewport(Screen screen, double x, double y) {
		return projectWorldToViewport(screen.getDimension().getWidth(), screen.getDimension().getHeight(), new Vector2d(x, y), cameraPosition, rotation);
	}

	public void renderSpriteLOD(Screen screen, Vector2d position, double scale, int size, Sprite sprite, int offsetX, int offsetY) {
		renderSpriteLOD(screen, position.getX(), position.getY(), scale, size, sprite, offsetX, offsetY);
	}

	public void renderSpriteLOD(Screen screen, double x, double y, double scale, int size, Sprite sprite, int offsetX, int offsetY) {
		if (y > 2.5 * scale) {
			double y1 = projectWorldToViewport(screen, x, y - 2.5 * scale).getY();
			double y2 = projectWorldToViewport(screen, x, y + 2.5 * scale).getY();

			int factor = (int) Math.pow(2, (int) Math.ceil(Math.log(size / (y1 - y2)) / Math.log(2)));
			if (factor > 1) {
				size /= factor;
				offsetX /= factor;
				offsetY /= factor;
			}
		}

		renderSprite3D(screen, x, y, size, 0, size, size, sprite, offsetX, offsetY);
	}

	public void renderSprite3D(Screen screen, Vector2d position, int startX, int startY, int width, int height, Sprite sprite) {
		renderSprite3D(screen, position.getX(), position.getY(), startX, startY, width, height, sprite);
	}

	public void renderSprite3D(Screen screen, double x, double y, int startX, int startY, int width, int height, Sprite sprite) {
		renderSprite3D(screen, x, y, startX, startY, width, height, sprite, 0, 0);
	}

	public void renderSprite3D(Screen screen, Vector2d position, int startX, int startY, int width, int height, Sprite sprite, int offsetX, int offsetY) {
		renderSprite3D(screen, position.getX(), position.getY(), startX, startY, width, height, sprite, offsetX, offsetY);
	}

	public void renderSprite3D(Screen screen, Sprite3D sprite) {
		if (sprite.isLod()) {
			renderSpriteLOD(screen, sprite.getPosition(), sprite.getLodScale(), sprite.getHeight(), sprite.getBase(), sprite.getOffsetX(), sprite.getOffsetY());
		} else {
			renderSprite3D(screen, sprite.getPosition(), sprite.getTextureX(), sprite.getTextureY(), sprite.getWidth(), sprite.getHeight(), sprite.getBase(), sprite.getOffsetX(), sprite.getOffsetY());
		}
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
			double z = CAMERA_HEIGHT / (theta - 0.01);

			for (int xp = 0; xp < screen.getDimension().getWidth(); ++xp) {
				double phi = (xp - x0) / screen.getDimension().getWidth();
				double x = phi * z;

				double xs = x * cos + z * sin - cameraPosition.getX();
				double zs = x * -sin + z * cos - cameraPosition.getY();

				int color;
				if (yp - y0 < 0) {
					color = dataSource.getSkyColor();
				} else {
					color = dataSource.getFloorColor(xs, -zs);
				}
				screen.setPixel(xp, yp, Dither.lookupColor(xp, yp, color));
				zBuffer[xp + yp * (int) size.getWidth()] = Math.sqrt(z * z + x * x);
			}
		}
	}

	private int[] dither = {
			14, 2, 16, 4,
			0, 12, 2, 13,
			16, 0, 13, 2,
			3, 12, 6, 16
	};

	public void postProcess(Screen screen) {
		double y0 = size.getHeight() / HORIZON_OFFSET;
		for (int yp = 0; yp < size.getHeight(); ++yp) {
			for (int xp = 0; xp < size.getWidth(); ++xp) {
				if (yp < y0) continue;
				double z = zBuffer[xp + yp * (int)size.getWidth()];
				if (dither[(xp & 3) + ((yp & 3) << 2)] < (z * 0.7 - 22)) {
					int color = screen.getPixel(xp, yp);
					screen.setPixel(xp, yp, 0xfffff);
				}
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
