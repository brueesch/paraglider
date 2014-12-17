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
var fullStall = false;
var direction = true;

var gamepad = undefined;
var leftStick = 0;
var rightStick = 0;
var maxSpeed = 34;
var myAudio = undefined;

/*
 * ------------------------------------------------------- 
 * Initializing Google Earth and the Controller
 * -------------------------------------------------------
 */

google.load("earth", "1", false);


function init() {

	google.earth.createInstance('map3d', initCB, failureCB);

	$("#slider-left").on("oninput", function() {

		leftBreak($("#slider-left").val());
	});
	$("#slider-right").on("oninput", function() {

		rightBreak($("#slider-right").val());
	});

	myAudio = new Audio('audio/wind.mp3');
	if (typeof myAudio.loop == 'boolean') {
		myAudio.loop = true;
	} else {
		myAudio.addEventListener('ended', function() {
			this.currentTime = 0;
			this.play();
		}, false);
	}

	gamepad = navigator.getGamepads()[0];
}

function initCB(instance) {
	ge = instance;
	ge.getWindow().setVisibility(true);
	initCamera();
}

function failureCB(errorCode) {
	alert(errorCode);
}

/*
 * ------------------------------------------------------- 
 * Setting up the Camera
 * -------------------------------------------------------
 */

function initCamera() {
	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_ABSOLUTE);
	ge.getOptions().setFlyToSpeed(ge.SPEED_TELEPORT);
	ge.getOptions().setMouseNavigationEnabled(true);

	var name = unescape(GetURLParameter('Name'));
	var latitude = parseFloat(GetURLParameter('Latitude'));
	var longitude = parseFloat(GetURLParameter('Longitude'));

	if (!isNaN(latitude) && !isNaN(longitude) && name !== 'undefined') {
		camera.setLatitude(latitude);
		camera.setLongitude(longitude);
		document.getElementById('startPoint').innerHTML = name;
		camera.setTilt(85);
		camera.setAltitude(187);
		currentHeading = 45;

	} else {
		camera.setLatitude(46.795046);
		camera.setLongitude(9.631149);
		document.getElementById('startPoint').innerHTML = "Tschiertschen";
		camera.setTilt(85);
		camera.setAltitude(2153);
		currentHeading = -45;
	}

	camera.setHeading(currentHeading);
	camera.setRoll(0);

	ge.getView().setAbstractView(camera);
	ge.getLayerRoot().enableLayerById(ge.LAYER_TERRAIN, true);
	ge.getLayerRoot().enableLayerById(ge.LAYER_TREES, true);
	ge.getLayerRoot().enableLayerById(ge.LAYER_BUILDINGS, true);
//	ge.getSun().setVisibility(true);
	ge.getOptions().setAtmosphereVisibility(true);
}


function waiting() {
	while (gamepad.buttons[9].value != 1) {}
	move();
}

function move() {
	myAudio.play();
	ge.getOptions().setMouseNavigationEnabled(false);
	if (!moving) {
		nextStep();
	}
}

function nextStep() {
	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_ABSOLUTE);

	glider.makeNextStep();
	
	if(gamepad != undefined) {
		$("#slider-left").val(leftStick);
		$("#slider-right").val(rightStick);
	}
	else {
		leftStick = $("#slider-left").val();
		rightStick = $("#slider-right").val();
	}

	$("#slider-left").trigger("oninput");
	$("#slider-right").trigger("oninput");

	if (leftStick >= 0.9 * maxSpeed && rightStick >= 0.9) {
		glider.simulateFullStall();
	}
	if (fullStall) {
		glider.simulateFullStall();
	}

	setHeading(camera);
	setRoll(camera);
	setTilt(camera);
	setPosition(camera);

	// log(camera);
	document.getElementById('horizontalspeed').innerHTML = Math
			.round(3.6 * glider.getHorizontalSpeed());
	document.getElementById('verticalspeed').innerHTML = -Math.round(glider
			.getVerticalSpeed() * 10) / 10;
	document.getElementById('alt').innerHTML = Math.round(camera.getAltitude());

	ge.getView().setAbstractView(camera);

	timer = setTimeout("nextStep()", constants.getTimeInterval());
	moving = true;
	setController();
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
	camera.setLatitude(camera.getLatitude() + changeLatitude);
	camera.setLongitude(camera.getLongitude() + changeLongitude);
}

function setVerticalPosition(camera) {
	var v = glider.getVerticalSpeed();
	var s = v * constants.getTimeInterval();
	camera.setAltitude(camera.getAltitude() - s);
}

function setHeading(camera) {
	var angularVelocity = pilot.getAngularVelocity();
	//Including a "Deadzone", because the Controller is never exactly 0
	if(angularVelocity <= 0.07 && angularVelocity >= -0.07) {
		angularVelocity = 0;
	}
	var alpha = angularVelocity * constants.getTimeInterval();
	alpha = constants.convertRadianToDegree(alpha);
	camera.setHeading(camera.getHeading() + alpha);
	currentHeading = camera.getHeading();
	setCompass(-camera.getHeading());
}

function setRoll(camera) {
	var angle = constants.convertRadianToDegree(glider.getRollAngle());

	if (!glider.getIsOnRightSite()) {
		angle = -angle;
	}
	camera.setRoll(angle);
}

function setTilt(camera) {
	var angle = constants.convertRadianToDegree(glider.getPitchAngle());
	if (!glider.getIsOnPositiveSite()) {
		angle = -angle;
	}
	camera.setTilt(angle + 85);
}

function stopMovement() {
	clearTimeout(timer);
	glider.reset();
	moving = false;
}

function reset() {
	stopMovement();
	speedChange = 0.0001;
	initCamera();
	myAudio.pause();
	myAudio.currentTime = 0
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
	};
})();

var bothBreaks = (function() {
	var oldValue = 0;
	return function(value) {
		var slidervalue = document.getElementById('slider-both').value;
		if (value >= 0.90 * maxSpeed) {
			glider.setInFullStall(true);
			fullStall = true;
		} else if (fullStall) {
			glider.setInFullStall(false);
			fullStall = false;
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
	console.log("longitude: " + camera.getLongitude());
	console.log("Latitude: " + camera.getLatitude());
	console.log("Altitude: " + camera.getAltitude());
	console.log("Heading: " + camera.getHeading());
	console.log(" ");
}

function initStartPosition() {
	stopMovement();
	initCamera();
}
function setController() {

	gamepad = navigator.getGamepads()[0];

	if (gamepad != undefined) {
		if (gamepad.axes[1] > 0.05) {
			leftStick = Math.round(10 * gamepad.axes[1]) / 10 * maxSpeed;
		} else {
			leftStick = 0;
		}
		if (gamepad.axes[3] > 0.05) {
			rightStick = Math.round(10 * gamepad.axes[3]) / 10 * maxSpeed;
		} else {
			rightStick = 0;
		}

		if (gamepad.buttons[8].value == 1) {
			moving = false;
			reset();
		}
	}
}

google.setOnLoadCallback(init);
