package com.deviotion.ld.eggine.math;

public class Line2d {
	private Vector2d start, end;

	public Line2d(Vector2d start, Vector2d end) {
		this.start = start;
		this.end = end;
	}

	public Vector2d getStart() {
		return start;
	}

	public void setStart(Vector2d start) {
		this.start = start;
	}

	public Vector2d getEnd() {
		return end;
	}

	public void setEnd(Vector2d end) {
		this.end = end;
	}
}
