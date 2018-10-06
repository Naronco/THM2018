package com.naronco.thm2018.maze;

public class Way {
	private Point target;
	private long seed;
	private String name;

	public Way(Point target, long seed, String name) {
		this.target = target;
		this.seed = seed;
		this.name = name;
	}

	public Point getTarget() {
		return target;
	}

	public void setTarget(Point target) {
		this.target = target;
	}

	public long getSeed() {
		return seed;
	}

	public void setSeed(long seed) {
		this.seed = seed;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
