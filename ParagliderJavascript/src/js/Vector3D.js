function Vector3D(valueX, valueY, valueZ)
{
	var x = null
	var y = null;
	var z = null;
	
	if(valueX == undefined || valueY == undefined || valueZ == undefined)
	{
		x = 0;
		y = 0;
		z = 0;
	}
	else
	{
		x = valueX;
		y = valueY;
		z = valueZ;
	}
	
	this.getX = function()
	{
		return x;
	}
	
	this.setX = function(value)
	{
		x = value;
	}
	
	this.getY = function()
	{
		return y;
	}
	
	this.setY = function(value)
	{
		y = value;
	}
	
	this.getZ = function()
	{
		return z;
	}
	
	this.setZ = function(value)
	{
		z = value;
	}
	this.add = function(v)
	{
		if(v instanceof Vector3D)
		{			
			return new Vector3D(this.getX() + v.getX(), this.getY() + v.getY(), this.getZ() + v.getZ());
		}
		else
		{
			return null;
		}
	}
	this.sub = function(v)
	{
		if(v instanceof Vector3D)
		{			
			return new Vector3D(this.getX() - v.getX(), this.getY() - v.getY(), this.getZ() - v.getZ());
		}
		else
		{
			return null;
		}
	}
	
	this.log = function()
	{
		console.log("<----- Logging called for Class 'Vector3D' ----->");
		console.log("Typeof: " + typeof this);
		console.log("X: " + x);
		console.log("Y: " + y);
		console.log("Z: " + z);
		console.log("<----- End of Logging for Class 'Vector3D' ----->");
		console.log(" ");
	}
}