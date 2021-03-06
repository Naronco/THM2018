package com.naronco.thm2018.graphics;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Sprite;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.deviotion.ld.eggine.math.Dimension2i;
import com.deviotion.ld.eggine.math.Vector2d;
import com.deviotion.ld.eggine.math.Vector3d;
import com.naronco.thm2018.Dither;
import com.naronco.thm2018.Sprite3D;

public class Viewport {
	private Dimension2i size;

	private IViewportDataSource dataSource;

	private Vector2d cameraPosition;
	private double rotation;

	private double[] zBuffer;

	private static final double HORIZON_OFFSET = 6.0;
	private static final double CAMERA_HEIGHT = 5;
	public static final double CEILING_HEIGHT = 3;

	public Viewport(Dimension2i size) {
		this.size = size;
		this.cameraPosition = new Vector2d(0, 0);

		this.zBuffer = new double[(int) size.getWidth() * (int) size.getHeight()];
	}

	public static Vector3d projectWorldToViewport(double width, double height, Vector2d input, Vector2d cameraPosition, double rotation) {
		double x0 = width * 0.5;
		double y0 = height * 1.0 / HORIZON_OFFSET;

		double sin = Math.sin(rotation);
		double cos = Math.cos(rotation);

		input = input.subtract(cameraPosition);

		double xs = input.getX() * cos + input.getY() * -sin;
		double ys = input.getX() * sin + input.getY() * cos;

		if (ys < 0.1) return null;
		double theta = CAMERA_HEIGHT / ys;
		double phi = xs / ys;

		double xp = phi * width + x0;
		double yp = theta * height + y0;

		return new Vector3d(xp, yp, ys);
	}

	public Vector3d projectWorldToViewport(Screen screen, double x, double y) {
		return projectWorldToViewport(screen.getDimension().getWidth(), screen.getDimension().getHeight(), new Vector2d(x, y), cameraPosition, rotation);
	}

	public void renderSpriteLOD(Screen screen, Vector2d position, double start, double scale, int size, Sprite sprite, int offsetX, int offsetY) {
		renderSpriteLOD(screen, position.getX(), position.getY(), start, scale, size, sprite, offsetX, offsetY);
	}

