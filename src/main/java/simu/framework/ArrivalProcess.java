
/**
 * ArrivalProcess is responsible for generating arrival events in the simulation.
 * It uses a continuous random generator to determine the time between arrivals and
 * schedules new events of a specified type into the event list.
 *
 * @author (your name)
 */
package simu.framework;
import eduni.distributions.*;
import simu.model.EventType;


/**
 * Handles the generation of arrival events for the simulation using a random generator.
 */
public class ArrivalProcess {

	/** Random generator for inter-arrival times. */
	private ContinuousGenerator generator;

	/** The event list to which new events are added. */
	private EventList eventList;

	/** The type of event to generate. */
	private EventType type;


	/**
	 * Constructs an ArrivalProcess with the given generator, event list, and event type.
	 *
	 * @param g the continuous random generator for inter-arrival times
	 * @param tl the event list to which new events are added
	 * @param type the type of event to generate
	 */
	public ArrivalProcess(ContinuousGenerator g, EventList tl, EventType type) {
		this.generator = g;
		this.eventList = tl;
		this.type = type;
	}


	/**
	 * Generates the next arrival event and adds it to the event list.
	 * The event is scheduled at the current simulation time plus a sampled inter-arrival time.
	 */
	public void generateNext() {
		Event t = new Event(type, Clock.getInstance().getTime() + generator.sample());
		eventList.add(t);
	}


}
