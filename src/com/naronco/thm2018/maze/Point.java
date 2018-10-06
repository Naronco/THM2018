package com.naronco.thm2018.maze;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Point {
    public List<Point> targets = new ArrayList<>();

    public Point() {
    }

    public void addTarget(Point point) {
        targets.add(point);
    }

    public List<Point> getTargets() {
        return targets;
    }
}
