package com.deviotion.ld.eggine.graphics;

import com.deviotion.ld.eggine.map.Map;
import com.deviotion.ld.eggine.math.Dimension2d;
import com.deviotion.ld.eggine.math.Vector2d;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

/**
 * Eggine
 * A last minute game engine for Ludum Dare.
 *
 * @author Alex Nicholson (TechnoCF)
 */

public class Screen {

	private Dimension2d dimension;
	private BufferedImage bufferedImage;
	private int[] pixels;

	public Screen(Dimension2d dimension) {
		this.dimension = dimension;
		this.bufferedImage = new BufferedImage((int) this.dimension.getWidth
				(), (int) this.dimension.getHeight(), BufferedImage
				.TYPE_INT_RGB);
		this.pixels = ((DataBufferInt) this.bufferedImage.getRaster()
				.getDataBuffer()).getData();
	}

	public Dimension2d getDimension() {
		return this.dimension;
	}

	public BufferedImage getBufferedImage() {
		return this.bufferedImage;
	}

	public int[] getPixels() {
		return this.pixels;
	}

	public void renderSprite(int x, int y, int startX, int startY, int endX,
							 int endY, Sprite sprite) {
		BufferedImage spriteImage = sprite.getBufferedImage();

		if (startX > sprite.getDimension().getWidth()) startX =
				(int) sprite.getDimension().getWidth();
		if (startY > sprite.getDimension().getHeight()) startY =
				(int) sprite.getDimension().getHeight();

		if (endX > (sprite.getDimension().getWidth() - startX)) endX =
				((int) sprite.getDimension().getWidth()
						- startX);
		if (endY > (sprite.getDimension().getHeight() - startY)) endY =
				((int) sprite.getDimension().getHeight()
						- startY);


		for (int j = startY; j < startY + endY; j++) {
			for (int i = startX; i < startX + endX; i++) {
				if (i >= spriteImage.getWidth() || i < 0 || j >= spriteImage.getHeight() || j < 0)
					continue;
				int pixelColor = spriteImage.getRGB(i, j);
				if (pixelColor != sprite.getTransparentColor()) {
					this.setPixel(x + (i - startX), y + (j - startY),
							pixelColor);
				}
			}
		}
	}

	public void renderSpriteFlipped(int x, int y, int startX, int startY, int endX,
									int endY, Sprite sprite) {
		BufferedImage spriteImage = sprite.getBufferedImage();

		if (startX > sprite.getDimension().getWidth()) startX =
				(int) sprite.getDimension().getWidth();
		if (startY > sprite.getDimension().getHeight()) startY =
				(int) sprite.getDimension().getHeight();

		if (endX > (sprite.getDimension().getWidth() - startX)) endX =
				((int) sprite.getDimension().getWidth()
						- startX);
		if (endY > (sprite.getDimension().getHeight() - startY)) endY =
				((int) sprite.getDimension().getHeight()
						- startY);


		for (int j = startY; j < startY + endY; j++) {
			for (int i = startX; i < startX + endX; i++) {
				if (i >= spriteImage.getWidth() || i < 0 || j >= spriteImage.getHeight() || j < 0)
					continue;
				int pixelColor = spriteImage.getRGB(i, j);
				if (pixelColor != sprite.getTransparentColor()) {
					this.setPixel(x + endX - 1 - (i - startX), y + (j - startY),
							pixelColor);
				}
			}
		}
	}

	public void renderClippedSprite(int x, int y, int startX, int startY, int endX, int endY, int clipX, int clipY, int clipWidth, int clipHeight, Sprite sprite) {
		BufferedImage spriteImage = sprite.getBufferedImage();

		if (startX > sprite.getDimension().getWidth()) startX =
				(int) sprite.getDimension().getWidth();
		if (startY > sprite.getDimension().getHeight()) startY =
				(int) sprite.getDimension().getHeight();

		if (endX > (sprite.getDimension().getWidth() - startX)) endX =
				((int) sprite.getDimension().getWidth()
						- startX);
		if (endY > (sprite.getDimension().getHeight() - startY)) endY =
				((int) sprite.getDimension().getHeight()
						- startY);

		for (int j = startY; j < startY + endY; j++) {
			for (int i = startX; i < startX + endX; i++) {
				if (i >= spriteImage.getWidth() || i < 0 || j >= spriteImage.getHeight() || j < 0)
					continue;
				int xx = x + (i - startX);
				int yy = y + (j - startY);
				if (xx < clipX || yy < clipY || xx >= clipX + clipWidth || yy >= clipY + clipHeight)
					continue;
				int pixelColor = spriteImage.getRGB(i, j);
				if (pixelColor != sprite.getTransparentColor()) {
					this.setPixel(xx, yy, pixelColor);
				}
			}
		}
	}

	public void renderSprite(int x, int y, Sprite sprite) {
		this.renderSprite(x, y, 0, 0, (int) sprite.getDimension().getWidth(),
				(int) sprite.getDimension().getHeight(), sprite);
	}

