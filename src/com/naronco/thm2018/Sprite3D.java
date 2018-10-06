package com.naronco.thm2018;

import com.deviotion.ld.eggine.graphics.Sprite;
import com.deviotion.ld.eggine.math.Vector2d;

public class Sprite3D {
	private Vector2d position;
	private Sprite base;
	private boolean lod;
	private double lodScale;
	private int width, height;
	private int textureX, textureY;
	private int offsetX, offsetY;

	public Sprite3D(Vector2d position, Sprite base, double lodScale, int offsetX, int offsetY) {
		this.position = position;
		this.base = base;
		this.lod = true;
		this.lodScale = lodScale;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public Sprite3D(Vector2d position, Sprite base, int width, int height, int textureX, int textureY, int offsetX, int offsetY) {
		this.position = position;
		this.base = base;
		this.width = width;
		this.height = height;
		this.textureX = textureX;
		this.textureY = textureY;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
	}

	public Vector2d getPosition() {
		return position;
	}

	public void setPosition(Vector2d position) {
		this.position = position;
	}

	public Sprite getBase() {
		return base;
	}

	public void setBase(Sprite base) {
		this.base = base;
	}

	public boolean isLod() {
		return lod;
	}

	public void setLod(boolean lod) {
		this.lod = lod;
	}

	public double getLodScale() {
		return lodScale;
	}

	public void setLodScale(double lodScale) {
		this.lodScale = lodScale;
	}

	public int getTextureX() {
		return textureX;
	}

	public void setTextureX(int textureX) {
		this.textureX = textureX;
	}

	public int getTextureY() {
		return textureY;
	}

	public void setTextureY(int textureY) {
		this.textureY = textureY;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
}
