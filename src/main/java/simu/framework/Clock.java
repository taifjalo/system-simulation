
/**
 * Clock is a singleton class that tracks the current simulation time.
 * It provides methods to set and get the simulation time, ensuring a single
 * global clock instance is used throughout the simulation.
 *
 * @author (your name)
 */
package simu.framework;


/**
 * Singleton class for tracking the current simulation time.
 */
public class Clock {

	/** The current simulation time. */
	private double time;

	/** Singleton instance of Clock. */
	private static Clock instance;

	/**
	 * Private constructor to prevent external instantiation. Initializes time to zero.
	 */
	private Clock(){
		time = 0;
	}

	/**
	 * Returns the singleton instance of Clock. If it does not exist, creates a new one.
	 * @return the singleton instance of Clock
	 */
	public static Clock getInstance(){
		if (instance == null){
			instance = new Clock();
		}
		return instance;
	}

	/**
	 * Sets the current simulation time.
	 * @param time the simulation time to set
	 */
	public void setTime(double time){
		this.time = time;
	}

	/**
	 * Returns the current simulation time.
	 * @return the current simulation time
	 */
	public double getTime(){
		return time;
	}
}