	public void renderScaledSprite(int x, int y, int scale, Sprite sprite) {
		BufferedImage spriteImage = sprite.getBufferedImage();

		int w = (int)sprite.getDimension().getWidth();
		int h = (int)sprite.getDimension().getHeight();

		for (int yy = 0; yy < h; yy++) {
			for (int xx = 0; xx < w; xx++) {
				int pixelColor = spriteImage.getRGB(xx, yy);
				if (pixelColor != sprite.getTransparentColor()) {
					for (int yo = 0; yo < scale; ++yo) {
						for (int xo = 0; xo < scale; ++xo) {
							this.setPixel(xx * scale + xo + x, yy * scale + yo + y, pixelColor);
						}
					}
				}
			}
		}
	}

	public void renderSprite(Vector2d location, Sprite sprite) {
		this.renderSprite((int) location.getX(), (int) location.getY(), sprite);
	}

	public void renderSprite(Vector2d location, Vector2d startLocation,
							 Dimension2d endLocation, Sprite sprite) {
		this.renderSprite((int) location.getX(), (int) location.getY(),
				(int) startLocation.getX(), (int) startLocation.getY(),
				(int) endLocation.getWidth(), (int) endLocation.getHeight(),
				sprite);
	}

	public void renderSprite(Vector2d location, int startX, int startY,
							 int endX, int endY, Sprite sprite) {
		this.renderSprite((int) location.getX(), (int) location.getY(), startX,
				startY, endX, endY, sprite);
	}

	public void renderSprite(int x, int y, Vector2d startLocation, int endX,
							 int endY, Sprite sprite) {
		this.renderSprite(x, y, (int) startLocation.getX(),
				(int) startLocation.getY(), endX, endY, sprite);
	}

	public void renderSprite(int x, int y, int startX, int startY,
							 Dimension2d endLocation, Sprite sprite) {
		this.renderSprite(x, y, startX, startY, (int) endLocation.getWidth(),
				(int) endLocation.getHeight(), sprite);
	}

	public void renderSprite(Vector2d location, Vector2d startLocation,
							 int endX, int endY, Sprite sprite) {
		this.renderSprite((int) location.getX(), (int) location.getY(), (int)
						startLocation.getX(), (int) startLocation.getY(), endX, endY,
				sprite);
	}

	public void renderSprite(Vector2d location, int startX, int startY,
							 Dimension2d endLocation, Sprite sprite) {
		this.renderSprite((int) location.getX(), (int) location.getY(),
				startX, startY, (int) endLocation.getWidth(), (int) endLocation
						.getHeight(), sprite);
	}

	public void renderSprite(int x, int y, Vector2d startLocation,
							 Dimension2d endLocation, Sprite sprite) {
		this.renderSprite(x, y, (int) startLocation.getX(), (int)
						startLocation.getY(), (int) endLocation.getWidth(),
				(int) endLocation.getHeight(), sprite);
	}

	public void renderSpriteFlipped(int x, int y, Vector2d startLocation,
									Dimension2d endLocation, Sprite sprite) {
		this.renderSpriteFlipped(x, y, (int) startLocation.getX(), (int)
						startLocation.getY(), (int) endLocation.getWidth(),
				(int) endLocation.getHeight(), sprite);
	}

	public void setPixel(int x, int y, int color) {
		if (x >= 0 && y >= 0 && x < this.dimension.getWidth() && y < this
				.dimension.getHeight()) {
			this.pixels[x + y * (int) this.dimension.getWidth()] = color;
		}
	}

	public void mixPixel(int x, int y, int color) {
		if (x >= 0 && y >= 0 && x < this.dimension.getWidth() && y < this
				.dimension.getHeight()) {
			int p = this.pixels[x + y * (int) this.dimension.getWidth()];
			int a = (color >> 24) & 0xFF;
			int r = ((p & 0xFF) * (255 - a) + (color & 0xFF) * a) / 255;
			int g = (((p >> 8) & 0xFF) * (255 - a) + ((color >> 8) & 0xFF) * a) / 255;
			int b = (((p >> 16) & 0xFF) * (255 - a) + ((color >> 16) & 0xFF) * a) / 255;
			this.pixels[x + y * (int) this.dimension.getWidth()] = r | (g << 8) | (b << 16);
		}
	}

	public void renderSpriteTile(int x, int y, SpriteSheet spriteSheet,
								 int tile) {
		Vector2d location = spriteSheet.getTileVector(tile);

		this.renderSprite(x, y, location, spriteSheet
				.getSpriteSize(), spriteSheet.getSprite());
	}

	public void renderSpriteTile(Vector2d vector, int tile, SpriteSheet
			spriteSheet) {
		this.renderSpriteTile((int) vector.getX(), (int) vector.getY(),
				spriteSheet, tile);
	}

	public void renderSpriteTileFlipped(int x, int y, SpriteSheet spriteSheet,
										int tile) {
		Vector2d location = spriteSheet.getTileVector(tile);

		this.renderSpriteFlipped(x, y, location, spriteSheet
				.getSpriteSize(), spriteSheet.getSprite());
	}

