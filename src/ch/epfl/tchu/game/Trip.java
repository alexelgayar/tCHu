package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, final and immutable. Represents what we call a "journey"
 */
public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    /**
     * Constructs a new route between the two given stations, worth the given number of points
     * @param from Departure station
     * @param to Arrival station
     * @param points points if both stations are connected
     * @throws NullPointerException if one of the two stations is null
     * @throws IllegalArgumentException if the number of points is not strictly positive (> 0)
     */
    public Trip(Station from, Station to, int points) {
        Preconditions.checkArgument(points > 0);

        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        this.points = points;
    }

    /**
     * Returns the list of all possible routes going from a "from" station to a "to" station, each worth the given number of points
     * @param from List containing the departure stations
     * @param to List containing the arrival stations
     * @param points points if both stations are connected
     * @return a list containing the different trips
     * @throws IllegalArgumentException if one of the lists os empty, or if the number of points is not strictly positive
     */
    public static List<Trip> all(List<Station> from, List<Station> to, int points) {
        Preconditions.checkArgument((points > 0) && (!from.isEmpty()) && (!to.isEmpty()));

        List<Trip> allTrips = new ArrayList<>();

        for(Station i : from){
            for (Station j : to){
                allTrips.add(new Trip(i, j, points));
            }
        }
        return allTrips;
    }

    /**
     * Returns the departure station of the trip
     * @return the departure station of the trip
     */
    public Station from() {
        return from;
    }

    /**
     * Returns the arrival station of the trip
     * @return the arrival station of the trip
     */
    public Station to() {
        return to;
    }

    /**
     * Method which returns the number of points of the trip
     * @return the number of points of the trip
     */
    public int points() {
        return points;
    }

    /**
     * Method which returns the number of points obtained in the path for the given connectivity
     * @param connectivity true iff both stations are connected
     * @return the number of points added if the stations are connected, or removes the points if they are not
     */
    public int points(StationConnectivity connectivity) {
        return connectivity.connected(from, to) ? points : -points;
    }
}
