/*
 * -------------------------------------------------------
 * Initializing all global variables
 * -------------------------------------------------------
 */

var ge;
var timer;
var speedChange = 0.00001;
var moving = false;
var glider = new Glider();
var constants = new Constants();
var pilot = new Pilot();
var currentHeading = -45;

/*
 * ------------------------------------------------------- Initializing Google
 * Earth -------------------------------------------------------
 */

google.load("earth", "1", false);

// Creates a new instance of google earth.
function init() {
	// Param: div id where the earth will appear, function to call by success,
	// function to call by failure.
	google.earth.createInstance('map3d', initCB, failureCB);

	var buttonSize = 108;
	var left = 550;

	createNativeHTMLButton(left + (1 * buttonSize), 10, buttonSize, buttonSize,
			'left', rollLeft);
	createNativeHTMLButton(left + (2 * buttonSize), 10, buttonSize, buttonSize,
			'slower', slower);
	createNativeHTMLButton(left + (3 * buttonSize), 10, buttonSize, buttonSize,
			'play', move);
	createNativeHTMLButton(left + (4 * buttonSize), 10, buttonSize, buttonSize,
			'faster', faster);
	createNativeHTMLButton(left + (5 * buttonSize), 10, buttonSize, buttonSize,
			'right', rollRight);
	createNativeHTMLButton(left + (7 * buttonSize), 10, buttonSize, buttonSize,
			'pause', stopMovement);
	createNativeHTMLButton(left + (8 * buttonSize), 10, buttonSize, buttonSize,
			'stop', reset);

}

// initialises google earth and set it's visibility to true.
function initCB(instance) {
	ge = instance;
	ge.getWindow().setVisibility(true);
	initCamera();
}

function failureCB(errorCode) {
	alert(errorCode);
}

/*
 * ------------------------------------------------------- Setting up the Camera
 * -------------------------------------------------------
 */

// Coordinates of New York City: Latitude: 40.681682 Longitude: -74.038428
// COordinates of Weisshorn: Latitude: 46.789676 Longitude: 9.638993
function initCamera() {
	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_ABSOLUTE);

	ge.getOptions().setFlyToSpeed(ge.SPEED_TELEPORT);
	ge.getOptions().setMouseNavigationEnabled(false);
//	camera.setLatitude(40.681682);
//	camera.setLongitude(-74.038428);
	camera.setLatitude(46.795046);
	camera.setLongitude(9.631149);
	camera.setTilt(80);
	camera.setAltitude(2153);
	camera.setHeading(currentHeading);
	camera.setRoll(0);

	ge.getView().setAbstractView(camera);
	ge.getLayerRoot().enableLayerById(ge.LAYER_TERRAIN, true);
	ge.getLayerRoot().enableLayerById(ge.LAYER_TREES, true);
	ge.getLayerRoot().enableLayerById(ge.LAYER_BUILDINGS, true);
	// ge.getSun().setVisibility(true);
	ge.getOptions().setAtmosphereVisibility(true);
}

/*
 * ------------------------------------------------------- In this section are
 * test functions to determine how the manipulation of the camera works.
 * -------------------------------------------------------
 */

function move() {
	if (!moving) {
		nextStep();
	}

}

function nextStep() {

	glider.makeNextStep();
	// log();
	

	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_ABSOLUTE);
	setPosition(camera);
	setHeading(camera);
	setRoll(camera);
	setTilt(camera);
	
	

	ge.getView().setAbstractView(camera);

	timer = setTimeout("nextStep()", constants.getTimeInterval());
	moving = true;
}

function setPosition(camera) {
	setHorizontalPosition(camera);
	setVerticalPosition(camera);
}

function setHorizontalPosition(camera) {
	var v = glider.getHorizontalSpeed();
	var s = v * constants.getTimeInterval();
	var changeLatitude = (s * Math.cos(constants
			.convertDegreeToRadian(currentHeading)))
			/ constants.getDistanceBetweenLatitude();
	var changeLongitude = (s * Math.sin(constants
			.convertDegreeToRadian(currentHeading)))
			/ (constants.getDistanceBetweenLatitude() * Math.cos(constants
					.convertDegreeToRadian(camera.getLatitude())));
//	changeLatitude *= 10;
//	changeLongitude *= 10;
	camera.setLatitude(camera.getLatitude() + changeLatitude);
	camera.setLongitude(camera.getLongitude() + changeLongitude);
}

