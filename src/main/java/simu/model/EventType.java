package simu.model;

import simu.framework.IEventType;

/**
 * Enum representing the different types of events in the simulation model.
 * Each event type corresponds to a specific action or transition in the simulation process.
 */
public enum EventType implements IEventType {
    /** Customer arrives at the restaurant (walk-in). */
    ArrivalRestaurant,
    /** Customer arrives via call (delivery order). */
    ArrivalCall,
    /** Customer returns to receive money (refund). */
    ReturnMoney,
    /** Payment failed event. */
    PaymentFailed,
    /** Customer departs from the reception. */
    DepartureFromReception,
    /** Customer departs from the kitchen. */
    DepartureFromKitchen,
    /** Customer departs from the delivery service. */
    DepartureFromDelivery,
    /** Delivery is refused by the customer. */
    DeliveryRefused,
    /** Order is remade (sent back to kitchen). */
    RemakeOrder,
    /** Customer departs from counter to delivery. */
    DepartureFromCounterToDelivery,
    /** Counter error, customer sent to kitchen. */
    CounterErrorToKitchen,
    /** Counter error, customer sent to reception. */
    CounterErrorToReception,
    /** Customer departs from counter to customer (final handoff). */
    DepartureFromCounterToCostumer;
}
