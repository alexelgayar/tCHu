package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public final class Ticket implements Comparable<Ticket> {

    private final String TEXT;

   private List<Trip> trips;

    Ticket(List<Trip> trips) {

        this.trips = trips;

        Preconditions.checkArgument(trips != null);

        for (int i = 0; i < trips.size() - 1; ++i) {
            Preconditions.checkArgument(trips.get(i).from() == trips.get(i + 1).from());
        }

        TEXT = computeText();

    }

    Ticket(Station from, Station to, int points) {

        this(List.of(new Trip(from, to, points)));
    }

    private String computeText(){

        TreeSet<String> to = new TreeSet<>();

        for (Trip w: trips) {
            to.add(w.to().toString() + " (" + w.points() + ")");
        }

        String destinations = String.join(", ", to);

        String s = new String();

        if(trips.size() == 1){
           s = String.format("%s - %s", trips.get(0).from().toString(), destinations);
        }
        else
            s = String.format("%s - {%s}", trips.get(0).from().toString(), destinations);

        return s;
    }

    public String text(){
        return TEXT;
    }

    public int points(StationConnectivity connectivity){

        int i = 0;
        int temp = 0;

        for(Trip w: trips) {
          if(connectivity.connected(w.from(), w.to()) == true){
             if (w.points() > temp)
                 temp = w.points();
          }
          i = temp;
        }

        if (temp == 0){
           temp = trips.get(0).points();
            for(Trip w: trips) {
                if(w.points() < temp){
                    temp = w.points();
                }
            }
            i = temp;
        }
        return i;
    }

    @Override
    public int compareTo(Ticket that) {

        return this.compareTo(that);
    }



/*
    public static void main(String[] args) {

        Station Lausanne = new Station(0, "Lausanne");
        Station Bern = new Station(1, "Bern");
        Station Zurich = new Station(2, "Zurich");

        List<Trip> trip = new ArrayList<>();
        trip.add(new Trip(Lausanne, Bern, 9));
        trip.add(new Trip(Lausanne, Zurich, 20));



        Ticket ticket = new Ticket(trip);

        System.out.println(ticket.text());


    }

 */


}

