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
		Point point = points.get(index);
		if (index == points.size() - 1)
			return null;
		if (!point.hasIntersection()) {
			jump(point.getTargets()[0]);
			return getNextPoints();
		}
		return point.getIntersection();
	}

	public void jump(Point point) {
		int newIndex = points.indexOf(point);
		if (newIndex == -1)
			throw new IllegalArgumentException("point not in points array");
		index = newIndex;
	}

	public Point getPoint() {
		return points.get(index);
	}
}
