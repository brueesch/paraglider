package ch.zhaw.paraglider.physics;

/**
 * This Class represents a Vector
 * 
 * @author Jonas Gschwend
 * 
 */

public class Vector2D extends Object {
	
	private double x;
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
	public Vector2D(double x, double z)
	{
		this.setX(x);
		this.setZ(z);
	}
	
	/*
	 * Returns an Vector with three dimensions which values are 0	 
	 *  
	 */
	public Vector2D(){
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
	
	public Vector2D add(Vector2D v)
	{
		return new Vector2D(this.getX() + v.getX(), this.getZ() + v.getZ());
	}
	public Vector2D sub(Vector2D v)
	{
		return new Vector2D(this.getX() - v.getX(), this.getZ() - v.getZ());
	}
	
	public double getScalarProduct(Vector2D v)
	{
		return this.getX()* v.getX() + this.getZ() * v.getZ();
	}
	
	public double getNorm()
	{
		return Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getZ(), 2));
	}
	
	public double getAngleToVector2D(Vector2D v, Unit unit)
	{
		if(unit == Unit.Degree)
		{
			return Math.toDegrees(this.getScalarProduct(v) / (this.getNorm() * v.getNorm()));
		}
		return this.getScalarProduct(v) / (this.getNorm() * v.getNorm());
			
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
		
		if(v.getX() == this.getX() && v.getZ() == this.getZ()){
			return true;
		}
		return false;
	}
	
	public String ToString()
	{
		return "(" + this.getX() + ", " + this.getZ() + ")";
	}

}
