package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;

public final class Ticket implements Comparable<Ticket> {

    private final String TEXT;

    Ticket(List<Trip> trips) {

        Preconditions.checkArgument(trips != null);

        for (int i = 0; i < trips.size() - 1; ++i) {
            Preconditions.checkArgument(trips.get(i).from() == trips.get(i + 1).from());
        }
    }

    Ticket(Station from, Station to, int points) {

        this(List.of(new Trip(from, to, points)));
    }

    private static String computeText(){


    }

    public String text(){
        return TEXT;
    }

    @Override
    public int compareTo(Ticket o) {
        return 0;
    }


}

