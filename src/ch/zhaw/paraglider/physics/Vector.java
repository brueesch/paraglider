package ch.zhaw.paraglider.physics;

/**
 * This Class represents a Vector
 * 
 * @author Jonas Gschwend
 * 
 */

public class Vector extends Object {
	
	private double x;
	private double y;
	private double z;
	
	public enum Unit
	{
		Radian, Degree;
	}
	
	/*
	 * Returns an Vector with three dimensions x,y,z	 * 
	 * @param double x
	 * @param double y
	 * @param double z
	 */
	public Vector(double x, double y, double z)
	{
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	
	public Vector(Vector v)
	{
		this.setX(v.getX());
		this.setY(v.getY());
		this.setZ(v.getZ());
	}
	
	/*
	 * Returns an Vector with three dimensions which values are 0	 
	 *  
	 */
	public Vector(){
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
	
	public Vector add(Vector v)
	{
		return new Vector(this.getX() + v.getX(), this.getY() + v.getY(), this.getZ() + v.getZ());
	}
	public Vector sub(Vector v)
	{
		return new Vector(this.getX() - v.getX(), this.getY() - v.getY(), this.getZ() - v.getZ());
	}
	@Override
	public boolean equals(Object obj)
	{
		if(obj == this){
			return true;
		}		
		if((obj == null) || (obj.getClass() != this.getClass())){
			return false;
		}
		
		Vector v = (Vector) obj;
		
		if(v.getX() == this.getX() && v.getY() == this.getY() && v.getZ() == this.getZ()){
			return true;
		}
		return false;
	}

}
