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
		//TODO: math formula for Fa
		return 0;
	}
	
	/**
	 * Returns the resistance force.
	 * @return double
	 */
	public double getFr() {
		//TODO: math formula for Fr
		return 0;
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
