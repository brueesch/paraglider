package ch.zhaw.paraglider.util;

/**
 * This Class represents a 2 dimensional Vector.
 * 
 * @author Jonas Gschwend / Christian Brüesch
 * 
 */

public class Vector2D extends Object {

	private double x;
	private double z;

	public enum Unit {
		Radian, Degree;
	}

	/**
	 * Creates a Vector with two dimensions x,z
	 * 
	 * @param double x
	 * @param double z
	 */
	public Vector2D(double x, double z) {
		this.setX(x);
		this.setZ(z);
	}
	
	/**
	 * Creates a Vector which is equal to the Vector given
	 * 
	 * @param Vector v
	 */
	public Vector2D(Vector2D v) {
		this.setX(v.getX());
		this.setZ(v.getZ());
	}

	/**
	 * Creates a 0 - Vector
	 */
	public Vector2D() {
		this.setX(0);
		this.setZ(0);
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
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
	public Vector2D add(Vector2D v) {
		return new Vector2D(this.getX() + v.getX(), this.getZ() + v.getZ());
	}

	/**
	 * Subtracts one Vector v from the current Vector.
	 * @param v
	 * @return new Vector
	 */
	public Vector2D sub(Vector2D v) {
		return new Vector2D(this.getX() - v.getX(), this.getZ() - v.getZ());
	}

	/**
	 * Calculates and returns the Scalar Product between two Vectors.
	 * @param Vector2D v
	 * @return result Vector
	 */
	public double getScalarProduct(Vector2D v) {
		return this.getX() * v.getX() + this.getZ() * v.getZ();
	}

	/**
	 * Calculates and returns the normal.
	 * @return double normal
	 */
	public double getNorm() {
		return Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getZ(), 2));
	}

	/**
	 * Calculates the Angle between twso Vectors.
	 * @param v
	 * @param unit
	 * @return double angle
	 */
	public double getAngleToVector2D(Vector2D v, Unit unit) {
		if (unit == Unit.Degree) {
			return Math.toDegrees(this.getScalarProduct(v)
					/ (this.getNorm() * v.getNorm()));
		}
		return this.getScalarProduct(v) / (this.getNorm() * v.getNorm());

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

		if (v.getX() == this.getX() && v.getZ() == this.getZ()) {
			return true;
		}
		return false;
	}

	public String ToString() {
		return "(" + this.getX() + ", " + this.getZ() + ")";
	}

}
