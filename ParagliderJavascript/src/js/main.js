/*
 * -------------------------------------------------------
 * Initializing Google Earth
 * -------------------------------------------------------
 */

var ge;
var timer;
var speedChange = 0.0001;

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

	ge.getOptions().setFlyToSpeed(3.0);
	camera.setLatitude(40.681682);
	camera.setLongitude(-74.038428);
	camera.setTilt(90);
	camera.setAltitude(500);
	camera.setHeading(45);
	
	ge.getView().setAbstractView(camera);
}

function move() {
	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_RELATIVE_TO_GROUND);

	// Add 25 degrees to the current latitude and longitude values.
	camera.setLatitude(camera.getLatitude() + speedChange);
	camera.setLongitude(camera.getLongitude() +speedChange);

	// Update the view in Google Earth.
	ge.getView().setAbstractView(camera);
	
    timer = setTimeout("move()",50); // time in milliseconds; this is 5 seconds
}

function stopMovement() {
	clearTimeout(timer);	
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

google.setOnLoadCallback(init);

























