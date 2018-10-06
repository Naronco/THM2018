package com.naronco.thm2018.maze;

import java.util.List;

public class Level {
	private List<Point> points;
	private int index;

	public Level(List<Point> points) {
		this.points = points;
		this.index = 0;
	}

	public List<Point> getNextPoints() {
		List<Point> targets = points.get(index).getTargets();
		if (targets.size() == 1) {
			jump(targets.get(0));
			return getNextPoints();
		}
		return targets;
	}

	public void jump(Point point) {
		int newIndex = points.indexOf(point);
		if (newIndex == -1)
			throw new IllegalArgumentException("point not in points array");
		index = newIndex;
	}
}
