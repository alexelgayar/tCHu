package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.Constants.*;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Instanciable class, represents the observable state of a game in tCHu
 */
public final class ObservableGameState {
    private PublicGameState publicGameState;
    private PlayerState playerState;
    private final PlayerId playerId;

    private static final int TOTAL_TICKETS_COUNT = ChMap.tickets().size();

    //PublicGameState properties
    private ObjectProperty<PlayerId> currentPlayer = new SimpleObjectProperty<>();
    private final IntegerProperty ticketsPercentage = new SimpleIntegerProperty();
    private final IntegerProperty cardsPercentage = new SimpleIntegerProperty();
    private final List<ObjectProperty<Card>> faceUpCards = createFaceUpCards();
    private final Map<Route, ObjectProperty<PlayerId>> routes = createRoutes();

    //PublicPlayerState properties
    private final Map<PlayerId, IntegerProperty> playerTicketsCount = initProperties();
    private final Map<PlayerId, IntegerProperty> playerCardsCount = initProperties();
    private final Map<PlayerId, IntegerProperty> playerCarsCount = initProperties();
    private final Map<PlayerId, IntegerProperty> playerClaimPoints = initProperties();

    //PlayerState properties
    private final ObservableList<Ticket> playerTickets = FXCollections.observableArrayList();
    private final Map<Card, IntegerProperty> playerCardTypeCount = createPlayerCardTypeCount();
    private final Map<Route, BooleanProperty> playerCanClaimRoute = createPlayerCanClaimRoute();

    /**
     * Constructor for ObservableGameState
     *
     * @param playerId the identity of the player
     */
    public ObservableGameState(PlayerId playerId) {
        this.playerId = playerId;
    }

    /**
     * Updates all the values of the observableGameState attributes
     *
     * @param newGameState   the updated version of the gameState
     * @param newPlayerState the updated version of the player state
     */
    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        publicGameState = newGameState;
        playerState = newPlayerState;

        //1. Public Game State
        ticketsPercentage.set((HUNDRED_PERCENT * publicGameState.ticketsCount()) / TOTAL_TICKETS_COUNT);
        cardsPercentage.set((HUNDRED_PERCENT * publicGameState.cardState().deckSize()) / TOTAL_CARDS_COUNT);
        currentPlayer.set(newGameState.currentPlayerId());


        for (int slot : FACE_UP_CARD_SLOTS) {
            faceUpCards.get(slot).set(publicGameState.cardState().faceUpCard(slot));
        }

        for (Route route : routes.keySet()) {
            for (PlayerId id : PlayerId.ALL) {
                if (publicGameState.playerState(id).routes().contains(route))
                    routes.get(route).set(id);
            }
        }

        //2. Public Player State
        for (PlayerId id : PlayerId.ALL) {
            playerTicketsCount.get(id).set(publicGameState.playerState(id).ticketCount());
            playerCardsCount.get(id).set(publicGameState.playerState(id).cardCount());
            playerCarsCount.get(id).set(publicGameState.playerState(id).carCount());
            playerClaimPoints.get(id).set(publicGameState.playerState(id).claimPoints());
        }

        //3. Player State
        playerTickets.setAll(playerState.tickets().toList());

        playerCardTypeCount.forEach((card, count) -> playerCardTypeCount.get(card).set(playerState.cards().countOf(card)));

