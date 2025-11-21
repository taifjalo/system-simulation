package simu.framework;

import simu.framework.statistics.SimulationStatistics;
import simu.model.ServicePoint;
import controller.Controller;

/**
 * Engine is an abstract base class for simulation engines. It manages the simulation loop,
 * event processing, service points, and provides pause/resume functionality. Subclasses must
 * implement initialization, event handling, and results processing.
 *
 * @author (your name)
 */
public abstract class Engine extends Thread implements IEngine {
	/** Time when the simulation will be stopped. */
	private double simulationTime = 0;
	/** Delay in milliseconds between simulation steps. */
	private long delay = 0;
	/** Reference to the global simulation clock. */
	private Clock clock;
	/** Reference to the simulation statistics singleton. */
	private SimulationStatistics simulationStatistics = SimulationStatistics.getInstance();
	/** Reference to the simulation controller. */
	Controller controller;
	/** The event list for managing simulation events. */
	protected EventList eventList;
	/** Array of service points in the simulation. */
	protected ServicePoint[] servicePoints;
	// Pause/Resume functionality
	/** Indicates if the simulation is currently paused. */
	private volatile boolean isPaused = false;
	/** Lock object for pause/resume synchronization. */
	private final Object pauseLock = new Object();

	/**
	 * Constructs an Engine with the given controller. Service points are created in subclasses.
	 * @param controller the simulation controller
	 */
	public Engine(Controller controller) {
		this.controller = controller;
		clock = Clock.getInstance();
		eventList = new EventList();
		/* Service Points are created in simu.model-package's class who is inheriting the Engine class */
	}

	/**
	 * Sets the simulation time limit. Also updates the statistics configuration.
	 * @param time the simulation time limit
	 */
	@Override
	public void setSimulationTime(double time) {
		this.simulationTime = time;
		// Also set it in statistics for configuration purposes
		simulationStatistics.setSimulationTime(time);
	}

	/**
	 * Sets the delay (in milliseconds) between simulation steps.
	 * @param time the delay in milliseconds
	 */
	@Override
	public void setDelay(long time) {
		this.delay = time;
	}

	/**
	 * Returns the delay (in milliseconds) between simulation steps.
	 * @return the delay in milliseconds
	 */
	@Override
	public long getDelay() {
		return delay;
	}

	/**
	 * Pauses the simulation. The simulation thread will wait until resumed.
	 */
	public void pauseSimulation() {
		synchronized (pauseLock) {
			isPaused = true;
		}
	}

	/**
	 * Resumes the simulation if it is paused.
	 */
	public void resumeSimulation() {
		synchronized (pauseLock) {
			isPaused = false;
			pauseLock.notifyAll();
		}
	}

	/**
	 * Returns whether the simulation is currently paused.
	 * @return true if paused, false otherwise
	 */
	public boolean isPaused() {
		return isPaused;
	}

	/**
	 * The main simulation loop. Handles initialization, event processing, and results.
	 * Supports pause/resume functionality.
	 */
	@Override
	public void run() {
		initialization(); // creating, e.g., the first event

		while (simulate()){
			// Check if paused
			synchronized (pauseLock) {
				while (isPaused) {
					try {
						pauseLock.wait();
					} catch (InterruptedException e) {
						// If interrupted while paused, exit gracefully
						return;
					}
				}
			}

			delay(); // NEW
			clock.setTime(currentTime());
			runBEvents();
			tryCEvents();
		}

		results();
	}

	/**
	 * Processes all B-type events scheduled for the current simulation time.
	 */
	private void runBEvents() {
		while (eventList.getNextTime() == clock.getTime()){
			Event event = eventList.remove();
            Trace.out(Trace.Level.INFO, "\nTime is: " + clock.getTime());
            Trace.out(Trace.Level.INFO, "Running event: " + event.getType());
            runEvent(event);
		}
	}

	/**
	 * Attempts to start service at all service points that are not reserved and have a queue.
	 * Can be overridden by subclasses for custom logic.
	 */
	private void tryCEvents() {    // define protected, if you want to overwrite
		for (ServicePoint p: servicePoints){
			if (!p.isReserved() && p.isOnQueue()){
				p.beginService();
			}
		}
	}

	/**
	 * Returns the time of the next scheduled event.
	 * @return the next event time
	 */
	private double currentTime(){
		return eventList.getNextTime();
	}

	/**
	 * Determines whether the simulation should continue running.
	 * @return true if the simulation time has not been reached, false otherwise
	 */
	private boolean simulate() {
		return clock.getTime() < simulationTime;
	}

	/**
	 * Delays the simulation thread for the configured delay time.
	 */
	private void delay() { // NEW
		Trace.out(Trace.Level.INFO, "Delay " + delay);
		try {
			sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Performs simulation-specific initialization, such as creating the first event.
	 * Must be implemented by subclasses.
	 */
	protected abstract void initialization(); 	// Defined in simu.model-package's class who is inheriting the Engine class

	/**
	 * Handles the execution of a single event. Must be implemented by subclasses.
	 * @param t the event to process
	 */
	protected abstract void runEvent(Event t);	// Defined in simu.model-package's class who is inheriting the Engine class

	/**
	 * Processes and outputs simulation results. Must be implemented by subclasses.
	 */
	protected abstract void results(); 			// Defined in simu.model-package's class who is inheriting the Engine class
}