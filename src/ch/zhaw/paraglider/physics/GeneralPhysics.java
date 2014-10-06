package ch.zhaw.paraglider.physics;


/**
 * 
 * @author Christian Brüesch
 * 
 *This Class manages the General Physics of the Simulator.
 * 
 */
public class GeneralPhysics {

	private int WEIGHT_OF_PILOT = 85;
	private int WEIGHT_OF_EQUIPMENT = 20;	
	
	private double GLIDE_RATIO = 9;
	private double RESISTANCE_RATIO = 0.6;
	
	/* Air Density in kg/m^3 */
	private double AIR_DENSITY = 1.225;
	
	/* Ground speed + head wind in m/s  */
	private double RELATIVE_SPEED = 10;
	
	/* Projected */
	private double PROJECTED_UPSTREAMFLOW_AREA = 6;
	
	/* Projected wing area in m^2 */
	private double PROJECTED_WING_AREA = 6;
	private final double gravitationalForce = 9.81f;
	private static GeneralPhysics instance;
	
	private GeneralPhysics() {
		super();
	}
	
	public static GeneralPhysics getInstance() {
		if(instance == null) {
			instance = new GeneralPhysics();
		}
		
		return instance;
	}
	
	/**
	 * Returns the gravitational force.
	 * @return double
	 */
	public double getFg () {
		return gravitationalForce * (WEIGHT_OF_EQUIPMENT + WEIGHT_OF_PILOT);
	}
	/**
	 * Returns the uplift force.
	 * @return double
	 */
	public double getFa() {
		//TODO: doublecheck formula
		return (GLIDE_RATIO * AIR_DENSITY * Math.pow(RELATIVE_SPEED, 2) * PROJECTED_WING_AREA)/2.0; 
	}
	
	/**
	 * Returns the resistance force.
	 * @return double
	 */
	public double getFr() {
		//TODO: doublecheck formula for Fr Angeströmte fläche
		return getDynamicPressure() * PROJECTED_UPSTREAMFLOW_AREA * RESISTANCE_RATIO;
	}
	
	/**
	 * Returns the dynamic pressure.
	 * @return double
	 */
	public double getDynamicPressure()
	{
		return (AIR_DENSITY * Math.pow(RELATIVE_SPEED, 2))/2.0;
	}

	public int getWEIGHT_OF_PILOT() {
		return WEIGHT_OF_PILOT;
	}

	public void setWEIGHT_OF_PILOT(int weight) {
		WEIGHT_OF_PILOT = weight;
	}

	public int getWEIGHT_OF_EQUIPMENT() {
		return WEIGHT_OF_EQUIPMENT;
	}

	public void setWEIGHT_OF_EQUIPMENT(int weight) {
		WEIGHT_OF_EQUIPMENT = weight;
	}

}
