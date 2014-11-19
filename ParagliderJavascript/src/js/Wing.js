/*var glider = function()
{
	var leftWing = new Wing();
	var childFunction = function(){
	alert(protectedValue);
	}
	childFunction();
}
var object = new parentFunction();
*/



function Wing(name)
{
	var name = name;
	
	var OPTIMAL_GLIDE_RATIO = 9;
	var SPEED_OPTIMAL_GLIDING = 9.722;
	var c1 = 1 / (2 * Math.pow(SPEED_OPTIMAL_GLIDING, 2) * OPTIMAL_GLIDE_RATIO);
	var c2 = Math.pow(SPEED_OPTIMAL_GLIDING, 2)/ (2 * OPTIMAL_GLIDE_RATIO);
	
	var currentSpeed = SPEED_OPTIMAL_GLIDING;
	var verticalSpeed = null;
	var inFullStall = false;
	
	this.getHorizontalSpeed = function()
	{
		return currentSpeed;
	}
	
	this.setHorizontalSpeed = function(speed)
	{
		currentSpeed = speed;
	}
	
	this.getVerticalSpeed = function()
	{
		if(!inFullStall)
		{
			return (1 / (2 * OPTIMAL_GLIDE_RATIO)) * (Math.pow(this.getHorizontalSpeed() / SPEED_OPTIMAL_GLIDING, 2) + Math.pow(SPEED_OPTIMAL_GLIDING / this.getHorizontalSpeed(), 2)) * this.getHorizontalSpeed();
		}
		return verticalSpeed;
	}
	
	this.setVerticalSpeed = function(speed)
	{
		inFullStall = true;
		verticalSpeed = speed;
	}
	
	this.changeCurrentSpeed = function(change)
	{
		currentSpeed += change;
	}
	
	this.getCurrentGlideRatio = function()
	{
		return  1 / ((c1 * Math.pow(currentSpeed, 2)) + (c2 / Math.pow(currentSpeed, 2)));
	}
	
	this.getName = function()
	{
		return name;
	}
	
	this.isInFullStall = function()
	{
		return inFullStall;
	}
	
	this.setInFullStall = function(inFullStall)
	{
		inFullStall = inFullStall;
	}
	
	this.log = function()
	{
		console.log("<----- Logging called for Class 'Wing' ----->");
		console.log("Typeof: " + typeof this);
		console.log("Name: " + this.getName());
		console.log("Speed: " + this.getHorizontalSpeed());
		console.log("Verticalspeed: " + this.getVerticalSpeed());
		console.log("Glide Ratio: " + this.getCurrentGlideRatio());
		console.log("<----- End of Logging for Class 'Wing' ----->");
		console.log(" ");
	}
}
