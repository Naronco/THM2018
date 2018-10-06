package com.naronco.thm2018.maze;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Point {
	public Point[] targets = new Point[4];

	public Point() {
	}

	public void setTargets(Point[] targets) {
		this.targets = targets;
	}

	public Point[] getTargets() {
		return targets;
	}

	public boolean hasIntersection() {
		return targets[1] != null || targets[2] != null || targets[3] != null;
	}

	public List<Point> getIntersection() {
		List<Point> ret = new ArrayList<>(3);
		for (int i = 1; i < targets.length; i++)
			ret.add(targets[i]);
		return ret;
	}

	public void setTarget(int i, Point next) {
		targets[i] = next;
	}

	public boolean isFull() {
		return targets[1] != null && targets[2] != null && targets[3] != null;
	}
}