	public void renderSpriteLOD(Screen screen, double x, double y, double start, double scale, int size, Sprite sprite, int offsetX, int offsetY) {
		int factor;
		double ty = y - cameraPosition.getY() - start;
		if (ty < 1)
			factor = 1;
		else
			factor = (int) Math.pow(2, (int) (Math.log(ty / scale) / Math.log(2) + 1));

		if (factor > 1) {
			size /= factor;
			offsetX /= factor;
			offsetY /= factor;
		}

		if (size > 0) {
			renderSprite3D(screen, x, y, size, 0, size, size, sprite, offsetX, offsetY);
		}
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
			renderSpriteLOD(screen, sprite.getPosition(), sprite.getLodStart(), sprite.getLodScale(), sprite.getHeight(), sprite.getBase(), sprite.getOffsetX(), sprite.getOffsetY());
		} else {
			renderSprite3D(screen, sprite.getPosition(), sprite.getTextureX(), sprite.getTextureY(), sprite.getWidth(), sprite.getHeight(), sprite.getBase(), sprite.getOffsetX(), sprite.getOffsetY());
		}
	}

	public void renderSprite3D(Screen screen, double x, double y, int startX, int startY, int width, int height, Sprite sprite, int offsetX, int offsetY) {
		Vector3d projected = projectWorldToViewport(screen, x, y);
		if (projected == null)
			return;
		
		int sx = (int) (projected.getX() - width / 2) + offsetX;
		int sy = (int) (projected.getY() - height) + offsetY;
		
		double z = projected.getZ();
		
		for (int yp = 0; yp < height; ++yp) {
			int screenY = sy + yp;
			if (screenY < 0 || screenY >= (int)screen.getDimension().getHeight()) continue;
			
			for (int xp = 0; xp < width; ++xp) {
				int screenX = sx + xp;
				if (screenX < 0 || screenX >= (int)screen.getDimension().getWidth()) continue;
				
				int color = sprite.getPixel(xp + startX, yp + startY);
				if (color == sprite.getTransparentColor()) continue;
				
				int idx = screenX + screenY * (int)size.getWidth();
				
				if (zBuffer[idx] < z) continue;
				zBuffer[idx] = z;
				screen.setPixel(sx + xp, sy + yp, color);
			}
		}
	}

	public void renderWall(Screen screen, Vector2d start, Vector2d end, int color) {
		renderWall(screen, new Vector3d(start.getX(), start.getY(), -CAMERA_HEIGHT), new Vector3d(end.getX(), end.getY(), CEILING_HEIGHT), color);
	}
	
	public void renderWall(Screen screen, Vector3d start, Vector3d end, int color) {
		double centerX = size.getWidth() * 0.5;
		double centerY = size.getHeight() * 1.0 / HORIZON_OFFSET;

		double sin = Math.sin(rotation);
		double cos = Math.cos(rotation);

		double xo1 = start.getX() - cameraPosition.getX();
		double yo1 = start.getY() - cameraPosition.getY();

		double xo2 = end.getX() - cameraPosition.getX();
		double yo2 = end.getY() - cameraPosition.getY();
		
		double x1 = xo1 * cos + yo1 * -sin;
		double y1 = xo1 * sin + yo1 * cos;

		double x2 = xo2 * cos + yo2 * -sin;
		double y2 = xo2 * sin + yo2 * cos;

		double clip = 0.1;
		if (y1 < clip && y2 < clip) return;
		if (y1 < clip) {
			double p = (clip - y1) / (y2 - y1);
			y1 = y1 + (y2 - y1) * p;
			x1 = x1 + (x2 - x1) * p;
		}
		
		if (y2 < clip) {
			double p = (clip - y1) / (y2 - y1);
			y2 = y1 + (y2 - y1) * p;
			x2 = x1 + (x2 - x1) * p;
		}
		
		double xp1 = (x1 / y1) * size.getWidth() + centerX;
		double xp2 = (x2 / y2) * size.getWidth() + centerX;
		
		if (xp2 <= xp1) return;
		
		double l1 = (-start.getZ() / y1) * size.getHeight() + centerY;
		double u1 = (-end.getZ() / y1) * size.getHeight() + centerY;
		
		double l2 = (-start.getZ() / y2) * size.getHeight() + centerY;
		double u2 = (-end.getZ() / y2) * size.getHeight() + centerY;
		
		int xpi1 = (int)xp1;
		int xpi2 = (int)xp2;
		
		double iz1 = 1 / y1;
		double iz2 = 1 / y2;
		
		for (int x = xpi1; x <= xpi2; ++x) {
			if (x < 0 || x >= (int)size.getWidth()) continue;
			
			double p = (x - xp1) / (xp2 - xp1);
			
			double yp1 = u1 + (u2 - u1) * p;
			double yp2 = l1 + (l2 - l1) * p;
			
			double z = 1 / (iz1 + (iz2 - iz1) * p);
			
			int ypi1 = (int)yp1;
			int ypi2 = (int)yp2;

			for (int y = ypi1; y <= ypi2; ++y) {
				if (y < 0 || y >= (int)size.getHeight()) continue;
				
				if (zBuffer[x + y * (int)size.getWidth()] < z) continue;
				zBuffer[x + y * (int)size.getWidth()] = z;
				screen.setPixel(x, y, color);
			}
		}
	}

	public void render(Screen screen) {
		double x0 = screen.getDimension().getWidth() * 0.5;
		double y0 = screen.getDimension().getHeight() * 1.0 / HORIZON_OFFSET;

		double sin = Math.sin(rotation);
		double cos = Math.cos(rotation);

		for (int yp = 0; yp < screen.getDimension().getHeight(); ++yp) {
			double theta = -(yp - y0) / screen.getDimension().getHeight();
			double z = CAMERA_HEIGHT / (theta - 0.01);
			if (yp < y0) {
				z = CEILING_HEIGHT / -theta;
			}

			for (int xp = 0; xp < screen.getDimension().getWidth(); ++xp) {
				double phi = (xp - x0) / screen.getDimension().getWidth();
				double x = phi * z;

				double xs = x * cos + z * sin - cameraPosition.getX();
				double zs = x * -sin + z * cos - cameraPosition.getY();

				int color;
				double depth = Math.sqrt(z * z + x * x);
				if (yp - y0 < 0) {
					color = dataSource.getCeilingColor(xs, -zs);
					if (color < 0) {
						color = dataSource.getSkyColor();
						depth = 1000000;
					}
				} else {
					color = dataSource.getFloorColor(xs, -zs);
				}
				screen.setPixel(xp, yp, color);
				zBuffer[xp + yp * (int) size.getWidth()] = depth;
			}
		}
	}

	public void postProcess(Screen screen) {
		int sky = dataSource.getSkyColor();
		int skyR = ((sky >> 16) & 0xFF);
		int skyG = ((sky >> 8) & 0xFF);
		int skyB = ((sky) & 0xFF);

		screen.lockRW();
		for (int yp = 0; yp < screen.getDimension().getHeight(); ++yp) {
			for (int xp = 0; xp < screen.getDimension().getWidth(); ++xp) {
				double z = zBuffer[xp + yp * (int)size.getWidth()];
				int br = 0xff - (int)((z * z * 0.01 - 10));
				if (br < 0) br = 0;
				if (br > 0xff) br = 0xff;
				int color = screen.getPixel(xp, yp);
				int r = (color >> 16) & 0xff;
				int g = (color >> 8) & 0xff;
				int b = (color) & 0xff;
				r = r * br / 255 + skyR * (0xFF - br) / 255;
				if (r > 0xff) r = 0xff;
				g = g * br / 255 + skyG * (0xFF - br) / 255;
				if (g > 0xff) g = 0xff;
				b = b * br / 255 + skyB * (0xFF - br) / 255;
				if (b > 0xff) b = 0xff;
				screen.setPixel(xp, yp, Dither.lookupColor(xp, yp, (r << 16) | (g << 8) | b));
			}
		}
		screen.unlockRW();
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
