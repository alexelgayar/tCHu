package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 */
public final class Ticket implements Comparable<Ticket> {

    private final String TEXT;

    private final List<Trip> trips;

    /**
     * Constructs a ticket out a list of trips
     * @param trips list containing all the trips
     */
   public Ticket(List<Trip> trips) {
        Preconditions.checkArgument(trips != null);
        Preconditions.checkArgument(!trips.isEmpty());

        for (Trip w : trips) {
            Preconditions.checkArgument(trips.get(0).from().toString().equals(w.from().toString()));
        }

        this.trips = trips;
        TEXT = computeText();
    }

    /**
     * Creates a ticket from a single trip
     * @param from Departure station
     * @param to Arrival station
     * @param points points if both stations are connected
     */
   public Ticket(Station from, Station to, int points) {
        this(List.of(new Trip(from, to, points)));
    }

    private String computeText() {
        TreeSet<String> to = new TreeSet<>();

        for (Trip w : trips) {
            to.add(w.to().toString() + " (" + w.points() + ")");
        }

        String destinations = String.join(", ", to);

        String s = new String();

        if (trips.size() == 1) {
            s = String.format("%s - %s", trips.get(0).from().toString(), destinations);
        } else
            s = String.format("%s - {%s}", trips.get(0).from().toString(), destinations);

        return s;
    }

    /**
     *
     * @return textual form of a ticket
     */
    public String text() {
        return TEXT;
    }

    /**
     *
     * @param connectivity If both stations are connected
     * @return number of points earned per ticket
     */
    public int points(StationConnectivity connectivity) {

        int i = 0;
        int temp = 0;

        for (Trip w : trips) {
            if (connectivity.connected(w.from(), w.to())) {
                if (w.points() > temp)
                    temp = w.points();
            }
        }
        i = temp;

        if (temp == 0) {
            temp = trips.get(0).points();
            for (Trip w : trips) {
                if (w.points() < temp) {
                    temp = w.points();
                }
            }
            i = -temp;
        }
        return i;
    }

    /**
     * Compares to ticket alphabetically
     * @param that ticket to be compared to
     * @return a negative integer if this is smaller than that alphabetically,
     * a positive integer if this is greater than that alphabetically
     * and 0 if both tickets are equal
     */
    @Override
    public int compareTo(Ticket that) {
        return this.text().compareTo(that.text());
    }


}

