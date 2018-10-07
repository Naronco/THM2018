package com.deviotion.ld.eggine.loop;

import com.deviotion.ld.eggine.graphics.Screen;
import com.deviotion.ld.eggine.graphics.Window;

/**
 * Eggine
 * A last minute game engine for Ludum Dare.
 *
 * @author Alex Nicholson (TechnoCF)
 *
 */

public abstract class Loop {

    private int frameRate;
    private int updateRate;
    private int frames;
    private int updates;
    private int fps;
    private int ups;
    private double calculatedFrameRate;
    private double calculatedUpdateRate;
    private double lagRender;
    private double lagUpdate;
    private long lastLoop;
    private long lastFPS;
    private Window window;
    private boolean running;

    public Loop (int fps, int ups, Window window) {
        setFps(fps);
	    setUps(ups);
        this.lastLoop = 0;
        this.lastFPS = 0;
        this.window = window;
    }

	public void setFps(int frameRate) {
		this.frameRate = frameRate;
		this.calculatedFrameRate = 1000000000f / this.frameRate;
		this.lagRender = 0;
		this.frames = 0;
		this.fps = 0;
		this.lagRender = 0;
	}

	public void setUps(int updateRate) {
		this.updateRate = updateRate;
		this.calculatedUpdateRate = 1000000000f / this.updateRate;
		this.lagUpdate = 0;
		this.updates = 0;
		this.ups = 0;
		this.lagUpdate = 0;
	}

    public int getFps () {
        return this.fps;
    }
    public int getUps () {
        return this.ups;
    }
    public Window getWindow() {
        return this.window;
    }

    public void start () {
        this.lastLoop = System.nanoTime();

        try {
            running = true;
            while (running && window.isValid()) {
                long now = System.nanoTime();
                long taken = now - this.lastLoop;
                this.lastLoop = now;

                this.lagRender += taken;
                this.lagUpdate += taken;

                while (this.lagUpdate > this.calculatedUpdateRate) {
                    this.update(1.0 / this.updateRate);

                    this.lagUpdate -= this.calculatedUpdateRate;
                    this.updates++;
                }

                /*while (this.lagRender > this.calculatedFrameRate) {
                    this.render(window.getScreen());
                    this.window.render();

                    this.lagRender -= this.calculatedFrameRate;
                    this.frames++;
                }*/
	            if (this.lagRender > this.calculatedFrameRate) {
		            this.render(window.getScreen());
		            this.window.render();

		            this.lagRender = 0;
		            this.frames++;
	            }

                now = System.nanoTime();
                if (now >= this.lastFPS + 1000000000f) {
                    this.lastFPS = now;

                    this.fps = this.frames;
                    this.ups = this.updates;

                    this.frames = 0;
                    this.updates = 0;
                }

                Thread.sleep(0);
            }
            window.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        running = false;
    }

    public abstract void render (Screen screen);
    public abstract void update (double delta);

}