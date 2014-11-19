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
	createNativeHTMLButton(400, 10, 108, 108, 'left',slower);
	createNativeHTMLButton(508, 10, 108, 108, 'slower', slower);
	createNativeHTMLButton(616, 10, 108, 108, 'play', move);
	createNativeHTMLButton(724, 10, 108, 108, 'faster', faster);
	createNativeHTMLButton(832, 10, 108, 108, 'right',slower);
	createNativeHTMLButton(1048, 10, 108, 108, 'pause',stopMovement );
	createNativeHTMLButton(1156, 10, 108, 108, 'stop',reset);
	
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
	ge.getLayerRoot().enableLayerById(ge.LAYER_TERRAIN, true);
	ge.getLayerRoot().enableLayerById(ge.LAYER_TREES , true);
	ge.getLayerRoot().enableLayerById(ge.LAYER_BUILDINGS , true);
	ge.getSun().setVisibility(true);
	ge.getOptions().setAtmosphereVisibility(true);	
	 
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

function showWing(){
	var wing = new Wing("LeftWing");
	wing.log();
	wing.changeCurrentSpeed(3);
	wing.log();
	
	var vector3D = new Vector3D(1.2,2.3,3.4);
	vector3D.log();
	var vec1 = new Vector3D(2,3,4);
	vec1.log();
	var vec2 = vector3D.sub(vec1);
	vec2.log();
	
	var pilot = new Pilot();
	pilot.log();
}

google.setOnLoadCallback(init);


























