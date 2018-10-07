package com.deviotion.ld.eggine.graphics;

import com.deviotion.ld.eggine.math.Dimension2d;
import com.deviotion.ld.eggine.math.Dimension2i;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferStrategy;

/**
 * Eggine
 * A last minute game engine for Ludum Dare.
 *
 * @author Alex Nicholson (TechnoCF)
 */

public class Window extends JFrame {

	private Dimension2i dimension;
	private int scale;
	private Screen screen;
	private Canvas canvas;

	public Window(String title, Dimension2i dimension, int scale) {
		this.setTitle(title);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.canvas = new Canvas();
		this.canvas.setBackground(new Color(0, 0, 0));

		this.add(this.canvas);

		resize(dimension, scale);

		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}

	public void resize(Dimension2i dimension, int scale) {
		int oldRawWidth = this.dimension != null ? this.dimension.getWidth() * this.scale : 0;
		int oldRawHeight = this.dimension != null ? this.dimension.getHeight() * this.scale : 0;
		boolean dimensionChanged = this.dimension == null ? true : (this.dimension.getWidth() != dimension.getWidth() || this.dimension.getHeight() != dimension.getHeight());
		this.dimension = dimension;
		this.scale = scale;

		if ((int) this.dimension.getWidth() * this.scale == oldRawWidth && (int) this.dimension.getHeight() * this.scale == oldRawHeight) {
			// already ok
		} else {
			this.canvas.setSize((int) this.dimension.getWidth() * this.scale,
					(int) this.dimension.getHeight() * this.scale);
			this.pack();

		}
		if (this.screen == null) {
			this.screen = new Screen(this.dimension);
		} else if (dimensionChanged) {
			this.screen.resize(dimension);
		}
	}

	public Dimension2i getDimension() {
		return this.dimension;
	}

	public Screen getScreen() {
		return this.screen;
	}

	public Canvas getCanvas() {
		return this.canvas;
	}

	public int getScale() {
		return this.scale;
	}

	public void render() {
		BufferStrategy bufferStrategy = this.canvas.getBufferStrategy();
		if (bufferStrategy == null) {
			this.canvas.createBufferStrategy(3);
			bufferStrategy = this.canvas.getBufferStrategy();
		}

		Graphics graphics = bufferStrategy.getDrawGraphics();

		graphics.drawImage(this.screen.getBufferedImage(), 0, 0, (int) this
				.dimension.getWidth() * this.scale, (int) this.dimension
				.getHeight() * this.scale, null);

		bufferStrategy.show();
		graphics.dispose();
	}

}