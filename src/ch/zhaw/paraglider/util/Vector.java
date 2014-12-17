package ch.zhaw.paraglider.util;

/**
 * This Class represents a 3 dimensional Vector.
 * 
 * @author Jonas Gschwend / Christian Brüesch
 * 
 */

public class Vector extends Object {

	private double x;
	private double y;
	private double z;

	public enum Unit {
		Radian, Degree;
	}

	/**
	 * Creates a Vector with three dimensions x,y,z
	 * 
	 * @param x
	 * @param y
	 * @param z
	 */
	public Vector(double x, double y, double z) {
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}

	/**
	 * Creates a Vector which is equal to the Vector given
	 * 
	 * @param v
	 */
	public Vector(Vector v) {
		this.setX(v.getX());
		this.setY(v.getY());
		this.setZ(v.getZ());
	}

	/**
	 * Creates a 0 - Vector
	 */
	public Vector() {
		this.setX(0);
		this.setY(0);
		this.setZ(0);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public double getZ() {
		return z;
	}

	public void setZ(double z) {
		this.z = z;
	}

	/**
	 * Adds two three dimensional Vectors.
	 * @param v
	 * @return new Vector
	 */
	public Vector add(Vector v) {
		return new Vector(this.getX() + v.getX(), this.getY() + v.getY(),
				this.getZ() + v.getZ());
	}

	/**
	 * Subtracts one Vector v from the current Vector.
	 * @param v
	 * @return new Vector
	 */
	public Vector sub(Vector v) {
		return new Vector(this.getX() - v.getX(), this.getY() - v.getY(),
				this.getZ() - v.getZ());
	}

	/**
	 * Compares two Vectors with each other.
	 * returns true if both are equal, false otherwise.
	 * 
	 * @return boolean
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if ((obj == null) || (obj.getClass() != this.getClass())) {
			return false;
		}

		Vector v = (Vector) obj;

		if (v.getX() == this.getX() && v.getY() == this.getY()
				&& v.getZ() == this.getZ()) {
			return true;
		}
		return false;
	}

}
