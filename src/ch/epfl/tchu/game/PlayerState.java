package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.*;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, final, immutable class
 * Represents the completeness of a player. It inherits from PublicPlayerState.
 */
public final class PlayerState extends PublicPlayerState {

    private final SortedBag<Ticket> tickets;
    private final SortedBag<Card> cards;
    private final int ticketPoints;

    /**
     * Builds the state of a player owning the given tickets, cards and routes
     * @param tickets the tickets the player owns
     * @param cards the cards the player owns
     * @param routes the routes the player owns
     */
    public PlayerState(SortedBag<Ticket> tickets, SortedBag<Card> cards, List<Route> routes) {
        super(tickets.size(), cards.size(), routes);
        this.tickets = tickets;
        this.cards = cards;
        this.ticketPoints = ticketPoints();
    }

    /**
     * Returns the initial state of a player to whom the given initial cards were dealt. In the initial state, player has no tickets, nor any roads.
     * @param initialCards the initial cards dealt to the player
     * @return the initial state of a player to whom the given initial cards were dealt. In the initial state, player has no tickets, nor any roads.
     * @throws IllegalArgumentException if the number of initial cards is not equal to 4
     */
    public static PlayerState initial(SortedBag<Card> initialCards) {
        Preconditions.checkArgument(initialCards.size() == 4);

        SortedBag<Ticket> initialTickets = SortedBag.of();
        List<Route> initialRoutes = new ArrayList<>();

        return new PlayerState(initialTickets, initialCards, initialRoutes);
    }

    /**
     * Returns the player's tickets
     * @return the player's tickets
     */
    public SortedBag<Ticket> tickets() {
        return tickets;
    }

    /**
     * Returns an identical state to the receiver, except that the player also has the given tickets
     * @param newTickets the tickets to add to the player
     * @return an identical state to the receiver, except that the player also has the given tickets
     */
    public PlayerState withAddedTickets(SortedBag<Ticket> newTickets) {
        SortedBag<Ticket> additionalTickets = tickets.union(newTickets);
        return new PlayerState(additionalTickets, cards, routes());
    }

    /**
     * Turns over the player's wagon/locomotive cards
     * @return the player's wagon/locomotive cards
     */
    public SortedBag<Card> cards() {
        return cards;
    }

    /**
     * Method which returns an identical state to the receiver, except that the player also has the given card
     * @param card a card to add to the list of cards of the player
     * @return an identical state to the receiver, except that the player also has the given card
     */
    public PlayerState withAddedCard(Card card) {
        SortedBag<Card> newCards = cards.union(SortedBag.of(card));
        return new PlayerState(tickets, newCards, routes());
    }

    /**
     * Method which returns true IFF the player can seize the given route, i.e. if they have enough cars left and they have the necessary cards
     * @param route the route that is tested to see if it can be seized by the player
     * @return returns true IFF the player can seize the given route, i.e. if they have enough cars left and they have the necessary cards
     */
    public boolean canClaimRoute(Route route) {
        boolean playerHasEnoughCars = (carCount() >= route.length());
        boolean playerHasNecessaryCards = false;

        if (playerHasEnoughCars){
            playerHasNecessaryCards = (!possibleClaimCards(route).isEmpty());
        }

        return (playerHasEnoughCars && playerHasNecessaryCards);
    }

    /**
     * Returns the list of all the sets of cards the player could use to take possession of the given route
     * @param route the route that the player wishes to claim
     * @return the list of all the sets of cards the player could use from their hand to take possession of the given route
     * @throws IllegalArgumentException if the player does not have enough cards to take the route
     */
    public List<SortedBag<Card>> possibleClaimCards(Route route) {
        Preconditions.checkArgument(carCount() >= route.length());

        List<SortedBag<Card>> allPossibleRouteCards = route.possibleClaimCards();

        Set<SortedBag<Card>> allCardCombinations = (cards.size() >= route.length())
                ? cards.subsetsOfSize(route.length())
                : new HashSet<>();

        List<SortedBag<Card>> filteredCards = filterCards(allPossibleRouteCards, allCardCombinations);

        return sortList(filteredCards);
    }

    //Filters the the possible route claim card combinations such that only those that the player can obtain are returned
    private List<SortedBag<Card>> filterCards(List<SortedBag<Card>> allPossibleRouteCards, Set<SortedBag<Card>> allCardCombinations){
        List<SortedBag<Card>> filteredCards = new ArrayList<>();

        for (Card card: Card.ALL) {
            for (SortedBag<Card> cardCombination : allCardCombinations) {
                if (allPossibleRouteCards.contains(cardCombination) && cardCombination.get(0).equals(card)) {
                    filteredCards.add(cardCombination);
                }
            }
        }

        return filteredCards;
    }

