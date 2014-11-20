function Constants
{
	//How do make those things readonly???
	this.GLIDER_WINGSPAN = 12;
	this.GRAVITATIONAL_FORCE = 9.81;
	this.LENGTH_OF_CORD = 7.68;
	this.TIME_INTERVALL = 0.01;
	this.DAMPING_FACTOR = 0.1 * this.TIME_INTERVALL;
	
	this.convertKmhToMs = function(kmh)
	{
		return kmh / 3.6;
	}
	this.convertMsToKmh = function(ms)
	{
		return ms * 3.6;
	}
	this.convertSecToMs = function(sec)
	{
		return sec * 1000;
	}
	this.convertMsToSec = function(ms)
	{
		return ms / 1000;
	}
} 