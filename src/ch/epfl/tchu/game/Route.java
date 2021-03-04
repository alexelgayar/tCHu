package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Public, final immutable class
 * Represents a road connecting two neighboring towns
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 * */
public final class Route {

    private String id;
    private Station station1;
    private Station station2;
    private int length;
    private Level level;
    private Color color;

    /**
     *
     * @param id
     * @param station1
     * @param station2
     * @param length
     * @param level
     * @param color
     * @throws IllegalArgumentException if (station1 is the same as station2) or (length isn't contained within Constants range ([MIN_ROUTE_LENGTH, MAX_ROUTE_LENGTH]))
     * @throws NullPointerException if id, either station or the level is null
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color) {

        boolean stationsNotSame = !(station1.toString().equals(station2.toString()));
        boolean lengthIsValid = ((length) >= Constants.MIN_ROUTE_LENGTH) && ((length <= Constants.MAX_ROUTE_LENGTH));

        Preconditions.checkArgument(stationsNotSame && lengthIsValid);

        this.id = Objects.requireNonNull(id);
        this.station1 = Objects.requireNonNull(station1);
        this.station2 = Objects.requireNonNull(station2);
        this.length = length;
        this.level = Objects.requireNonNull(level);
        this.color = color;
    }

    /**
     * Getter method for route id
     * @return Returns the route id
     */
    public String id() {
        return id;
    }

    /**
     * Getter method for the first station of the route
     * @return Returns the first station of the route
     */
    public Station station1() {
        return station1;
    }

    /**
     * Getter method for the second station of the route
     * @return Returns the second station of the route
     */
    public Station station2() {
        return station2;
    }

    /**
     * Getter method for the length of the route
     * @return Returns the (int) length of the route
     */
    public int length() {
        return length;
    }

    /**
     * Getter method for the level
     * @return Returns the level type for the route (Surface level or Tunnel level)
     */
    public Level level() {
        return level;
    }

    /**
     * Getter method for the color
     * @return Returns the color of the route, or null if the route is of neutral colour
     */
    public Color color() {
        return color;
    }

    /**
     * @return Returns the list of two stations of the route, in the order they were passed into the constructor
     */
    public List<Station> stations(){
        Station[] stationsArray = new Station[]{station1, station2};
        return List.of(stationsArray);
    }

    /**
     *
     * @param station Enter one of two of the stations belonging to the route. If station does not belong => IllegalArgumentException
     * @return Returns the station of the route that is not the one given through the parameter else IllegalArgumentException if station provided is neither first or second of the route
     * @throws IllegalArgumentException if input parameter station is neither station 1 nor station 2
     */
    public Station stationOpposite(Station station){
        Preconditions.checkArgument(station.equals(station1) || station.equals(station2));

        //If station = station1, then return station2. If station = station2, then return station1
        return (station.equals(station1) ? station2:station1);
    }

    /**
     *
     * @return Returns the list of all the sets of cards that could be played to (attempt to) grab the road, sorted in ascending order of number of locomotive cards, then by suit
     */
    public List<SortedBag<Card>> possibleClaimCards() {

        List<SortedBag<Card>> possibleCards = new ArrayList<>();

        if (level == Level.OVERGROUND) {
            if (color != null) {
                possibleCards.add(SortedBag.of(length, Card.of(color)));
            }

         else {
             for(Card w: Card.CARS){
                 possibleCards.add(SortedBag.of(length, w));
             }
        }
    }

        else{
            if (color != null){
                for(int i = 0; i <= length; ++i){
                    possibleCards.add(SortedBag.of(length - i, Card.of(color), i ,Card.LOCOMOTIVE));
                }
            }

            else{
                for(int i =0; i <= length - 1; ++i){
                    for(Card w: Card.CARS){
                        possibleCards.add(SortedBag.of(length - i, w, i, Card.LOCOMOTIVE));
                    }
                }
                possibleCards.add(SortedBag.of(length, Card.LOCOMOTIVE));
            }
        }
        return possibleCards;
    }

    /**
     *
     * @param claimCards
     * @param drawnCards
     * @return Returns the number of additonal cards to be played to seize the road (in tunnel) knowing that the player initially laid the cards claimCards and that the three cards drawn from the top of the pile are drawnCards
     * @throws IllegalArgumentException if the road to which it is applied is not a tunnel, or if drawnCards does not contain exactly 3 cards
     */
    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards){
        Preconditions.checkArgument((level == Level.UNDERGROUND) && (drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS));

        int counter = 0;

        for(Card w: drawnCards){
            if(claimCards.contains(w) || (w == Card.LOCOMOTIVE) ){
                counter += 1;
            }
        }

        return counter;
    }

    /**
     *
     * @return Returns the number of construction points that the player gains if they create the route
     */
    public int claimPoints(){
        return Constants.ROUTE_CLAIM_POINTS.get(length);
    }
    /**
     * Represents the two levels that a route can connect two neighbouring cities
     * These values, in order, are: OVERGROUND (surface route) and UNDERGROUND (tunnel route)
     */
    public enum Level{
        OVERGROUND,
        UNDERGROUND;
    }
}
