/*
 * -------------------------------------------------------
 * Initializing Google Earth
 * -------------------------------------------------------
 */

var ge;
var timer;
var speedChange = 0.00001;

google.load("earth", "1", false);

//Creates a new instance of google earth.
function init() {
	// Param: div id where the earth will appear, function to call by success,
	// function to call by failure.
	google.earth.createInstance('map3d', initCB, failureCB);
	
}

//initialises google earth and set it's visibility to true.
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

//Coordinates of New York City: Latitude: 40.681682 Longitude: -74.038428
//COordinates of Weisshorn: Latitude: 46.789676 Longitude: 9.638993

function initCamera() {
	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_RELATIVE_TO_GROUND);

	ge.getOptions().setFlyToSpeed(ge.SPEED_TELEPORT);
	ge.getOptions().setMouseNavigationEnabled (false);
	camera.setLatitude(40.681682);
	camera.setLongitude(-74.038428);
	camera.setTilt(90);
	camera.setAltitude(500);
	camera.setHeading(45);
	camera.setRoll(0);
	
	ge.getView().setAbstractView(camera);
}


/*
 * -------------------------------------------------------
 * In this section are test functions to determine how
 * the manipulation of the camera works.
 * -------------------------------------------------------
 */

function move() {
	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_RELATIVE_TO_GROUND);

	camera.setLatitude(camera.getLatitude() + speedChange);
	camera.setLongitude(camera.getLongitude() +speedChange);

	ge.getView().setAbstractView(camera);
	
    timer = setTimeout("move()",10);
}

function stopMovement() {
	clearTimeout(timer);	
}

function faster() {
	speedChange += 0.00001;
}

function slower() {
	speedChange -= 0.00001;
	
}

function reset() {
	stopMovement();
	speedChange = 0.0001;
	initCamera();
}

function roll(rollAngle) {
	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_RELATIVE_TO_GROUND);
	
	camera.setRoll(camera.getRoll() + rollAngle);
	
	ge.getView().setAbstractView(camera);
}

google.setOnLoadCallback(init);

