        for (Route route : routes.keySet()) {
            boolean routeDoubleNotOwned = true;

            for (Route w : routes.keySet()) {
                if (w.stations().containsAll(route.stations())) routeDoubleNotOwned = routes.get(w).get() == null;
            }

            playerCanClaimRoute.get(route).set(
                    playerState.canClaimRoute(route)
                            && publicGameState.currentPlayerId() == playerId
                            && routes.get(route).get() == null
                            && routeDoubleNotOwned);
        }

    }

    //1. PublicGameState Properties

    /**
     * Returns the percentage of tickets remaining in the pile
     *
     * @return the percentage of tickets remaining in the pile
     */
    public ReadOnlyIntegerProperty ticketsPercentage() {
        return ticketsPercentage;
    }

    /**
     * Returns the percentage of cards remaining in the pile
     *
     * @return the percentage of cards remaining in the pile
     */
    public ReadOnlyIntegerProperty cardsPercentage() {
        return cardsPercentage;
    }

    /**
     * Returns the face up card stored at the given slot
     *
     * @param slot the index of the face-up card to return
     * @return the face up card stored at the given slot
     */
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    /**
     * Returns the owner of the given route, otherwise null if the route is not owned
     *
     * @param route the route whose owner will be returned
     * @return the owner of the given route, otherwise null if the route is not owned
     */
    public ReadOnlyObjectProperty<PlayerId> routeOwner(Route route) {
        return routes.get(route);
    }

    //2. PublicPlayerState Properties

    /**
     * Returns the number of tickets that the player has in their hand
     *
     * @param id the identity of the player
     * @return the number of tickets that the player has in their hand
     */
    public ReadOnlyIntegerProperty playerTicketsCount(PlayerId id) {
        return playerTicketsCount.get(id);
    }

    /**
     * Returns the number of cards that the player has in their hand
     *
     * @param id the identity of the player
     * @return the number of cards that the player has in their hand
     */
    public ReadOnlyIntegerProperty playerCardsCount(PlayerId id) {
        return playerCardsCount.get(id);
    }

    /**
     * Returns the number of cars that the player owns
     *
     * @param id the identity of the player
     * @return the number of cars that the player owns
     */
    public ReadOnlyIntegerProperty playerCarsCount(PlayerId id) {
        return playerCarsCount.get(id);
    }

    /**
     * Returns the number of claim points that the player has obtained
     *
     * @param id the identity of the player
     * @return the number of claim points that the player has obtained
     */
    public ReadOnlyIntegerProperty playerClaimPoints(PlayerId id) {
        return playerClaimPoints.get(id);
    }

    //3. PlayerState Properties

    /**
     * Returns a list of the player tickets
     *
     * @return the list of the player tickets
     */
    public ObservableList<Ticket> playerTickets() {
        return FXCollections.unmodifiableObservableList(playerTickets);
    }

    /**
     * Returns the number of cards of each type that the player owns
     *
     * @param card the card whose number is needed
     * @return the number of cards of each type that the player owns
     */
    public ReadOnlyIntegerProperty playerCardTypeCount(Card card) {
        return playerCardTypeCount.get(card);
    }

    /**
     * Returns true if the player has the necessary prerequisites to claim the route, else false
     *
     * @param route the route that is desired to be claimed
     * @return true if the player has the necessary prerequisites to claim the route, else false
     */
    public ReadOnlyBooleanProperty claimable(Route route) {
        return playerCanClaimRoute.get(route);
    }

    //Additional methods

    /**
     * Returns whether it is possible to draw a ticket
     *
     * @return whether it is possible to draw a ticket
     */
    public ReadOnlyBooleanProperty canDrawTickets() {
        return new SimpleBooleanProperty(publicGameState.canDrawTickets());
    }

    /**
     * Returns whether it is possible to draw a card
     *
     * @return whether it is possible to draw a card
     */
    public ReadOnlyBooleanProperty canDrawCards() {
        return new SimpleBooleanProperty(publicGameState.canDrawCards());
    }

    /**
     * Returns a sortedBag of cards that the player can use to claim the given route
     *
     * @param route the route that the player wishes to claim
     * @return a sortedBag of cards that the player can use to claim the given route
     */
    public ObservableList<SortedBag<Card>> possibleClaimCards(Route route) {
        return FXCollections.unmodifiableObservableList(FXCollections.observableList(playerState.possibleClaimCards(route)));
    }

    public ReadOnlyObjectProperty<PlayerId> getCurrentPlayer(){
        return currentPlayer;
    }

    //Initializes the face up cards
    private static List<ObjectProperty<Card>> createFaceUpCards() {
        List<ObjectProperty<Card>> faceUpCards = new ArrayList<>(FACE_UP_CARDS_COUNT);

        for (int i = 0; i < FACE_UP_CARDS_COUNT; ++i) faceUpCards.add(new SimpleObjectProperty<>());

        return faceUpCards;
    }

    //Initializes the routes
    private static Map<Route, ObjectProperty<PlayerId>> createRoutes() {
        Map<Route, ObjectProperty<PlayerId>> routes = new HashMap<>();
        for (Route route : ChMap.routes()) routes.put(route, new SimpleObjectProperty<>());

        return routes;
    }

    //Initializes the players cards
    private static Map<Card, IntegerProperty> createPlayerCardTypeCount() {
        Map<Card, IntegerProperty> cardMap = new HashMap<>();

        for (Card card : Card.values()) cardMap.put(card, new SimpleIntegerProperty());

        return cardMap;
    }

    //Initializes the initial public playerState properties
    private static Map<PlayerId, IntegerProperty> initProperties() {
        Map<PlayerId, IntegerProperty> intMap = new HashMap<>();

        for (PlayerId id : PlayerId.ALL) intMap.put(id, new SimpleIntegerProperty());

        return intMap;
    }

    //Initializes the claimed routes
    private static Map<Route, BooleanProperty> createPlayerCanClaimRoute() {
        Map<Route, BooleanProperty> claimRouteMap = new HashMap<>();

        for (Route route : ChMap.routes()) claimRouteMap.put(route, new SimpleBooleanProperty());

        return claimRouteMap;
    }


}
