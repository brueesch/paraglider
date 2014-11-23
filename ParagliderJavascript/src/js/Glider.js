var leftWing = new Wing("Left");
var rightWing = new Wing("Right");
var yawAngle = 0;

function Glider() {	}

Glider.prototype = {
		constructor: Glider,
		getPilotPosition: function () {
			return pilot.getCurrentPosition();
		},
		getPitchAngle: function() {
			return pilot.getPitchAngle();
		},
		getRollAngle: function() {
			return pilot.getRollAngle();
		},
		getYawAngle: function() {
			this.calculateYawAngle();
			return yawAngle;
		},
		getHorizontalSpeed: function() {
			return (leftWing.getHorizontalSpeed() + rightWing.getHorizontalSpeed())/2;
		},	
		getVerticalSpeed: function() {
			return (leftWing.getVerticalSpeed() + rightWing.getVerticalSpeed())/2;
		},
		getCurrentGlideRatio: function() {
			return (leftWing.getCurrentGlideRatio() + rightWing.getCurrentGlideRatio())/2;
		},
		changeSpeed: function (msLeft, msRight) {
			leftWing.changeCurrentSpeed(msLeft);
			rightWing.changeCurrentSpeed(msRight);
			pilot.setChangeInSpeed((msLeft+msRight)/2);
			pilot.setChangeInSpeedY(msLeft - msRight);
		},
		reset: function() {
			yawAngle = 0;
			pilot.reset();
		},
		calculateYawAngle: function() {
			var pathLeft = leftWing.getHorizontalSpeed() * constants.getTimeInterval();
			var pathRight = rightWing.getHorizontalSpeed() * constants.getTimeInterval();
				
			if(pathLeft != pathRight) {
				var xa = pathLeft;
				var xb = pathRight;
				var rb = constants.getGliderWingspan();
				var ra = (xa*rb)/(xb-xa);
				var radius = ra + rb;
				yawAngle += Math.toDegrees(xb/radius);
			}
		},
		makeNextStep: function() {
			pilot.calculatePosition(leftWing.getHorizontalSpeed(), rightWing.getHorizontalSpeed());
		},
		getAnglularVelocity: function() {
			return pilot.getAngularVelocity();
		},
		isOnPositiveSite: function() {
			return pilot.isOnPositiveSite();
		},
		setInFullStall: function(inFullStall) {
			pilot.setInFullStall(inFullStall);
		},
		isOnRightSite: function() {
			return pilot.isOnRightSite();
		},
		log: function() {	
			pilot.log();
		}
};
