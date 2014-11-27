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

var gamepad = undefined;
var leftStick = 0;
var rightStick = 0;
var maxSpeed = 34;

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
	var value = document.getElementById("selectBox").value;
	
	ge.getOptions().setFlyToSpeed(ge.SPEED_TELEPORT);
	ge.getOptions().setMouseNavigationEnabled(true);
	
	
	var latitude = GetURLParameter('Latitude');
	var longitude = GetURLParameter('Longitude');
	
	if(latitude !== undefined && longitude !== undefined)
	{
		
	}

	if(value=="NewYork") {
		camera.setLatitude(40.697130);
		camera.setLongitude(-74.024031);
		camera.setTilt(90);
		camera.setAltitude(187);
		currentHeading=45;
		
	}
	else if (value == "Tschiertschen") {
		camera.setLatitude(46.795046);
		camera.setLongitude(9.631149);
		camera.setTilt(80);
		camera.setAltitude(2153);
		currentHeading = -45;
	}
	else if (value == "Hamptons") {
		camera.setLatitude(40.816297);
		camera.setLongitude(-72.563624);
		camera.setTilt(90);
		camera.setAltitude(50);
		currentHeading = 75;
		
	}
	else if(value == "Maagan Michael")
	{
		camera.setLatitude(32.5740317);
		camera.setLongitude(34.9524011);
		camera.setTilt(90);
		camera.setAltitude(200);
		currentHeading = -90;
	}
	else if(value == "Mollis") {
		camera.setLatitude(47.06763735);
		camera.setLongitude(9.10409027);
		camera.setTilt(90);
		camera.setAltitude(2000);
		currentHeading = -90;
	}
	else if(value = "Huesliberg") {
		camera.setLatitude(46.7714576);
		camera.setLongitude(9.62140767);
		camera.setTilt(90);
		camera.setAltitude(3000);
		currentHeading = 0;
	}
		
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

	ge.getOptions().setMouseNavigationEnabled(false);
	if (!moving) {
		nextStep();
	}

}

function nextStep() {

	glider.makeNextStep();
	/*setController();
	if(leftStick > 0)
	{
		leftBreak(leftStick*maxSpeed);
	}
	else
	{
		leftBreak(0);
	}
	if(rightStick > 0)
	{
		rightBreak(rightStick*maxSpeed);
	}
	else
	{
		rightBreak(0);
	}*/

	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_ABSOLUTE);
	setPosition(camera);
	setHeading(camera);
	setRoll(camera);
	setTilt(camera);
//	log(camera);
	document.getElementById('horizontalspeed').innerHTML = Math.round(3.6 *glider.getHorizontalSpeed());
	document.getElementById('verticalspeed').innerHTML = -Math.round(glider.getVerticalSpeed()*10)/10;
	document.getElementById('alt1').innerHTML = Math.round(camera.getAltitude());

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
	camera.setHeading(camera.getHeading() + alpha);
	currentHeading = camera.getHeading();
	setCompass(-camera.getHeading());	
}

function setRoll(camera) {
	var angle = constants.convertRadianToDegree(glider.getRollAngle());

	if(!glider.getIsOnRightSite())
	{
		angle = -angle;
	}
	camera.setRoll(angle);
}

function setTilt(camera) {
	var angle = constants.convertRadianToDegree(glider.getPitchAngle());
	if(!glider.getIsOnPositiveSite()) {
		angle = -angle;
	}
	camera.setTilt(angle+90);
}

function stopMovement() {
	clearTimeout(timer);
	moving = false;
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
function roll(rollAngle) {
	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_ABSOLUTE);

	camera.setRoll(camera.getRoll() + rollAngle);

	ge.getView().setAbstractView(camera);
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
		console.log(value);
	};
})();

var bothBreaks = (function() {
	var oldValue = 0;
	return function(value) {
		if (value >= 0.90 * $( "#slider-both" ).slider( "option", "max" )) {
			glider.setInFullStall(true);
		} else {
			glider.setInFullStall(false);
		}

		glider.changeSpeed(-((value - oldValue) / 3.6),
				-((value - oldValue) / 3.6));
		oldValue = value;
	};
})();

function log(camera) {
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
	console.log("Latitude: "+ camera.getLatitude());
	console.log("Altitude: "+ camera.getAltitude());
	console.log("Heading: "+ camera.getHeading());
	console.log(" ");
}

function initStartPosition(value) {
	stopMovement();
	initCamera();
}
function setController() {

	gamepad = navigator.getGamepads && navigator.getGamepads()[0];
	if(gamepad != undefined)
	{
		leftStick = Math.round(10*gamepad.axes[1])/10;
		rightStick = Math.round(10*gamepad.axes[3])/10;
	}
}

google.setOnLoadCallback(init);
