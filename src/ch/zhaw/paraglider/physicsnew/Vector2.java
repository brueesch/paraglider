package ch.zhaw.paraglider.physicsnew;

/**
 * This Class represents a Vector
 * 
 * @author Jonas Gschwend
 * 
 */

public class Vector2 extends Object {
	
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
	public Vector2(double x, double y, double z)
	{
		this.setX(x);
		this.setY(y);
		this.setZ(z);
	}
	
	/*
	 * Returns an Vector with three dimensions which values are 0	 
	 *  
	 */
	public Vector2(){
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
	
	public Vector2 add(Vector2 v)
	{
		return new Vector2(this.getX() + v.getX(), this.getY() + v.getY(), this.getZ() + v.getZ());
	}
	public Vector2 sub(Vector2 v)
	{
		return new Vector2(this.getX() - v.getX(), this.getY() - v.getY(), this.getZ() - v.getZ());
	}
	
	public double getScalarProduct(Vector2 v)
	{
		return this.getX()* v.getX() + this.getY() * v.getY() + this.getZ() * v.getZ();
	}
	
	public double getNorm()
	{
		return Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2) + Math.pow(this.getZ(), 2));
	}
	
	public double getAngleToVector(Vector2 v, ch.zhaw.paraglider.physicsnew.Vector2.Unit radian)
	{
		if(radian == Unit.Degree)
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
		
		Vector2 v = (Vector2) obj;
		
		if(v.getX() == this.getX() && v.getY() == this.getY() && v.getZ() == this.getZ()){
			return true;
		}
		return false;
	}

}
