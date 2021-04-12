package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

import static ch.epfl.tchu.game.Constants.MAX_ROUTE_LENGTH;
import static ch.epfl.tchu.game.Constants.MIN_ROUTE_LENGTH;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, final, immutable class. Represents a road connecting two neighboring towns
 * */
public final class Route {

    //TODO: Should I move Level down to the bottom
    /**
     * Represents the two levels that a route can connect two neighbouring cities
     * These values, in order, are: OVERGROUND (surface route) and UNDERGROUND (tunnel route)
     */
    public enum Level{
        OVERGROUND,
        UNDERGROUND
    }

    private final String id;
    private final Station station1;
    private final Station station2;
    private final int length;
    private final Level level;
    private final Color color;

    /**
     * Constructs a route with an identity, stations, length, level and the given colour.
     * @param id the identity of the route
     * @param station1 that first station of the route
     * @param station2 the second station of the route
     * @param length the length of the route
     * @param level the level of the route (either OVERGROUND or UNDERGROUND)
     * @param color the colour of the route
     * @throws IllegalArgumentException if (station1 is the same as station2) or (length isn't contained within Constants range ([MIN_ROUTE_LENGTH, MAX_ROUTE_LENGTH]))
     * @throws NullPointerException if id, either station or the level is null
     */
    public Route(String id, Station station1, Station station2, int length, Level level, Color color) {
        boolean stationsNotSame = !(station1.equals(station2));
        boolean lengthIsValid = (length >= MIN_ROUTE_LENGTH && length <= MAX_ROUTE_LENGTH);

        Preconditions.checkArgument(stationsNotSame && lengthIsValid);

        this.id = Objects.requireNonNull(id);
        this.station1 = Objects.requireNonNull(station1);
        this.station2 = Objects.requireNonNull(station2);
        this.length = length;
        this.level = Objects.requireNonNull(level);
        this.color = color;
    }

    /**
     * Returns the route id
     * @return the route id
     */
    public String id() {
        return id;
    }

    /**
     * Returns the first station of the route
     * @return the first station of the route
     */
    public Station station1() {
        return station1;
    }

    /**
     * Returns the second station of the route
     * @return the second station of the route
     */
    public Station station2() {
        return station2;
    }

    /**
     * Returns the length of the route
     * @return the length of the route
     */
    public int length() {
        return length;
    }

    /**
     * Returns the level type for the route (OVERGROUND or UNDERGROUND)
     * @return the level type for the route (OVERGROUND or UNDERGROUND)
     */
    public Level level() {
        return level;
    }

    /**
     * Returns the color of the route, or null if the route is of neutral colour
     * @return the color of the route, or null if the route is of neutral colour
     */
    public Color color() {
        return color;
    }

    /**
     * Returns a list containing the two stations of the route, in the order they are passed into the constructor
     * @return a list containing the two stations of the route, in the order they are passed into the constructor
     */
    public List<Station> stations(){
        Station[] stationsArray = new Station[]{station1, station2};
        return List.of(stationsArray);
    }

    /**
     * Returns the station of the route, that is not the one provided.
     * @param station the station at one end of the route
     * @return the station at the other end of the route, not equal to the provided station
     * @throws IllegalArgumentException if input station is neither station 1 nor station 2
     */
    public Station stationOpposite(Station station){
        Preconditions.checkArgument(station.equals(station1) || station.equals(station2));
        return (station.equals(station1) ? station2:station1);
    }

    /**
     * Returns a list of all the sets of cards that could be played to try claim a route, sorted by increasing order of the number of locomotives, then by color
     * @return a list of all the sets of cards that could be played to try claim a route, sorted by increasing order of the number of locomotives, then by color
     */
    public List<SortedBag<Card>> possibleClaimCards() {
        List<SortedBag<Card>> possibleCards = new ArrayList<>();

        int maxLocomotives = (level == Level.UNDERGROUND ? length : 0);

        for (int i = 0; i <= maxLocomotives; ++i){
            List<Color> colors = (color != null) ? List.of(color): Color.ALL;

            for (Color color: colors) {
                SortedBag<Card> cards = SortedBag.of(length - i, Card.of(color), i, Card.LOCOMOTIVE);
                if (!possibleCards.contains(cards)) {
                    possibleCards.add(cards);
                }
            }
        }

        return possibleCards;
    }

    /**
     * Returns the number of additional cards to play to claim an UNDERGROUND route, knowing the player initially placed claimCards and the three cards from the deck is drawnCards
     * @param claimCards the cards the player initially placed to try claim the UNDERGROUND route
     * @param drawnCards the three cards drawn from the top of the deck
     * @return the number of additional cards to play to claim an UNDERGROUND route, knowing the player initially placed claimCards and the three cards from the deck is drawnCards
     * @throws IllegalArgumentException if the road to which it is applied is not a tunnel, or if drawnCards does not contain exactly 3 cards
     */
    public int additionalClaimCardsCount(SortedBag<Card> claimCards, SortedBag<Card> drawnCards){
        Preconditions.checkArgument((level == Level.UNDERGROUND) && (drawnCards.size() == Constants.ADDITIONAL_TUNNEL_CARDS));

        int counter = 0;

        for(Card c: drawnCards){
            if(claimCards.contains(c) || (c == Card.LOCOMOTIVE) ){
                counter += 1;
            }
        }

        return counter;
    }

    /**
     * Returns the number of construction points that a player obtains when they claim the route
     * @return the number of construction points that a player obtains when they claim the route
     */
    public int claimPoints(){
        return Constants.ROUTE_CLAIM_POINTS.get(length);
    }


}
