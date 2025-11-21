package simu.framework;

import java.util.PriorityQueue;

/**
 * EventList manages a priority queue of simulation events, providing methods to add,
 * remove, and inspect the next scheduled event. Events are ordered by their scheduled time.
 *
 * @author (your name)
 */
public class EventList {
	/** Priority queue for storing events in time order. */
	private PriorityQueue<Event> list = new PriorityQueue<Event>();
	
	/**
	 * Constructs an empty EventList.
	 */
	public EventList() {
	}
	
	/**
	 * Removes and returns the event with the earliest scheduled time.
	 * @return the earliest event in the list
	 */
	public Event remove(){
		return list.remove();
	}
	
	/**
	 * Adds an event to the event list, maintaining time order.
	 * @param t the event to add
	 */
	public void add(Event t){
		list.add(t);
	}
	
	/**
	 * Returns the scheduled time of the next event in the list.
	 * @return the time of the next event
	 */
	public double getNextTime(){
		return list.peek().getTime();
	}

    /**
     * Returns the underlying priority queue of events (for testing purposes).
     * @return the event priority queue
     */
    public PriorityQueue<Event> getEventList() {
        return list;
    }
}
