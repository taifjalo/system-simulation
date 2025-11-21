package simu.framework;

/**
 * IEngine defines the interface for simulation engine control, including methods for
 * setting simulation time, controlling execution delay, and managing pause/resume state.
 * Used by the Controller to interact with the simulation engine.
 *
 * @author (your name)
 */
public interface IEngine { // NEW
	/**
	 * Sets the simulation time limit.
	 * @param time the simulation time limit
	 */
	public void setSimulationTime(double time);
	/**
	 * Sets the delay (in milliseconds) between simulation steps.
	 * @param time the delay in milliseconds
	 */
	public void setDelay(long time);
	/**
	 * Returns the delay (in milliseconds) between simulation steps.
	 * @return the delay in milliseconds
	 */
	public long getDelay();
	/**
	 * Pauses the simulation.
	 */
	public void pauseSimulation();
	/**
	 * Resumes the simulation if it is paused.
	 */
	public void resumeSimulation();
	/**
	 * Returns whether the simulation is currently paused.
	 * @return true if paused, false otherwise
	 */
	public boolean isPaused();
}
