function Pilot() {
	this.weightOfPilot = 85;
	this.ZERO_POSITION = new Vector3D(0, 0, constants.getLengthOfCord());
	this.ZERO_POINT = new Vector3D(0, 0, 0);
	this.currentPosition = new Vector3D(this.ZERO_POSITION.getX(), this.ZERO_POSITION
			.getY(), this.ZERO_POSITION.getZ());
	this.isOnPositiveSite = true;
	this.isOnRightSite = true;
	this.fForward = 0;
	this.fSideway = 0;
	this.timeCounter = 0;

	this.angularVelocity = 0;
	this.rollAngle = 0;
	this.inFullStall = false;
}

Pilot.prototype = {
	constructor : Pilot,
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

	/*
	********************************************************************
											Calculation of the x-axis
	********************************************************************
	*/
	calculateX: function() {
		this.calculateForcesInTheXAxis();
		this.setNewPositionInXAxis();
	},
	calculateForcesInTheXAxis: function() {
		var fg = this.weightOfPilot * constants.getGravitationalForce();
		var fBackwards = fg * Math.sin(this.getPitchAngle());
		this.addForces();
		this.addDamping();
	},
	addForces : function() {
		this.determinePositiveSite();
		if (this.isOnPositiveSite) {
			this.fForward += fBackwards;
		} else {
			this.fForward -= fBackwards;
		}
	},
	addDamping : function() {
		if (this.inFullStall) {
			this.fForward += this.calculateDamping(this.fForward) * 8;
		} else {
			this.fForward += this.calculateDamping(this.fForward)*4;
		}
	},
	setNewPositionInXAxis : function() {
		var acceleration = (this.fForward) / this.weightOfPilot;
		var changeX = (acceleration * Math.pow(constants.getTimeInterval(), 2)) / 2;
		this.currentPosition.setX(this.currentPosition.getX() - changeX);
	},

	/*
	********************************************************************
	Calculation of the y-axis
	********************************************************************
	*/
	calculateY : function(speedLeftWing, speedRightWing) {
		this.calculateForcesInTheYAxis(speedLeftWing, speedRightWing);
		this.calculateChangeInYAxis();
	},
	calculateForcesInTheYAxis : function(speedLeftWing, speedRightWing) {
		var fg = this.weightOfPilot * constants.getGravitationalForce();
		var centrifugalForce = determineCentrifugalForce(speedLeftWing, speedRightWing);
		var fBackwards = determineFBackwards();

		if (Math.round(speedLeftWing - speedRightWing) == 0) {
			this.fSideway += this.calculateDamping(this.fSideway) * 6;
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
	determineCentrifugalForce : function(speedLeftWing, speedRightWing) {
		var result;
		if (speedLeftWing == speedRightWing) {
			result = 0;
		} else {
			result = this.getCentrifugalForce(speedLeftWing,
				speedRightWing);
		}
		return result;
	},
	determineFBackwards : function() {
		this.calculateRollAngle();
		var result = fg * Math.sin(this.getRollAngle());
		if (isNaN(result)) {
			result = 0;
		}
		return result;
	},


	/*
	********************************************************************
	Calculation of the z-axis
	********************************************************************
	*/
	calculateZ: function() {
		var x = this.currentPosition.getX() - this.ZERO_POSITION.getX();
		var y = this.currentPosition.getY() - this.ZERO_POSITION.getY();
		var a = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		var z = Math.sqrt(Math.pow(constants.getLengthOfCord(), 2) - Math.pow(a, 2));
		this.currentPosition.setZ(z);
	},

	/*
	********************************************************************
	Calculation Angles
	********************************************************************
	*/
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
		return this.calculatePitchAngle();
	},
	calculatePitchAngle : function() {
		var u = new Vector2D(this.ZERO_POSITION.getX() - this.ZERO_POINT.getX(),
						this.ZERO_POSITION.getZ() - this.ZERO_POINT.getZ());
		var v = new Vector2D(this.currentPosition.getX() - this.ZERO_POINT.getX(),
						this.currentPosition.getZ() - this.ZERO_POINT.getZ());
		var cosAngle = u.getAngleToVector2D(v);

		return Math.acos(cosAngle);
	},
	determinePositiveSite : function() {
		if (this.currentPosition.getX() < this.ZERO_POSITION.getX()) {
			this.isOnPositiveSite = false;
		} else {
			this.isOnPositiveSite = true;
		}
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

	calculateDamping: function(force) {
		var acceleration = force / this.weightOfPilot;
		return -acceleration * constants.getDampingFactor()* this.weightOfPilot;
	},
	getCurveRadius: function(pathLeft, pathRight) {
		var xa = pathRight;
		var xb = pathLeft;
		var rb = constants.getGliderWingspan();
		var ra = (rb * xb) / (xa - xb);
		return (ra + rb);
	},
	simulateFullStall: function() {
		this.timeCounter += 1;
		if(this.timeCounter <20) {
			this.fForward -= 3000;
		} else if( this.timeCounter >=20 && this.timeCounter < 50) {
			this.fForward += 3000;
		} else if(this.timeCounter >= 50 && this.timeCounter < 80) {
			this.fForward -= 3000;
		}
		else if (this.timeCounter >=80 && this.timeCounter <100 ) {
			this.fForward += 3000;
		}


		if(this.timeCounter <30) {
			this.fSideway += 5000;
		} else if( this.timeCounter >=30 && this.timeCounter < 70) {
			this.fSideway -= 5000;
		} else if(this.timeCounter >= 70 && this.timeCounter < 110) {
			this.fSideway += 5000;
		}
		else if (this.timeCounter >=110 && this.timeCounter <140 ) {
			this.fSideway -= 5000;
		}
		else {
			this.timeCounter = 0;
		}
	},

	/*
	********************************************************************
	Setter / Getter functions
	********************************************************************
	*/
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
		if(!inFullStall) {
			this.fForward += 45000;
		}

	},
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
};
