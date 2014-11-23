	var leftWing = new Wing("Left");
	var rightWing = new Wing("Right");
	var pilot = Pilot.getInstance();
	var yawAngle = 0;
	
	private static Glider instance;
	private Glider() {}
	

	public static Glider getInstance() {
		if(instance == null) {
			instance = new Glider();
		}
		
		return instance;
	}

	function getPilotPosition()
	{
		return pilot.getCurrentPosition();
	}

	function getPitchAngle() {
		return pilot.getPitchAngle();
	}


	function getRollAngle() {
		return pilot.getRollAngle();
	}


	function getYawAngle() {
		calculateYawAngle();
		return yawAngle;
	}
	

	function getHorizontalSpeed() {
		return (leftWing.getHorizontalSpeed() + rightWing.getHorizontalSpeed())/2;
	}

	function getVerticalSpeed() {
		return (leftWing.getVerticalSpeed() + rightWing.getVerticalSpeed())/2;
	}
	

	function getCurrentGlideRatio() {
		return (leftWing.getCurrentGlideRatio() + rightWing.getCurrentGlideRatio())/2;
	}	
	
	/**
	 * Changes the speed of the Glider.
	 * @param msLeft double in meter per seconds
	 * @param msRight double in meter per seconds
	 */
	function changeSpeed(msLeft, msRight )
	{
		leftWing.changeCurrentSpeed(msLeft);
		rightWing.changeCurrentSpeed(msRight);
		pilot.setChangeInSpeed((msLeft+msRight)/2);
		pilot.setChangeInSpeedY(msLeft - msRight);
	}
	

	function reset()
	{
		yawAngle = 0;
		pilot.reset();
	}
	
	//TODO: Formel korrigieren, siehe getCurveRadius in Pilot
	function calculateYawAngle()
	{		
		
		double pathLeft = leftWing.getHorizontalSpeed() * Constants.TIME_INTERVALL;
		double pathRight = rightWing.getHorizontalSpeed() * Constants.TIME_INTERVALL;
			
		if(pathLeft != pathRight) {
			double xa = pathLeft;
			double xb = pathRight;
			double rb = Constants.GLIDER_WINGSPAN;
			double ra = (xa*rb)/(xb-xa);
			double radius = ra + rb;
			yawAngle += Math.toDegrees(xb/radius);
		}
	}

	function makeNextStep() {
		pilot.calculatePosition(leftWing.getHorizontalSpeed(), rightWing.getHorizontalSpeed());
	}
	
	function getAngularVelocity() {
		return pilot.getAngularVelocity();
	}

	function isOnPositiveSite() {
		return pilot.isOnPositiveSite();
	}
	
	//Method just for the sliders.
	function getWings() {
		return new Wing[] {rightWing, leftWing};
	}
	
	function setInFullStall(inFullStall) {
		pilot.setInFullStall(inFullStall);
	}

	function isOnRightSite() {
		return pilot.isOnRightSite();
	}
}
