package com.naronco.thm2018.maze;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class MazeGenerator {
	private List<Point> points = new ArrayList<>();
	private Random random;
	private static final List<String> STREET_SUFFIXES_AND_NAMES = readStreetNames();

	private static List<String> readStreetNames() {
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File("res/streetnames.txt")));
			List<String> ret = new ArrayList<>();
			String line;
			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty())
					ret.add(null);
				else
					ret.add(line.replace("ß", "ss").toUpperCase().trim());
			}
			return ret;
		} catch (IOException e) {
			e.printStackTrace();
			return Arrays.asList(null);
		}
	}

	public MazeGenerator(Random random) {
		this.random = random;
	}

	public Level generate(int numPoints, int numIntersections) {
		Point prev = null;
		for (int i = 0; i < numPoints; i++) {
			Point next = new Point();
			points.add(next);
			if (prev != null) {
				prev.setTarget(random.nextInt(prev.getTargets().length), new Way(next, random.nextLong(), generateStreetName()));
			}
			prev = next;
		}
		for (int i = 0; i < numIntersections; i++) {
			boolean done = false;
			for (int noTry = 0; noTry < 20; noTry++) {
				int fromIndex = random.nextInt(points.size());
				if (fromIndex == 0)
					continue;
				int toIndex = fromIndex - 1 - random.nextInt(fromIndex);
				Point from = points.get(fromIndex);
				Point to = points.get(toIndex);

				boolean exists = false;
				for (Way target : from.getTargets()) {
					if (target != null && target.getTarget() == to) {
						exists = true;
						break;
					}
				}
				if (from.isFull() || exists)
					continue;

				int numOpen = 0;
				Way[] targets = from.getTargets();
				for (int n = 0; n < targets.length; n++)
					if (targets[n] == null)
						numOpen++;
				int insertInto = random.nextInt(numOpen);
				for (int n = 0; n < targets.length; n++)
					if (targets[n] == null && --insertInto == 0) {
						targets[n] = new Way(to, random.nextLong(), generateStreetName());
						break;
					}
				from.setTargets(targets);
				done = true;
				break;
			}
			if (!done)
				System.out.println("Could not manage to create intersection #" + i);
		}
		return new Level(points);
	}

	private String generateStreetName() {
		int nullIndex = STREET_SUFFIXES_AND_NAMES.indexOf(null);
		if (nullIndex == 0)
			return "Strasse";
		int suffix = random.nextInt(nullIndex);
		int base = random.nextInt(STREET_SUFFIXES_AND_NAMES.size() - nullIndex - 1);

		String baseStr = STREET_SUFFIXES_AND_NAMES.get(nullIndex + 1 + base);
		if (baseStr.endsWith("$"))
			return baseStr.substring(0, baseStr.length() - 1);
		else if (baseStr.endsWith("_"))
			return baseStr.substring(0, baseStr.length() - 1) + " " + STREET_SUFFIXES_AND_NAMES.get(suffix);
		else
			return baseStr + STREET_SUFFIXES_AND_NAMES.get(suffix);
	}
}
