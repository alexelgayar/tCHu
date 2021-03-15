package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.HashMap;
import java.util.List;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 * Public, immutable class
 * Represents the public part of a player's state, namely the number of tickets, cards and wagons they own, the roads they have seized and the number of construction points they have obtained
 */
public class PublicPlayerState {

    private final int ticketCount, cardCount;
    private final List<Route> routes;
    private final int carCount, earnedClaimPoints;

    /**
     * Unique Contructor for the PublicPlayerState class
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

    public int ticketCount(){
        return ticketCount;
    }

    public int cardCount(){
        return cardCount;
    }

    public List<Route> routes(){
        return routes;
    }

    public int carCount(){
        return carCount;
    }

    public int claimPoints(){
        return earnedClaimPoints;
    }
}
