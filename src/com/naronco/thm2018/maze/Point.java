package com.naronco.thm2018.maze;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Point {
	public Way[] targets = new Way[3];

	public Point() {
	}

	public void setTargets(Way[] targets) {
		this.targets = targets;
	}

	public Way[] getTargets() {
		return targets;
	}

	public boolean hasIntersection() {
		return (targets[0] != null ? 1 : 0) + (targets[1] != null ? 1 : 0) + (targets[2] != null ? 1 : 0) > 1;
	}

	public List<Way> getIntersection() {
		List<Way> ret = new ArrayList<>(targets.length);
		for (int i = 0; i < targets.length; i++)
			ret.add(targets[i]);
		return ret;
	}

	public void setTarget(int i, Way next) {
		targets[i] = next;
	}

	public boolean isFull() {
		return targets[0] != null && targets[1] != null && targets[2] != null;
	}

	public Way getFirst() {
		for (Way target : targets) {
			if (target != null) {
				return target;
			}
		}
		throw new IllegalArgumentException("Targets empty");
	}
}
