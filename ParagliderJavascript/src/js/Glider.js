var leftWing = new Wing("Left");
var rightWing = new Wing("Right");

function Glider() {	}

Glider.prototype = {
		constructor: Glider,
		getPitchAngle: function() {
			return pilot.getPitchAngle();
		},
		getRollAngle: function() {
			return pilot.getRollAngle();
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
		makeNextStep: function() {
			pilot.calculatePosition(leftWing.getHorizontalSpeed(), rightWing.getHorizontalSpeed());
		},
		getAnglularVelocity: function() {
			return pilot.getAngularVelocity();
		},
		getIsOnPositiveSite: function() {
			return pilot.getIsOnPositiveSite();
		},
		setInFullStall: function(inFullStall) {
			pilot.setInFullStall(inFullStall);
		},
		getIsOnRightSite: function() {
			return pilot.getIsOnRightSite();
		},
		log: function() {	
			pilot.log();
		},
		simulateFullStall: function() {
			pilot.simulateFullStall();
		}
};
