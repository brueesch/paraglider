var Pilot = function ()
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
	
	this.setChangeInSpeedY = function(speed)
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
	
	this.reset = function()
	{
		currentPosition = new Vector3D(ZERO_POSITION.getX(),ZERO_POSITION.getY(),ZERO_POSITION.getZ());
		fForward = 0;
		fSideway = 0;
	}
	
	this.calculatePosition = function(speedLeftWing,speedRightWing)
	{
		calculateX();
		calculateY(speedLeftWing, speedRightWing);
		calculateZ();
	}
	
	var calculateY = function(speedLeftWing, speedRightWing) 
	{
		calculateForcesInTheYAxis(speedLeftWing, speedRightWing);
		calculateChangeInYAxis();
	}
	
	var calculateForcesInTheYAxis = function(speedLeftWing,speedRightWing)
	{
		var fg = weightOfPilot * Constants.GRAVITATIONAL_FORCE;
		var centrifugalForce;
		var fBackwards;
		calculateRollAngle();
		
		if (speedLeftWing == speedRightWing) 
		{
			centrifugalForce = 0;
		} 
		else 
		{
			centrifugalForce = getCentrifugalForce(speedLeftWing, speedRightWing);
			
		}
		fBackwards = fg * Math.sin(getRollAngle());
		if(isNaN(fBackwards)) 
		{
			fBackwards = 0;
		}
		if (Math.round(speedLeftWing - speedRightWing) == 0) 
		{
			fSideway += calculateDamping(fSideway) * 4;
		}
		var fBackwardsRoundedAndPositive = Math.round(Math.sqrt(Math.pow(fBackwards, 2)));
		var centrifugalForceRoundedAndPositive = Math.round(Math.sqrt(Math.pow(centrifugalForce, 2)));
		
		if (fBackwardsRoundedAndPositive >= centrifugalForceRoundedAndPositive && fBackwardsRoundedAndPositive <= centrifugalForceRoundedAndPositive + 5 && fBackwardsRoundedAndPositive > 5) 
		{
			fSideway = 0;
		}
		if (isOnRightSite) 
		{
			fSideway += fBackwards;
			fSideway += centrifugalForce;
		} else 
		{
			fSideway -= fBackwards;
			fSideway += centrifugalForce;
		}
	}
	
	var calculateRollAngle = function()
	{
		var u = new Vector2D(ZERO_POSITION.getY() - ZERO_POINT.getY(), ZERO_POSITION.getZ() - ZERO_POINT.getZ());
		var v = new Vector2D(currentPosition.getY() - ZERO_POINT.getY(), currentPosition.getZ() - ZERO_POINT.getZ());
		
		var cosAngle = u.getAngleToVector2D(v);
		
		if (currentPosition.getY() < ZERO_POSITION.getY()) 
		{
			isOnRightSite = false;
		} 
		else 
		{
			isOnRightSite = true;
		}

		setRollAngle(Math.acos(cosAngle));
	}
	
	this.getPitchAngle = function()
	{
		var u = new Vector2D(ZERO_POSITION.getX() - ZERO_POINT.getX(), ZERO_POSITION.getZ() - ZERO_POINT.getZ());
		var v = new Vector2D(currentPosition.getX() - ZERO_POINT.getX(), currentPosition.getZ() - ZERO_POINT.getZ());

		var cosAngle = u.getAngleToVector2D(v);

		if (currentPosition.getX() < ZERO_POSITION.getX()) 
		{
			isOnPositiveSite = false;
		} 
		else 
		{
			isOnPositiveSite = true;
		}

		return Math.acos(cosAngle);
	}
	
	this.calculateAngleOfHighestPoint = function(speedLeftWing,speedRightWing)
	{
		if (speedLeftWing == speedRightWing) 
		{
			angularVelocity = 0;
			return 0;
		}
		
		var fCen = getCentrifugalForce(speedLeftWing, speedRightWing);
		var fG = Constants.GRAVITATIONAL_FORCE * weightOfPilot;
		var resultAngle = Math.atan(fCen / fG);
		setRollAngle(resultAngle);
		return resultAngle;
	}
	
	var getCentrifugalForce = function(speedLeftWing, speedRightWing)
	{
		var pilotPath = (speedLeftWing + speedRightWing) / 2 * Constants.TIME_INTERVALL;
		var radius = getCurveRadius(speedLeftWing * Constants.TIME_INTERVALL, speedRightWing * Constants.TIME_INTERVALL)- (Constants.GLIDER_WINGSPAN / 2);
		var alpha = (pilotPath * 360) / (2 * radius * Math.PI);
		alpha = Math.toRadians(alpha);
		angularVelocity = alpha / Constants.TIME_INTERVALL;

		return weightOfPilot * Math.pow(angularVelocity, 2) * radius;
	}
	var calculateX = function() 
	{
		this.calculateForcesInTheXAxis();
		calculateChangeInXAxis();
	}.bind(this);
	
	var calculateChangeInXAxis = function() 
	{
		var acceleration = (fForward) / weightOfPilot;
		var changeX = (acceleration * Math.pow(Constants.TIME_INTERVALL, 2)) / 2;
		currentPosition.setX(currentPosition.getX() - changeX);
	}
	var calculateForcesInTheXAxis = function() 
	{
		var fg = weightOfPilot * Constants.GRAVITATIONAL_FORCE;
		var fBackwards = fg * Math.sin(this.getPitchAngle());

		if (isOnPositiveSite) 
		{
			fForward += fBackwards;
		} 
		else 
		{
			fForward -= fBackwards;
		}
		if(inFullStall) {
			fForward += calculateDamping(fForward)*8;
		} else {
			fForward += calculateDamping(fForward);
		}
	}
	var calculateDamping = function(force) 
	{
		var acceleration = force / weightOfPilot;
		return -acceleration * Constants.DAMPING_FACTOR * weightOfPilot;
	}
	var calculateZ = function() 
	{
		var x = currentPosition.getX() - ZERO_POSITION.getX();
		var y = currentPosition.getY() - ZERO_POSITION.getY();
		var a = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		var z = Math.sqrt(Math.pow(Constants.LENGTH_OF_CORD, 2)- Math.pow(a, 2));
		currentPosition.setZ(z);
	}
	var getCurveRadius = function(pathLeft, pathRight)
	{
		var xa = pathRight;
		var xb = pathLeft;
		var rb = Constants.GLIDER_WINGSPAN;
		var ra = (rb * xb) / (xa - xb);
		return (ra + rb);
	}
	
	this.getAngularVelocity = function()
	{
		return angularVelocity;
	}
	this.getRollAngle = function()
	{
		return rollAngle;
	}
	this.setRollAngle = function(rollAngle) 
	{
		rollAngle = rollAngle;
	}
	this.isOnPositiveSite = function()
	{
		return isOnPositiveSite;
	}
	this.isOnRightSite = function()
	{
		return isOnRightSite;
	}
	this.setInFullStall = function(boolean) 
	{
		inFullStall = boolean;
	}
	
	this.log = function()
	{
		console.log("<----- Logging called for Class 'Pilot' ----->");
		console.log("Typeof: " + typeof this);
		console.log("weightOfPilot: " + weightOfPilot);
		ZERO_POSITION.log("ZERO_POSITION");
		ZERO_POINT.log("ZERO_POINT");
		console.log("isOnPositiveSite: " + isOnPositiveSite);
		console.log("isOnRightSite: " + isOnRightSite);
		console.log("fForward: " + fForward);
		console.log("fSideway: " + fSideway);
		console.log("angularVelocity: " + angularVelocity);
		console.log("rollAngle: " + rollAngle);
		console.log("pitchAngle: " + this.getPitchAngle());
		console.log("inFullStall: " + inFullStall);
		console.log("<----- End of Logging for Class 'Pilot' ----->");
		console.log(" ");
	}
}

//TODO