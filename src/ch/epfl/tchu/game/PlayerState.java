package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;

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

        //TODO: How do I store the values in an immutable way?
        this.tickets = tickets;
        this.cards = cards;

        //TODO: Should the ticketPoints be stored as an attribute of the class?
        this.ticketPoints = ticketPoints();
    }

    //TODO: Is this a public static method or simply static?

    /**
     * Method which returns the initial state of a player to whom the given initial cards were dealt.
     * In this initial state, the player does not yet have any tickets and has not taken any roads
     * @param initialCards
     * @return Returns the initial state of a player to whom the given initial cards were dealt.
     * @throws IllegalArgumentException if the number of initial cards is not equal to 4
     */
    static PlayerState initial(SortedBag<Card> initialCards){
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
        //TODO: Is this how to create a union with a new sortedBag?
        SortedBag<Ticket> additionalTickets = SortedBag.of(tickets).union(newTickets);
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
        //TODO: Is this how to create a union with a new SortedBag?
        SortedBag<Card> newCards = SortedBag.of(cards).union(SortedBag.of(card));
        return new PlayerState(tickets, newCards, routes());
    }

    /**
     * Method which returns an identical state to the receiver, except that the player also has the given cards
     * @param additionalCards the cards to add to the list of cards of the player
     * @return returns an identical state to the receiver, except that the player also has the given cards
     */
    public PlayerState withAddedCards(SortedBag<Card> additionalCards){
        //TODO: Is this how to create a union with a new SortedBag?
        SortedBag<Card> newCards = SortedBag.of(cards).union(additionalCards);
        return new PlayerState(tickets, newCards, routes());
    }

    /**
     * Method which returns true IFF the player can seize the given route, i.e. if they have enough cars left and they have the necessary cards
     * @param route the route that is tested to see if it can be seized by the player
     * @return returns true IFF the player can seize the given route, i.e. if they have enough cars left and they have the necessary cards
     */
    public boolean canClaimRoute(Route route){
        //TODO: What are the necessary cards?
        boolean playerHasNecessaryCards = cardCount() >= route.length();

        return (carsCount() >= route.length() && cardCount() >= route.length());
    }

    /**
     * Method which returns the list of all the sets of cards the player could use to take possession of the given route
     * @param route
     * @return returns the list of all the sets of cards the player could use to take possession of the given route
     * @throws IllegalArgumentException if the player does not have enough cars to take the route
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route){
        Preconditions.checkArgument(carsCount() >= route.length());

        //TODO: How do I check all the routes here?
        return null;
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

        return null;
    }

    /**
     * Method which returns an identical state to the receive, except that the player has also seized the given route by means of the given cards
     * @param route
     * @param claimCards
     * @return
     */
    PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards){
        //TODO: Do I remove the claimCards from the the player list?
        SortedBag<Card> playerCards = SortedBag.of(cards.difference(claimCards));
        List<Route> playerRoutes = new ArrayList<>(routes());
        playerRoutes.add(route);

        return new PlayerState(tickets, playerCards, playerRoutes);
    }

    /**
     * Method which returns the number of points - possibly negative- obtained by the player thanks to his tickets
     * @return returns the number of points - possibly negative- obtained by the player thanks to his tickets
     */
    int ticketPoints(){

        return 0;
    }

    /**
     * Method which returns all the points obtained by the player at the end of the game, namely the sum of the points returned by the methods claimPoints and ticketPoints.
     * @return returns all the points obtained by the player at the end of the game, namely the sum of the points returned by the methods claimPoints and ticketPoints.
     */
    int finalPoints(){
        return claimPoints() + ticketPoints;
    }
}