function setVerticalPosition(camera) {
	var v = glider.getVerticalSpeed();
	var s = v * constants.getTimeInterval();
//	s *= 10;
	camera.setAltitude(camera.getAltitude() - s);
}

function setHeading(camera) {
	var angularVelocity = pilot.getAngularVelocity();
	var alpha = angularVelocity * constants.getTimeInterval();
	alpha = constants.convertRadianToDegree(alpha);
	if(document.getElementById("leftBreak").value < 1 && document.getElementById("rightBreak").value < 1) {
		alpha = 0;
	}
	camera.setHeading(camera.getHeading() + alpha);
	currentHeading = camera.getHeading();
	console.log("leftBreak: "+ document.getElementById("leftBreak").value + " rightBreak: "+ document.getElementById("rightBreak").value + " alpha: "+alpha);
}

function setRoll(camera) {

}

function setTilt(camera) {

}

function stopMovement() {
	clearTimeout(timer);
	moving = false;
	showWing();
}

function faster() {
	speedChange += 0.0001;
}

function slower() {
	speedChange -= 0.0001;

}

function reset() {
	stopMovement();
	speedChange = 0.0001;
	initCamera();
}
function rollLeft() {
	roll(15);
}
function rollRight() {
	roll(-15);
}

function roll(rollAngle) {
	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_ABSOLUTE);

	camera.setRoll(camera.getRoll() + rollAngle);

	ge.getView().setAbstractView(camera);
}

function showWing() {

	var cat = new Mammal("Cat", "claws");
	var dog = new Mammal("Dog", "a wet nose");

	alert(cat.species);

}

var rightBreak = (function() {
	var oldValue = 0;
	return function(value) {
		glider.changeSpeed(-((value - oldValue) / 3.6), 0);
		oldValue = value;
	};
})();

var leftBreak = (function() {
	var oldValue = 0;
	return function(value) {
		glider.changeSpeed(0, -((value - oldValue) / 3.6));
		oldValue = value;
	};
})();

var bothBreaks = (function() {
	var oldValue = 0;
	return function(value) {
		if (value >= 0.90 * (document.getElementById("bothBreaks").max)) {
			glider.setInFullStall(true);
		} else {
			glider.setInFullStall(false);
		}

		glider.changeSpeed(-((value - oldValue) / 3.6),
				-((value - oldValue) / 3.6));
		oldValue = value;
	};
})();

function log() {
	console.log("Typeof: " + typeof pilot);
	console.log("weightOfPilot: " + pilot.weightOfPilot);
	console.log("Zero Position: " + pilot.ZERO_POSITION);
	console.log("Zero Point: " + pilot.ZERO_POINT);
	console.log("isOnPositiveSite: " + pilot.isOnPositiveSite);
	console.log("isOnRightSite: " + pilot.isOnRightSite);
	console.log("fForward: " + pilot.fForward);
	console.log("fSideway: " + pilot.fSideway);
	console.log("angularVelocity: " + pilot.angularVelocity);
	console.log("rollAngle: " + pilot.rollAngle);
	console.log("pitchAngle: " + pilot.getPitchAngle());
	console.log("inFullStall: " + pilot.inFullStall);
	console.log("Horizontal Speed: " + glider.getHorizontalSpeed());
	console.log("Vertical Speed: " + glider.getVerticalSpeed());
	console.log("Gliding: " + glider.getCurrentGlideRatio());
	console.log("longitude: "+camera.getLongitude());
	console.log("Latitude: "+ camera.getLatitude() +" altitude: "+ camera.getAltitude());
	console.log("Heading: "+ camera.getHeading());
	console.log(" ");
}

google.setOnLoadCallback(init);
