function Pilot()
{
	var weightOfPilot = 85;
	
	//Take a lock a the log. Why is z an object????
	var ZERO_POSITION = new Vector3D(0,0,Constants.LENGTH_OF_CORD);
	var ZERO_POINT = new Vector3D(0,0,0);
	
	//How to do Constructor overloading???
	var currentPosition = new Vector3D(ZERO_POSITION.getX(),ZERO_POSITION.getY(),ZERO_POSITION.getZ());
	
	var isOnPositiveSite = true;
	var isOnRightSite = true;
	var fForward = 0;
	var fSideway = 0;
	
	//Javascript is singlethreaded anyway, no need for locks
	
	var angularVelocity = 0;
	var rollAngle = 0;
	var inFullStall = false;
	
	this.getCurrentPosition = function()
	{
		return currentPosition;
	}
	
	this.setChangeInSpeed = function(speed)
	{
		fForward += (speed/Constants.TIME_INTERVALL) * weightOfPilot;
	}
	
	this.setChangeInSpeedY = new(speed)
	{
		if(speed > 0)
		{
			fSideway--;
		}
		else
		{
			fSideway++;
		}
	}
	
	this.getWeightOfPilot = function()
	{
		return weightOfPilot;
	}
	
	this.setWeightOfPilot = function(weight)
	{
		weightOfPilot = weight;
	}
	
	this.log = function()
	{
		console.log("<----- Logging called for Class 'Pilot' ----->");
		console.log("Typeof: " + typeof this);
		console.log("weightOfPilot: " + weightOfPilot);
		console.log("ZERO_POSITION: " + ZERO_POSITION.log());
		console.log("<----- End of Logging for Class 'Vector3D' ----->");
		console.log(" ");
	}
}

//TODO