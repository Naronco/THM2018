package com.deviotion.ld.eggine.math;

/**
 * Eggine
 * A last minute game engine for Ludum Dare.
 *
 * @author Alex Nicholson (TechnoCF), Jan Jurzitza
 */

public class Vector3d {

	private double x;
	private double y;
	private double z;

	public Vector3d(Vector3d input) {
		this.x = input.getX();
		this.y = input.getY();
		this.z = input.getZ();
	}

	public Vector3d(Vector2d xy, double z) {
		this.x = xy.getX();
		this.y = xy.getY();
		this.z = z;
	}

	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public double getX() {
		return this.x;
	}

	public double getY() {
		return this.y;
	}

	public double getZ() {
		return z;
	}

	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public double distance(Vector3d vector) {
		double distX = vector.getX() - this.x;
		double distY = vector.getY() - this.y;
		double distZ = vector.getZ() - this.z;

		return Math.sqrt(distX * distX + distY * distY + distZ * distZ);
	}

	public Vector3d add(Vector3d vector) {
		return new Vector3d(x + vector.x, y + vector.y, z + vector.z);
	}

	public Vector3d subtract(Vector3d vector) {
		return new Vector3d(x - vector.x, y - vector.y, z - vector.z);
	}

	public Vector3d multiply(double v) {
		return new Vector3d(x * v, y * v, z * v);
	}

	public double getLengthSquared() { // ADDITION
		return this.x * this.x + this.y * this.y + this.z * this.z;
	}

	public double getLength() { // ADDITION
		return Math.sqrt(getLengthSquared());
	}

	public Vector3d normalized() { // ADDITION
		return new Vector3d(this.x, this.y, this.z).multiply(1.0 / this.getLength());
	}

	public Vector3d copy() { // ADDITION
		return new Vector3d(this.x, this.y, this.z);
	}

	public double dot(Vector3d vector) { // ADDITION
		return this.x * vector.x + this.y * vector.y + this.z * vector.z;
	}

	public Vector2d xy() {
		return new Vector2d(x, y);
	}

	public Vector2d xz() {
		return new Vector2d(x, z);
	}

	public Vector2d yz() {
		return new Vector2d(y, z);
	}
}