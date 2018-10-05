package com.deviotion.ld.eggine.math;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Polygon2d {
	private List<Vector2d> points;

	public Polygon2d(List<Vector2d> points) {
		this.points = points;
	}

	public Polygon2d(Vector2d... points) {
		this.points = Arrays.asList(points);
	}

	public boolean intersects(double x, double y) {
		for (int i = 0; i < points.size(); ++i) {
			Vector2d current = points.get(i);
			Vector2d next = i == (points.size() - 1) ? points.get(0) : points.get(i + 1);

			Vector2d dir = next.subtract(current);
			Vector2d normal = new Vector2d(-dir.getY(), dir.getX());

			Vector2d current2Point = new Vector2d(x, y).subtract(current);

			if (current2Point.dot(normal) < 0)
				return false;
		}

		return true;
	}

	public boolean intersects(Vector2d point) {
		return intersects(point.getX(), point.getY());
	}

	public boolean contains(Line2d line) {
		if (!intersects(line.getStart())) return false;
		if (!intersects(line.getEnd())) return false;
		return true;
	}

	public List<Vector2d> getPoints() {
		return points;
	}
}
