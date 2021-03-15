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
    private final int carsCount, earnedClaimPoints;

    /*
    1. tickets
    2. wagon/locomotive cards
    3. roads seized
    4. wagons in possession
    5. construction points already earned
    6. points his tickets will earn him

    => 4,5,6 can be computed from the others
     */

    /**
     * Unique Contructor for the PublicPlayerState class
     * @param ticketCount number of tickets owned
     * @param cardCount number of cards owned by the player
     * @param routes routes seized by player
     * @throws IllegalArgumentException if number of tickets or number of cards is strictly negative
     */
    public PublicPlayerState(int ticketCount, int cardCount, List<Route> routes){
        Preconditions.checkArgument(ticketCount >= 0 && cardCount >= 0);

        //TODO: How do I store the values in an immutable way?
        this.ticketCount = ticketCount;
        this.cardCount = cardCount;
        this.routes = List.copyOf(routes);


        HashMap<String, Integer> carPointsPair = computeCarsPointsPair(routes);

        int carsUsed = carPointsPair.get("length");
        this.carsCount = Constants.INITIAL_CAR_COUNT - carsUsed;
        this.earnedClaimPoints = carPointsPair.get("claimPoints");
    }

    //TODO: Is this a clean/good way of storing the points?
    private HashMap<String, Integer> computeCarsPointsPair(List<Route> routes){
        int totalLength = 0;
        int totalClaimPoints = 0;
        for (Route route : routes){
            totalLength += route.length();
            totalClaimPoints += route.claimPoints();
        }

        HashMap<String, Integer> lengthPointsPair = new HashMap<>();

        lengthPointsPair.put("length", totalLength);
        lengthPointsPair.put("claimPoints", totalClaimPoints);

        return lengthPointsPair;
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

    public int carsCount(){
        return carsCount;
    }

    public int claimPoints(){
        return earnedClaimPoints;
    }


}
