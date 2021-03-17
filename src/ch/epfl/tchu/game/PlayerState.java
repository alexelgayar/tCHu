package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 * Public, final, immutable class
 * Represents the completeness of a player. It inherits from PublicPlayerState.
 */
public final class PlayerState extends PublicPlayerState {

    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;

    private final int ticketPoints;

    /**
     * Builds the state of a player with the given tickets, maps and routes
     * @param tickets
     * @param cards
     * @param routes
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes){
        super(tickets.size(), cards.size(), routes);
        this.tickets = tickets;
        this.cards = cards;
        this.ticketPoints = ticketPoints();
    }


    /**
     * Method which returns the initial state of a player to whom the given initial cards were dealt.
     * In this initial state, the player does not yet have any tickets and has not taken any roads
     * @param initialCards
     * @return Returns the initial state of a player to whom the given initial cards were dealt.
     * @throws IllegalArgumentException if the number of initial cards is not equal to 4
     */
    public static PlayerState initial(SortedBag<Card> initialCards){
        Preconditions.checkArgument(initialCards.size() == 4);

        SortedBag<Ticket> initialTickets = SortedBag.of();
        List<Route> initialRoutes = new ArrayList<>();

        return new PlayerState(initialTickets, initialCards, initialRoutes);
    }

    /**
     * Method which returns the player's tickets
     * @return returns the player's tickets
     */
    public SortedBag<Ticket> tickets(){
        return tickets;
    }

    /**
     * Method which returns an identical state to the receiver, except that the player also has the given card
     * @param newTickets
     * @return
     */
    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets){
        SortedBag<Ticket> additionalTickets = tickets.union(newTickets);
        return new PlayerState(additionalTickets, cards, routes());
    }

    /**
     * Method which turns over the player's wagon/locomotive cards
     * @return returns the player's wagon/locomotive cards
     */
    public SortedBag<Card> cards(){
         return cards;
    }

    /**
     * Method which returns an identical state to the receiver, except that the player also has the given card
     * @param card a card to add to the list of cards of the player
     * @return returns an identical state to the receiver, except that the player also has the given card
     */
    public PlayerState withAddedCard(Card card){
        SortedBag<Card> newCards = cards.union(SortedBag.of(card));
        return new PlayerState(tickets, newCards, routes());
    }

    /**
     * Method which returns an identical state to the receiver, except that the player also has the given cards
     * @param additionalCards the cards to add to the list of cards of the player
     * @return returns an identical state to the receiver, except that the player also has the given cards
     */
    public PlayerState withAddedCards(SortedBag<Card> additionalCards){
        SortedBag<Card> newCards = cards.union(additionalCards);
        return new PlayerState(tickets, newCards, routes());
    }

    /**
     * Method which returns true IFF the player can seize the given route, i.e. if they have enough cars left and they have the necessary cards
     * @param route the route that is tested to see if it can be seized by the player
     * @return returns true IFF the player can seize the given route, i.e. if they have enough cars left and they have the necessary cards
     */
    public boolean canClaimRoute(Route route){
        //No need to throw exception here if player does not have enough cards, since possibleClaimCards throws an exception already
        boolean playerHasEnoughCars = (carCount() >= route.length());
        boolean playerHasNecessaryCards = (possibleClaimCards(route).contains(cards));
        return (playerHasEnoughCars && playerHasNecessaryCards);
    }

    /**
     * Method which returns the list of all the sets of cards the player could use to take possession of the given route
     * @param route
     * @return returns the list of all the sets of cards the player could use from their hand to take possession of the given route
     * @throws IllegalArgumentException if the player does not have enough cars to take the route
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route){
        Preconditions.checkArgument(carCount() >= route.length());
        List<SortedBag<Card>> allPossibleRouteCards = route.possibleClaimCards();

        //TODO: Would this cause ConcurrentModificationException?
        //Remove all the cards that the player does not have from allPossibleRouteCards
        allPossibleRouteCards.removeIf(sortedBag -> !sortedBag.contains(cards));

        return allPossibleRouteCards;
    }

    /**
     * Method which returns the list of all the sets of cards that the player could use to seize a tunnel, sorted in ascending order of the number of locomotive cards
     * Knowing that they initially laid the cards initialCards, that the 3 draw cards from the top of the draw pile are drawnCards
     * And that these latter force the player to lay down more additionalCardsCount cards
     * @param additionalCardsCount
     * @param initialCards
     * @param drawnCards
     * @return
     * @throws IllegalArgumentException if the number of additional cards is not between 1 and 3 (inclusive), if the set of initial cards is empty or contains more than 2 different types of cards, or if the set of cards drawn does not contain exactly 3 cards
     */
    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards, SortedBag<Card> drawnCards){
        boolean additionalCardsCountIsCorrect = (additionalCardsCount >= 1) && (additionalCardsCount <= 3);
        boolean initialCardsIsNotEmpty = (!initialCards.isEmpty());
        boolean drawnCardsExactlyThree = (drawnCards.size() == 3);

        Collection<Card> initialCardsSet = initialCards.toSet(); //Removes doubles
        boolean initialCardsContainsNoMoreThanTwoCardTypes = (initialCardsSet.size() <= 2);

        Preconditions.checkArgument(additionalCardsCountIsCorrect && initialCardsIsNotEmpty && initialCardsContainsNoMoreThanTwoCardTypes && drawnCardsExactlyThree);

        List<SortedBag<Card>> possibleCards = new ArrayList<>();

        for(Card w: drawnCards){
            if(initialCards.contains(w) && (w != Card.LOCOMOTIVE) ){
                for(int i = 0; i < additionalCardsCount; ++i){
                    possibleCards.add(SortedBag.of(additionalCardsCount - i, w, i, Card.LOCOMOTIVE));
                }
                break;
            }
        }
        possibleCards.add(SortedBag.of(additionalCardsCount, Card.LOCOMOTIVE));

        return possibleCards;
    }

    /**
     * Method which returns an identical state to the receive, except that the player has also seized the given route by means of the given cards
     * @param route
     * @param claimCards
     * @return
     */
    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards){
        SortedBag<Card> playerCards = cards.difference(claimCards);
        List<Route> playerRoutes = new ArrayList<>(routes());
        playerRoutes.add(route);

        return new PlayerState(tickets, playerCards, playerRoutes);
    }

    /**
     * Method which returns the number of points - possibly negative- obtained by the player thanks to his tickets
     * @return returns the number of points - possibly negative- obtained by the player thanks to his tickets
     */
    public int ticketPoints(){

        int temp = 0;
        int points = 0;

        for (Route w : routes()){

            if(w.station1().id() > temp){
                temp = w.station1().id();
            }
            if(w.station2().id() > temp){
                temp = w.station2().id();
            }

        }

       StationPartition.Builder partitionBuilder = new StationPartition.Builder(temp) ;

        for(Route w : routes()){
            partitionBuilder.connect(w.station1(), w.station2());
        }

      StationPartition partition = partitionBuilder.build();

        for(Ticket w: tickets){
            points += w.points(partition);

        }
        return points;
    }

    /**
     * Method which returns all the points obtained by the player at the end of the game, namely the sum of the points returned by the methods claimPoints and ticketPoints.
     * @return returns all the points obtained by the player at the end of the game, namely the sum of the points returned by the methods claimPoints and ticketPoints.
     */
    public int finalPoints(){
        return claimPoints() + ticketPoints;
    }
}
