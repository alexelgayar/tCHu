package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 */
public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    /**
     *
     * @param from Departure station
     * @param to Arrival station
     * @param points points if both stations are connected
     */
   public Trip(Station from, Station to, int points) {

        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        this.points = points;

        Preconditions.checkArgument(points > 0);

    }

    /**
     *
     * @param from List containing the departure station
     * @param to List of arrival stations
     * @param points points if both stations are connected
     * @return a list containing the different trips
     */
    public static List<Trip> all(List<Station> from, List<Station> to, int points) {
        Preconditions.checkArgument((points > 0) || (from != null) || (to != null));

        List<Trip> allTrips = new ArrayList<>();

        for(Station i : from){
            for (Station j : to){
                allTrips.add(new Trip(i, j, points));
            }
        }
        return allTrips;
    }

    public Station from() {
        return from;
    }

    public Station to() {
        return to;
    }

    public int points() {
        return points;
    }

    public int points(StationConnectivity connectivity) {
        if (connectivity.connected(from, to))
            return points;
        else
            return (-points);
    }
}
