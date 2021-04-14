package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, immutable class
 * Represents the public part of a player's state, namely the number of tickets, cards and wagons they own, the roads they have seized and the number of construction points they have obtained
 */
public class PublicPlayerState {

    private final int ticketCount, cardCount, carCount, earnedClaimPoints;
    private final List<Route> routes;

    /**
     * Unique Constructor for the PublicPlayerState class
     * @param ticketCount number of tickets owned
     * @param cardCount number of cards owned by the player
     * @param routes routes seized by player
     * @throws IllegalArgumentException if number of tickets or number of cards is strictly negative
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes){
        Preconditions.checkArgument(ticketCount >= 0 && cardCount >= 0);

        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = List.copyOf(routes);

        int carsUsed = 0;
        int totalClaimPoints = 0;

        for (Route route : routes){
            carsUsed += route.length();
            totalClaimPoints += route.claimPoints();
        }

        this.carCount = Constants.INITIAL_CAR_COUNT - carsUsed;
        this.earnedClaimPoints = totalClaimPoints;
    }

    /**
     * Returns the number of tickets the player has
     * @return the number of tickets the player has
     */
    public int ticketCount(){
        return ticketCount;
    }

    /**
     * Returns the number of cards the player has
     * @return the number of cards the player has
     */
    public int cardCount(){
        return cardCount;
    }

    /**
     * Returns the roads that the player has seized
     * @return the roads that the player has seized
     */
    public List<Route> routes(){
        return routes;
    }

    /**
     * Returns the number of cars the player has
     * @return the number of cars the player has
     */
    public int carCount(){
        return carCount;
    }

    /**
     * Returns the number of construction points obtained by the player
     * @return the number of construction points obtained by the player
     */
    public int claimPoints(){
        return earnedClaimPoints;
    }
}
