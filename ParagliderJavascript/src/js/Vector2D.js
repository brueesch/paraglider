//DRAFT, Need to doublecheck
function Vector2D(valueX, valueZ)
{
	var x = null
	var z = null;
	
	if(valueX == undefined || valueZ == undefined)
	{
		x = 0;
		z = 0;
	}
	else
	{
		x = valueX;
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
		if(v instanceof Vector2D)
		{			
			return new Vector2D(this.getX() + v.getX(), this.getZ() + v.getZ());
		}
		else
		{
			return null;
		}
	}
	this.sub = function(v)
	{
		if(v instanceof Vector2D)
		{			
			return new Vector2D(this.getX() - v.getX(), this.getZ() - v.getZ());
		}
		else
		{
			return null;
		}
	}
	this.getScalarProduct = function(v)
	{
		return this.getX() * v.getX() + this.getZ() * v.getZ();
	}
	this.getNorm = function()
	{
		return Math.sqrt(Math.pow(this.getX(),2) + Math.pow(this.getZ(), 2))
	}
	this.getAngleToVector2D(v)
	{
		if(v instanceof Vector2D)
		{
			return this.getScalarProduct(v) / (this.getNorm() * v.getNorm());
		}
		else
		{
			return null;
		}
	}
	
	this.log = function(name)
	{
		console.log("<----- Vector2D '" + name + "' = [" + x + "," + z + "] ----->");
	}
}