    /**
     * Returns the list of all the sets of cards that the player could use to seize a tunnel, sorted in ascending order of the number of locomotive cards
     * Knowing that they initially laid the cards initialCards, that the 3 draw cards from the top of the draw pile are drawnCards
     * And that these latter force the player to lay down more additionalCardsCount cards
     *
     * @param additionalCardsCount the number of additional cards that must be drawn
     * @param initialCards the cards initially used to claim the route
     * @return the list of all the sets of cards that the player could use to seize a tunnel, sorted in ascending order of the number of locomotive cards
     * @throws IllegalArgumentException if the number of additional cards is not between 1 and 3 (inclusive), if the set of initial cards is empty or contains more than 2 different types of cards, or if the set of cards drawn does not contain exactly 3 cards
     */
    public List<SortedBag<Card>> possibleAdditionalCards(int additionalCardsCount, SortedBag<Card> initialCards) {
        //===== Preconditions Check =====//
        boolean additionalCardsCorrect = (additionalCardsCount >= 1) && (additionalCardsCount <= 3);
        boolean initialCardsNotEmpty = (!initialCards.isEmpty());
        boolean initialCardsTwoTypes = (initialCards.toSet().size() <= 2);
        Preconditions.checkArgument(additionalCardsCorrect && initialCardsNotEmpty && initialCardsTwoTypes);

        //===== Computing Possible Cards =====//
        SortedBag<Card> remainingCards = cards.difference(initialCards);

        List<Card> additionalClaimCards = computeAdditionalClaimCards(initialCards, remainingCards);
        Set<SortedBag<Card>> possibleAdditionalPlayerCards = computePossibleAdditionalPlayerCards(additionalClaimCards, remainingCards, additionalCardsCount);

        List<SortedBag<Card>> options = new ArrayList<>(possibleAdditionalPlayerCards);

        return sortList(options);
    }

    //Sort cards in ascending locomotive order
    private List<SortedBag<Card>> sortList(List<SortedBag<Card>> unsortedList){
        List<SortedBag<Card>> list = new ArrayList<>(unsortedList);
        list.sort(
                Comparator.comparingInt(cs -> cs.countOf(Card.LOCOMOTIVE)));
        return list;
    }

    //Computes what additional cards the player must player, to seize the tunnel
    private List<Card> computeAdditionalClaimCards(SortedBag<Card> initialCards, SortedBag<Card> remainingCards) {
        List<Card> possibleClaimCards = new ArrayList<>();

        for (Card card: remainingCards){
            if(initialCards.contains((card)) || card == Card.LOCOMOTIVE){
                possibleClaimCards.add(card);
            }
        }

        return possibleClaimCards;
    }

    //Computes all the possible combinations of cards, belonging to the player, that the player can play
    private Set<SortedBag<Card>> computePossibleAdditionalPlayerCards(List<Card> possibleClaimCards, SortedBag<Card> remainingCards, int additionalCardsCount){
        Set<SortedBag<Card>> possibleSubsets = (SortedBag.of(possibleClaimCards).size() >= additionalCardsCount)
                ? SortedBag.of(possibleClaimCards).subsetsOfSize(additionalCardsCount)
                : new HashSet<>();

        Set<SortedBag<Card>> possibleAdditionalCards = new HashSet<>();

        Set<SortedBag<Card>> allSubsets = (remainingCards.size() >= additionalCardsCount)
                ? remainingCards.subsetsOfSize(additionalCardsCount)
                : new HashSet<>();

        for(SortedBag<Card> s : allSubsets){
            if(possibleSubsets.contains(s)){
                possibleAdditionalCards.add(s);
            }
        }
        return possibleAdditionalCards;
    }


    /**
     * Returns an identical state to the receive, except that the player has also seized the given route by means of the given cards
     * @param route the route that the player has seized
     * @param claimCards the cards that the player uses to seize the route
     * @return an identical state to the receive, except that the player has also seized the given route by means of the given cards
     */
    public PlayerState withClaimedRoute(Route route, SortedBag<Card> claimCards) {
        SortedBag<Card> playerCards = cards.difference(claimCards);
        List<Route> playerRoutes = new ArrayList<>(routes());
        playerRoutes.add(route);

        return new PlayerState(tickets, playerCards, playerRoutes);
    }

    /**
     * Returns the number of points, possibly negative, obtained by the player thanks to his tickets
     * @return the number of points, possibly negative, obtained by the player thanks to his tickets
     */
    public int ticketPoints() {
        int temp = 0;
        int points = 0;

        for (Route w : routes()) {
            if (w.station1().id() > temp) {
                temp = w.station1().id();
            }
            if (w.station2().id() > temp) {
                temp = w.station2().id();
            }
        }

        StationPartition.Builder partitionBuilder = new StationPartition.Builder(temp + 1);

        for (Route w : routes()) {
            partitionBuilder.connect(w.station1(), w.station2());
        }

        StationPartition partition = partitionBuilder.build();

        for (Ticket w : tickets) {
            points += w.points(partition);

        }
        return points;
    }

    /**
     * Returns all the points obtained by the player at the end of the game, namely the sum of the points returned by the methods claimPoints and ticketPoints.
     * @return all the points obtained by the player at the end of the game, namely the sum of the points returned by the methods claimPoints and ticketPoints.
     */
    public int finalPoints() {
        return claimPoints() + ticketPoints;
    }
}
