/*
 * -------------------------------------------------------
 * Initializing Google Earth
 * -------------------------------------------------------
 */

var ge;
var timer;
var speedChange = 0.00001;
var moving = false;
var glider = new Glider();

google.load("earth", "1", false);

//Creates a new instance of google earth.
function init() {
	// Param: div id where the earth will appear, function to call by success,
	// function to call by failure.
	google.earth.createInstance('map3d', initCB, failureCB);
	
	var buttonSize = 108;
	var left = 550;
	
	createNativeHTMLButton(left + (1*buttonSize), 10, buttonSize, buttonSize, 'left', rollLeft);
	createNativeHTMLButton(left + (2*buttonSize), 10, buttonSize, buttonSize, 'slower', slower);
	createNativeHTMLButton(left + (3*buttonSize), 10, buttonSize, buttonSize, 'play', move);
	createNativeHTMLButton(left + (4*buttonSize), 10, buttonSize, buttonSize, 'faster', faster);
	createNativeHTMLButton(left + (5*buttonSize), 10, buttonSize, buttonSize, 'right', rollRight);
	createNativeHTMLButton(left + (7*buttonSize), 10, buttonSize, buttonSize, 'pause', stopMovement);
	createNativeHTMLButton(left + (8*buttonSize), 10, buttonSize, buttonSize, 'stop',reset);
	
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
	//ge.getSun().setVisibility(true);
	ge.getOptions().setAtmosphereVisibility(true);
}


/*
 * -------------------------------------------------------
 * In this section are test functions to determine how
 * the manipulation of the camera works.
 * -------------------------------------------------------
 */

function move()
{
	if(!moving)
	{
		nextStep();
	}
	
}

function nextStep() {
	
		var camera = ge.getView().copyAsCamera(ge.ALTITUDE_RELATIVE_TO_GROUND);
	
		camera.setLatitude(camera.getLatitude() + speedChange);
		camera.setLongitude(camera.getLongitude() +speedChange);
	
		ge.getView().setAbstractView(camera);
		
	    timer = setTimeout("nextStep()",10);  
	    moving = true;	
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
function rollLeft()
{
	roll(15);
}
function rollRight()
{
	roll(-15);
}

function roll(rollAngle) {
	var camera = ge.getView().copyAsCamera(ge.ALTITUDE_RELATIVE_TO_GROUND);
	
	camera.setRoll(camera.getRoll() + rollAngle);
	
	ge.getView().setAbstractView(camera);
}

function showWing(){	
	
	var cat = new Mammal("Cat", "claws");
	var dog = new Mammal("Dog", "a wet nose");
	
	alert(cat.species);
	
}

google.setOnLoadCallback(init);


























