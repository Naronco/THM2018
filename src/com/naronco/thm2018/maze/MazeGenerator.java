package com.naronco.thm2018.maze;

import java.util.List;
import java.util.Random;

public class MazeGenerator {
    private List<Point> points;
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
                prev.addTarget(next);
            }
            prev = next;
        }
        for (int i = 0; i < numIntersections; i++) {
            while (true) {
                int fromIndex = random.nextInt(points.size());
                int toIndex = (fromIndex + random.nextInt(points.size() - 1)) % points.size();
                Point from = points.get(fromIndex);
                Point to = points.get(toIndex);
                if (from.getTargets().size() > 3 || from.getTargets().contains(to))
                    continue;
                from.addTarget(to);
                break;
            }
        }
        return new Level(points);
    }
}
