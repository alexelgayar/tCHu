package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;
import java.util.TreeSet;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, final and immutable. Represents a ticket.
 */
public final class Ticket implements Comparable<Ticket> {

    private final String TEXT;
    private final List<Trip> trips;

    /**
     * Constructor which constructs a ticket out of a list of trips
     * @param trips list containing all the trips
     * @throws IllegalArgumentException if trips is empty, or if all the start stations of trips don't have the same name
     */
    public Ticket(List<Trip> trips) {
        Preconditions.checkArgument(!trips.isEmpty());

        for (Trip w : trips) {
            Preconditions.checkArgument(trips.get(0).from().toString().equals(w.from().toString()));
        }

        this.trips = trips;
        this.TEXT = computeText(trips);
    }

    /**
     * Constructs a ticket from a single trip
     * @param from Departure station
     * @param to Arrival station
     * @param points points if both stations are connected
     */
    public Ticket(Station from, Station to, int points) {
        this(List.of(new Trip(from, to, points)));
    }

    //Private method used to compute the textual representation of the ticket
    private static String computeText(List<Trip> trips) {
        TreeSet<String> to = new TreeSet<>();

        for (Trip w : trips) {
            to.add(w.to().toString() + " (" + w.points() + ")");
        }

        String destinations = String.join(", ", to);

        return (trips.size() == 1)
                ? String.format("%s - %s", trips.get(0).from().toString(), destinations)
                : String.format("%s - {%s}", trips.get(0).from().toString(), destinations);
    }

    /**
     * Returns the textual representation of the ticket
     * @return the textual form of a ticket
     */
    public String text() {
        return TEXT;
    }

    /**
     * Returns the number of points the ticket is worth, provided the connectivity (belonging to the player who owns the ticket)
     * @param connectivity true iff both stations are connected
     * @return number of points earned per ticket
     */
    public int points(StationConnectivity connectivity) { //TODO: More efficient way to compute?

        boolean connected = false;
        int maxPoints = 0;
        int minPoints = trips.get(0).points();

        for (Trip trip: trips){
            maxPoints = Math.max(maxPoints, trip.points(connectivity));
            minPoints = Math.min(minPoints, trip.points());
            if (!connected) connected = connectivity.connected(trip.from(), trip.to());
        }

        return connected ? maxPoints : -(minPoints);
    }

    /**
     * Compares two tickets (textual representation) alphabetically, returns negative int if this < that, positive int if this > that, 0 if this == that
     * @param that ticket to be compared to
     * @return a negative integer if "this" is smaller than "that" alphabetically,
     * return a positive integer if "this" is greater than "that" alphabetically
     * return 0 if both tickets are equal
     */
    @Override
    public int compareTo(Ticket that) {
        return this.text().compareTo(that.text());
    }


}

