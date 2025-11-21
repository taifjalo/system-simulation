package simu.framework;

import simu.model.Customer;

/**
 * Event represents a scheduled event in the simulation, containing its type, scheduled time,
 * and an optional associated customer. Implements Comparable for event list ordering.
 *
 * @author (your name)
 */
public class Event implements Comparable<Event> {
	/** The type of the event. */
	private IEventType type;
	/** The scheduled time of the event. */
	private double time;
	/** The customer associated with this event, if any. */
	private Customer customer; // Add customer field for event tracking

	/**
	 * Constructs an Event with the given type and time, without a customer.
	 * @param type the type of the event
	 * @param time the scheduled time of the event
	 */
	public Event(IEventType type, double time) {
		this.type = type;
		this.time = time;
		this.customer = null;
	}

	/**
	 * Constructs an Event with the given type, time, and associated customer.
	 * @param type the type of the event
	 * @param time the scheduled time of the event
	 * @param customer the customer associated with this event
	 */
	// Constructor with customer
	public Event(IEventType type, double time, Customer customer) {
		this.type = type;
		this.time = time;
		this.customer = customer;
	}

	/**
	 * Sets the type of the event.
	 * @param type the event type to set
	 */
	public void setType(IEventType type) {
		this.type = type;
	}

	/**
	 * Returns the type of the event.
	 * @return the event type
	 */
	public IEventType getType() {
		return type;
	}

	/**
	 * Sets the scheduled time of the event.
	 * @param time the time to set
	 */
	public void setTime(double time) {
		this.time = time;
	}

	/**
	 * Returns the scheduled time of the event.
	 * @return the event time
	 */
	public double getTime() {
		return time;
	}

	/**
	 * Returns the customer associated with this event, or null if none.
	 * @return the associated customer
	 */
	public Customer getCustomer() {
		return customer;
	}

	/**
	 * Sets the customer associated with this event.
	 * @param customer the customer to associate
	 */
	public void setCustomer(Customer customer) {
		this.customer = customer;
	}

	/**
	 * Compares this event to another event based on scheduled time for ordering.
	 * @param arg the other event to compare to
	 * @return -1 if this event is earlier, 1 if later, 0 if equal
	 */
	@Override
	public int compareTo(Event arg) {
		if (this.time < arg.time) return -1;
		else if (this.time > arg.time) return 1;
		return 0;
	}
}
