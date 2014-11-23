function Constants() {
	this.GLIDER_WINGSPAN = 12;
	this.GRAVITATIONAL_FORCE = 9.81;
	this.LENGTH_OF_CORD = 7.68;
	this.TIME_INTERVAL = 0.01;
	this.DAMPING_FACTOR = 0.1 * this.TIME_INTERVAL;
}

Constants.prototype = {
	constructor : Constants,
	getGliderWingspan : function() {
		return this.GLIDER_WINGSPAN;
	},
	getGravitationalForce : function() {
		return this.GRAVITATIONAL_FORCE;
	},
	getLengthOfCord : function() {
		return this.LENGTH_OF_CORD;
	},
	getTimeInterval : function() {
		return this.TIME_INTERVAL;
	},
	getDampingFactor : function() {
		return this.DAMPING_FACTOR;
	},
	convertKmhToMs : function(kmh) {
		return kmh / 3.6;
	},
	convertMsToKmh : function(ms) {
		return ms * 3.6;
	},
	convertSecToMs : function(sec) {
		return sec * 1000;
	},
	convertMsToSec : function(ms) {
		return ms / 1000;
	},
	convertDegreeToRadian: function(degree) {
		return ((2*Math.PI)/360)*degree;
	},
	convertRadianToDegree: function(radian) {
		return (360/(2*Math.PI))*radian;
	}
};