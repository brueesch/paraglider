package ch.zhaw.paraglider.physics;

/**
 * This Class represents a Vector
 * 
 * @author Jonas Gschwend
 * 
 */

public class Vector {
	
	private double x;
	private double y;
	private double z;
	
	public Vector(double x, double y, double z)
	{
		this.setX(x);
		this.setY(y);
		this.setZ(z);
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
	
	public Vector add(Vector v)
	{
		return new Vector(this.getX() + v.getX(), this.getY() + v.getY(), this.getZ() + v.getZ());
	}
	public Vector sub(Vector v)
	{
		return new Vector(this.getX() - v.getX(), this.getY() - v.getY(), this.getZ() - v.getZ());
	}
	
	public double getScalarProduct(Vector v)
	{
		return this.getX()* v.getX() + this.getY() * v.getY() + this.getZ() * v.getZ();
	}
	
	public double getNorm()
	{
		return Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2) + Math.pow(this.getZ(), 2));
	}
	
	public double getAngleToVector(Vector v)
	{
		return this.getScalarProduct(v) / (this.getNorm()*v.getNorm());	
	}
	

}
