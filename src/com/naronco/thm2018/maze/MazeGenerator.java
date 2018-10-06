package com.naronco.thm2018.maze;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MazeGenerator {
	private List<Point> points = new ArrayList<>();
	private Random random;

	public MazeGenerator(Random random) {
		this.random = random;
	}

	public Level generate(int numPoints, int numIntersections) {
		Point prev = null;
		for (int i = 0; i < numPoints; i++) {
			Point next = new Point();
			points.add(next);
			if (prev != null) {
				prev.setTarget(0, next);
			}
			prev = next;
		}
		for (int i = 0; i < numIntersections; i++) {
			while (true) {
				int fromIndex = random.nextInt(points.size());
				int toIndex = (fromIndex + random.nextInt(points.size() - 1)) % points.size();
				Point from = points.get(fromIndex);
				Point to = points.get(toIndex);
				if (from.isFull() || Arrays.asList(from.getTargets()).contains(to))
					continue;
				int numOpen = 0;
				Point[] targets = from.getTargets();
				for (int n = 0; n < targets.length; n++)
					if (targets[n] == null)
						numOpen++;
				int insertInto = random.nextInt(numOpen);
				for (int n = 0; n < targets.length; n++)
					if (targets[n] == null && --insertInto == 0) {
						targets[n] = to;
						break;
					}
				from.setTargets(targets);
				break;
			}
		}
		return new Level(points);
	}
}
