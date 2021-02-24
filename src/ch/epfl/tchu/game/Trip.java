package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;
import java.util.Objects;


public final class Trip {

    private final Station from;
    private final Station to;
    private final int points;

    Trip(Station from, Station to, int points){

        this.from = Objects.requireNonNull(from);
        this.to = Objects.requireNonNull(to);
        this.points = points;

        Preconditions.checkArgument(points > 0);

    }

   public static List<Trip> all(List<Station> from, List<Station> to, int points){

       Preconditions.checkArgument(points > 0);
       Preconditions.checkArgument(from != null);
       Preconditions.checkArgument(to != null);

   }

    public Station From() {
        return from;
    }

    public Station To() {
        return to;
    }

    public int Points() {
        return points;
    }

    public int Points(StationConnectivity connectivity){

    }
}