	public void renderSpriteTileFlipped(Vector2d vector, int tile, SpriteSheet
			spriteSheet) {
		this.renderSpriteTileFlipped((int) vector.getX(), (int) vector.getY(),
				spriteSheet, tile);
	}

	public void renderAnimatedSprite(int x, int y, SpriteAnimation
			spriteAnimation) {
		this.renderSpriteTile(x, y, spriteAnimation.getSpriteSheet(),
				spriteAnimation.getTile());
	}

	public void renderAnimatedSprite(Vector2d vector, SpriteAnimation
			spriteAnimation) {
		this.renderAnimatedSprite((int) vector.getX(), (int) vector.getY(),
				spriteAnimation);
	}

	public void renderAnimatedSpriteFlipped(int x, int y, SpriteAnimation
			spriteAnimation) {
		this.renderSpriteTileFlipped(x, y, spriteAnimation.getSpriteSheet(),
				spriteAnimation.getTile());
	}

	public void renderAnimatedSpriteFlipped(Vector2d vector, SpriteAnimation
			spriteAnimation) {
		this.renderAnimatedSpriteFlipped((int) vector.getX(), (int) vector.getY(),
				spriteAnimation);
	}

	public void mixRectangle(int x, int y, int width, int height, int color) {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				this.mixPixel(i + x, j + y, color);
			}
		}
	}

	public void renderRectangle(int x, int y, int width, int height, int
			color) {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				this.setPixel(i + x, j + y, color);
			}
		}
	}

	public void renderRectangle(Vector2d location, int width, int height,
								int color) {
		this.renderRectangle((int) location.getX(), (int) location.getY(),
				width, height, color);
	}

	public void renderRectangle(int x, int y, Dimension2d dimension, int
			color) {
		this.renderRectangle(x, y, (int) dimension.getWidth(), (int)
				dimension.getHeight(), color);
	}

	public void renderRectangle(Vector2d location, Dimension2d dimension,
								int color) {
		this.renderRectangle((int) location.getX(), (int) location.getY(),
				(int) dimension.getWidth(), (int) dimension.getHeight(), color);
	}

	public void mixOutlinedRectangle(int x, int y, int width, int height, int color) {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (i != 0 && i != width - 1 && j != 0 && j != height - 1) continue;
				this.mixPixel(i + x, j + y, color);
			}
		}
	}

	public void renderOutlinedRectangle(int x, int y, int width, int height, int
			color) {
		for (int j = 0; j < height; j++) {
			for (int i = 0; i < width; i++) {
				if (i != 0 && i != width - 1 && j != 0 && j != height - 1) continue;
				this.setPixel(i + x, j + y, color);
			}
		}
	}

	public void renderCircle(int x, int y, int radius, int color) {
		for (int j = y - radius; j <= y + radius; j++) {
			for (int i = x - radius; i <= x + radius; i++) {
				int dx = i - x;
				int dy = j - y;
				if ((dx * dx) + (dy * dy) < radius * radius) {
					this.setPixel(i, j, color);
				}
			}
		}
	}

	public void renderMap(int x, int y, Map map, SpriteSheet spriteSheet) {
		for (int j = 0; j < map.getDimension().getHeight(); j++) {
			for (int i = 0; i < map.getDimension().getWidth(); i++) {
				int tile = map.getTile(i, j);
				if (tile != -1) {
					this.renderSpriteTile(x + (i * (int) spriteSheet
									.getSpriteSize().getWidth()), y +
									(j * (int)
											spriteSheet.getSpriteSize()
													.getHeight()),
							spriteSheet, tile);
				}
			}
		}
	}

	public void fillScreen(int color) {
		Arrays.fill(this.pixels, color);
	}

	public void renderText(int x, int y, Font font, String text) {
		text = text.toUpperCase();
		int xOffs = 0, yOffs = 0;
		for (int i = 0; i < text.length(); ++i) {
			char ch = text.charAt(i);
			if (ch == '\n') {
				xOffs = 0;
				yOffs += (int) font.getCharacterSize().getHeight() + 1;
				continue;
			}
			renderSprite(x + xOffs, y + yOffs, font.getCharacterVector(ch), font.getCharacterSize(), font.getSprite());
			xOffs += (int) font.getCharacterSize().getWidth();
		}
	}

	public void renderClippedText(int x, int y, int clipX, int clipY, int clipWidth, int clipHeight, Font font, String text) {
		text = text.toUpperCase();
		int xOffs = 0, yOffs = 0;
		Dimension2d s = font.getCharacterSize();

		for (int i = 0; i < text.length(); ++i) {
			char ch = text.charAt(i);
			if (ch == '\n') {
				xOffs = 0;
				yOffs += (int) font.getCharacterSize().getHeight() + 1;
				continue;
			}
			Vector2d v = font.getCharacterVector(ch);
			renderClippedSprite(x + xOffs, y + yOffs, (int) v.getX(), (int) v.getY(), (int) s.getWidth(), (int) s.getHeight(), clipX, clipY, clipWidth, clipHeight, font.getSprite());
			xOffs += (int) font.getCharacterSize().getWidth();
		}
	}
}