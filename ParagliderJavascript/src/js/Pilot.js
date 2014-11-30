function Pilot() {
	this.weightOfPilot = 85;

	// Take a lock a the log. Why is z an object????
	this.ZERO_POSITION = new Vector3D(0, 0, constants.getLengthOfCord());
	this.ZERO_POINT = new Vector3D(0, 0, 0);

	// How to do Constructor overloading???
	this.currentPosition = new Vector3D(this.ZERO_POSITION.getX(), this.ZERO_POSITION
			.getY(), this.ZERO_POSITION.getZ());

	this.isOnPositiveSite = true;
	this.isOnRightSite = true;
	this.fForward = 0;
	this.fSideway = 0;

	// Javascript is singlethreaded anyway, no need for locks

	this.angularVelocity = 0;
	this.rollAngle = 0;
	this.inFullStall = false;
}

Pilot.prototype = {
	constructor : Pilot,
	getCurrentPosition : function() {
		return this.currentPosition;
	},
	setChangeInSpeed : function(speed) {
		this.fForward += (speed / constants.getTimeInterval())
				* this.weightOfPilot;
	},
	setChangeInSpeedY : function(speed) {
		if (speed > 0) {
			this.fSideway--;
		} else {
			this.fSideway++;
		}
	},
	getWeightOfPilot : function() {
		return this.weightOfPilot;
	},
	setWeightOfPilot : function(weight) {
		this.weightOfPilot = weight;
	},
	reset : function() {
		this.currentPosition = new Vector3D(this.ZERO_POSITION.getX(), this.ZERO_POSITION
				.getY(), this.ZERO_POSITION.getZ());
		this.fForward = 0;
		this.fSideway = 0;
	},
	calculatePosition : function(speedLeftWing, speedRightWing) {
		this.calculateX();
		this.calculateY(speedLeftWing, speedRightWing);
		this.calculateZ();
	},
	calculateY : function(speedLeftWing, speedRightWing) {
		this.calculateForcesInTheYAxis(speedLeftWing, speedRightWing);
		this.calculateChangeInYAxis();
	},
	calculateForcesInTheYAxis : function(speedLeftWing, speedRightWing) {
		var fg = this.weightOfPilot * constants.getGravitationalForce();
		var centrifugalForce;
		var fBackwards;
		this.calculateRollAngle();

		if (speedLeftWing == speedRightWing) {
			centrifugalForce = 0;
		} else {
			centrifugalForce = this.getCentrifugalForce(speedLeftWing,
					speedRightWing);

		}
		fBackwards = fg * Math.sin(this.getRollAngle());
		if (isNaN(fBackwards)) {
			fBackwards = 0;
		}
		if (Math.round(speedLeftWing - speedRightWing) == 0) {
			this.fSideway += this.calculateDamping(this.fSideway) * 4;
		}
		var fBackwardsRoundedAndPositive = Math.round(Math.sqrt(Math.pow(
				fBackwards, 2)));
		var centrifugalForceRoundedAndPositive = Math.round(Math.sqrt(Math.pow(
				centrifugalForce, 2)));
		
		if (fBackwardsRoundedAndPositive >= centrifugalForceRoundedAndPositive
				&& fBackwardsRoundedAndPositive <= centrifugalForceRoundedAndPositive + 5
				&& fBackwardsRoundedAndPositive > 5) {
			this.fSideway = 0;
		}
		if (this.isOnRightSite) {
			this.fSideway += fBackwards;
			this.fSideway += centrifugalForce;
		} else {
			this.fSideway -= fBackwards;
			this.fSideway += centrifugalForce;
		}
	},
	calculateChangeInYAxis: function() {
		var acceleration = (this.fSideway) / this.weightOfPilot;

		var changeY = (acceleration * Math.pow(constants.getTimeInterval(), 2)) / 2;

		this.currentPosition.setY(this.currentPosition.getY() - changeY);

	},
	calculateRollAngle : function() {
		var u = new Vector2D(this.ZERO_POSITION.getY() - this.ZERO_POINT.getY(),
				this.ZERO_POSITION.getZ() - this.ZERO_POINT.getZ());
		var v = new Vector2D(this.currentPosition.getY() - this.ZERO_POINT.getY(),
				this.currentPosition.getZ() - this.ZERO_POINT.getZ());

		var cosAngle = u.getAngleToVector2D(v);

		if (this.currentPosition.getY() < this.ZERO_POSITION.getY()) {
			this.isOnRightSite = false;
		} else {
			this.isOnRightSite = true;
		}

		this.setRollAngle(Math.acos(cosAngle));
	},
	getPitchAngle : function() {
		var u = new Vector2D(this.ZERO_POSITION.getX() - this.ZERO_POINT.getX(),
				this.ZERO_POSITION.getZ() - this.ZERO_POINT.getZ());
		var v = new Vector2D(this.currentPosition.getX() - this.ZERO_POINT.getX(),
				this.currentPosition.getZ() - this.ZERO_POINT.getZ());

		var cosAngle = u.getAngleToVector2D(v);

		if (this.currentPosition.getX() < this.ZERO_POSITION.getX()) {
			this.isOnPositiveSite = false;
		} else {
			this.isOnPositiveSite = true;
		}

		return Math.acos(cosAngle);
	},
	getCentrifugalForce : function(speedLeftWing, speedRightWing) {
		var pilotPath = (speedLeftWing + speedRightWing) / 2
				* constants.getTimeInterval();
		var radius = this.getCurveRadius(speedLeftWing * constants.getTimeInterval(),
				speedRightWing * constants.getTimeInterval())
				- (constants.getGliderWingspan() / 2);
		var alpha = (pilotPath * 360) / (2 * radius * Math.PI);
		alpha = constants.convertDegreeToRadian(alpha);
		this.angularVelocity = alpha / constants.getTimeInterval(); 

		return this.weightOfPilot * Math.pow(this.angularVelocity, 2) * radius;
	},
	calculateForcesInTheXAxis: function() {
		var fg = this.weightOfPilot * constants.getGravitationalForce();
		var fBackwards = fg * Math.sin(this.getPitchAngle());

		if (this.isOnPositiveSite) {
			this.fForward += fBackwards;
		} else {
			this.fForward -= fBackwards;
		}
		if (this.inFullStall) {
			this.fForward += this.calculateDamping(this.fForward) * 8;
		} else {
			this.fForward += this.calculateDamping(this.fForward)*4;
		}
	},
	calculateX: function() {
		this.calculateForcesInTheXAxis();
		this.calculateChangeInXAxis();
	},
	calculateChangeInXAxis: function() {
		var acceleration = (this.fForward) / this.weightOfPilot; 
		var changeX = (acceleration * Math.pow(constants.getTimeInterval(), 2)) / 2;
		this.currentPosition.setX(this.currentPosition.getX() - changeX);
	},
	calculateDamping: function(force) {
		var acceleration = force / this.weightOfPilot;
		return -acceleration * constants.getDampingFactor()* this.weightOfPilot;
	},
	calculateZ: function() {
		var x = this.currentPosition.getX() - this.ZERO_POSITION.getX();
		var y = this.currentPosition.getY() - this.ZERO_POSITION.getY();
		var a = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		var z = Math.sqrt(Math.pow(constants.getLengthOfCord(), 2) - Math.pow(a, 2));
		this.currentPosition.setZ(z);
	},
	getCurveRadius: function(pathLeft, pathRight) {
		var xa = pathRight;
		var xb = pathLeft;
		var rb = constants.getGliderWingspan();
		var ra = (rb * xb) / (xa - xb);
		return (ra + rb);
	},
	getAngularVelocity: function() {
		return this.angularVelocity;
	},
	getRollAngle: function() {
		return this.rollAngle;
	},
	setRollAngle: function(rollAngle) {
		this.rollAngle = rollAngle;
	},
	getIsOnPositiveSite: function() {
		return this.isOnPositiveSite;
	},
	getIsOnRightSite: function() {
		return this.isOnRightSite;
	},
	setInFullStall: function(inFullStall) {
		this.inFullStall = inFullStall;
		
	},
	simulateFullStall: function() {
			this.fForward -= 100;
			this.fSideway -= 100;
	}
};
