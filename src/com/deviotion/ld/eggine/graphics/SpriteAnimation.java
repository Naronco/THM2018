package com.deviotion.ld.eggine.graphics;

/**
 * Eggine
 * A last minute game engine for Ludum Dare.
 *
 * @author Alex Nicholson (TechnoCF)
 */

public class SpriteAnimation {

	private SpriteSheet spriteSheet;
	public int animationFps;
	private int startTile;
	private int endTile;
	private int currentTile;
	private int tiles;
	private long lastTile;

	public SpriteAnimation(SpriteSheet spriteSheet, int startTile, int
			endTile, int fps) {
		this.spriteSheet = spriteSheet;
		this.animationFps = fps;
		this.startTile = startTile;
		this.endTile = endTile;
		this.currentTile = startTile;

		this.tiles = this.endTile - this.startTile;
	}

	public SpriteSheet getSpriteSheet() {
		return this.spriteSheet;
	}

	public int getTile() {
		return this.currentTile;
	}

	public void setStartTile(int tile) {
		startTile = tile;
		tiles = endTile - startTile;
	}

	public void setEndTile(int tile) {
		endTile = tile;
		tiles = endTile - startTile;
	}

	public boolean nextFrame() {
		long now = System.nanoTime();
		if (now >= this.lastTile + (1000000000f / this.animationFps)) {
			this.lastTile = now;
			this.currentTile++;

			if (this.currentTile > this.startTile + this.tiles) {
				this.currentTile = this.startTile;
				return true;
			}
		}
		return false;
	}

}