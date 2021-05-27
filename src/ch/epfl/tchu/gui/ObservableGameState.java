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
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Instanciable class, represents the observable state of a game in tCHu
 */
public class ObservableGameState {
    private PublicGameState publicGameState;
    private PlayerState playerState;
    private final PlayerId playerId;

    private static final int TOTAL_TICKETS_COUNT = ChMap.tickets().size();

    //PublicGameState properties
    private final IntegerProperty ticketsPercentage = new SimpleIntegerProperty(0);
    private final IntegerProperty cardsPercentage = new SimpleIntegerProperty(0);
    private final List<ObjectProperty<Card>> faceUpCards = createFaceUpCards();
    private final Map<Route, ObjectProperty<PlayerId>> routes = createRoutes();

    //PublicPlayerState properties
    private final Map<PlayerId, IntegerProperty> playersTicketsCount = initProperties();
    private final Map<PlayerId, IntegerProperty> playersCardsCount = initProperties();
    private final Map<PlayerId, IntegerProperty> playersCarsCount = initProperties();
    private final Map<PlayerId, IntegerProperty> playersClaimPoints = initProperties();

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
    public void setState(PublicGameState newGameState, PlayerState newPlayerState) { //TODO: Clean up the code here
        publicGameState = newGameState;
        playerState = newPlayerState;

        //1. Public Game State
        ticketsPercentage.set((100 * publicGameState.ticketsCount()) / TOTAL_TICKETS_COUNT);
        cardsPercentage.set((100 * publicGameState.cardState().deckSize()) / TOTAL_CARDS_COUNT);

        for (int slot : FACE_UP_CARD_SLOTS) {
            Card newCard = newGameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }


        for (Route route : routes.keySet()) {
            for (PlayerId id : PlayerId.ALL) {
                if (newGameState.playerState(id).routes().contains(route))
                    routes.get(route).set(id);
            }
        }

        //2. Public Player State
        for (PlayerId id : PlayerId.ALL) {
            playersTicketsCount.get(id).set(newGameState.playerState(id).ticketCount());
            playersCardsCount.get(id).set(newGameState.playerState(id).cardCount());
            playersCarsCount.get(id).set(newGameState.playerState(id).carCount());
            playersClaimPoints.get(id).set(newGameState.playerState(id).claimPoints());
        }

        //3. Player State
        if (playerTickets.size() == 0) {
            playerTickets.addAll(playerState.tickets().toList());
        } else {
            playerTickets.setAll(playerState.tickets().toList());
        }

        for (Card card : Card.values()) {

            playerCardTypeCount.get(card).set(playerState.cards().countOf(card));

        }


        for (Route route : routes.keySet()) {

            boolean correctPlayer = publicGameState.currentPlayerId() == playerId;
            boolean routeNotOwned = routes.get(route).get() == null;
            boolean routeDoubleNotOwned = true;

            for (Route w : routes.keySet()) {
                if (w.id() == route.id()) continue;
                if (w.stations().containsAll(route.stations())) {
                    routeDoubleNotOwned = routes.get(w).get() == null;
                }
            }

            boolean playerHasCards = playerState.canClaimRoute(route);
            playerCanClaimRoute.get(route).set(playerHasCards && correctPlayer && routeDoubleNotOwned && routeNotOwned);
        }
    }

    //TODO: Reorganise code set up
    /**
     * Returns the percentage of tickets remaining in the pile
     * @return the percentage of tickets remaining in the pile
     */
    public ReadOnlyIntegerProperty ticketsPercentage() { //TODO Clean up all the smaller methods
        return ticketsPercentage;
    }

    /**
     * Returns the percentage of cards remaining in the pile
     * @return the percentage of cards remaining in the pile
     */
    public ReadOnlyIntegerProperty cardsPercentage() {
        return cardsPercentage;
    }

    private static List<ObjectProperty<Card>> createFaceUpCards() {
        List<ObjectProperty<Card>> faceUpCards = new ArrayList<>(FACE_UP_CARDS_COUNT);

        for (int i = 0; i < FACE_UP_CARDS_COUNT; ++i) {
            faceUpCards.add(new SimpleObjectProperty<>(null));
        }

        return faceUpCards;
    }

    /**
     * Returns the face up card stored at the given slot
     * @param slot the index of the faceup card to return
     * @return the face up card stored at the given slot
     */
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    private static Map<Route, ObjectProperty<PlayerId>> createRoutes() {
        Map<Route, ObjectProperty<PlayerId>> routes = new HashMap<>();
        for (Route route : ChMap.routes())
            routes.put(route, new SimpleObjectProperty<>(null));

        return routes;
    }

    /**
     * Returns the owner of the given route, otherwise null if the route is not owned
     * @param route the route whose owner will be returned
     * @return the owner of the given route, otherwise null if the route is not owned
     */
    public ReadOnlyObjectProperty<PlayerId> routeOwner(Route route) {
        return routes.get(route);
    }


    //2. PublicPlayerState Properties

    /**
     * Returns the number of tickets that the player has in their hand
     * @param id the identity of the player
     * @return the number of tickets that the player has in their hand
     */
    public ReadOnlyIntegerProperty playerTicketsCount(PlayerId id) {
        return playersTicketsCount.get(id);
    }

    /**
     * Returns the number of cards that the player has in their hand
     * @param id the identity of the player
     * @return the number of cards that the player has in their hand
     */
    public ReadOnlyIntegerProperty playerCardsCount(PlayerId id) {
        return playersCardsCount.get(id);
    }

    /**
     * Returns the number of cars that the player owns
     * @param id the identity of the player
     * @return the number of cars that the player owns
     */
    public ReadOnlyIntegerProperty playerCarsCount(PlayerId id) {
        return playersCarsCount.get(id);
    }

    /**
     * Returns the number of claim points that the player has obtained
     * @param id the identity of the player
     * @return the number of claim points that the player has obtained
     */
    public ReadOnlyIntegerProperty playerClaimPoints(PlayerId id) {
        return playersClaimPoints.get(id);
    }


    //3. PrivatePlayerState Properties

    /**
     * Returns a list of the player tickets
     * @return the list of the player tickets
     */
    public ObservableList<Ticket> playerTickets() {
        return FXCollections.unmodifiableObservableList(playerTickets);
    }

    private static Map<Card, IntegerProperty> createPlayerCardTypeCount() {
        Map<Card, IntegerProperty> cardMap = new HashMap<>();

        for (Card card : Card.values())
            cardMap.put(card, new SimpleIntegerProperty(0));

        return cardMap;
    }

    private static Map<PlayerId, IntegerProperty> initProperties() {
        Map<PlayerId, IntegerProperty> intMap = new HashMap<>();

        for (PlayerId id : PlayerId.ALL) {
            intMap.put(id, new SimpleIntegerProperty(0));
        }
        return intMap;

    }

    /**
     * Returns the number of cards of each type that the player owns
     * @param card the card whose number is needed
     * @return the number of cards of each type that the player owns
     */
    public ReadOnlyIntegerProperty playerCardTypeCount(Card card) {
        return playerCardTypeCount.get(card);
    }

    private static Map<Route, BooleanProperty> createPlayerCanClaimRoute() {
        Map<Route, BooleanProperty> claimRouteMap = new HashMap<>();

        for (Route route : ChMap.routes())
            claimRouteMap.put(route, new SimpleBooleanProperty(false));

        return claimRouteMap;
    }

    /**
     * Returns true if the player has the necessary prerequisites to claim the route, else false
     * @param route the route that is desired to be claimed
     * @return true if the player has the necessary prerequisites to claim the route, else false
     */
    public ReadOnlyBooleanProperty claimable(Route route) {
        return playerCanClaimRoute.get(route);
    }

    //Additional methods

    /**
     * Returns whether it is possible to draw a ticket
     * @return whether it is possible to draw a ticket
     */
    public ReadOnlyBooleanProperty canDrawTickets() {
        return new SimpleBooleanProperty(publicGameState.canDrawTickets());
    }

    /**
     * Returns whether it is possible to draw a card
     * @return whether it is possible to draw a card
     */
    public ReadOnlyBooleanProperty canDrawCards() {
        return new SimpleBooleanProperty(publicGameState.canDrawCards());
    }

    /**
     * Returns a sortedBag of cards that the player can use to claim the given route
     * @param route the route that the player wishes to claim
     * @return a sortedBag of cards that the player can use to claim the given route
     */
    public ObservableList<SortedBag<Card>> possibleClaimCards(Route route) {
        return FXCollections.unmodifiableObservableList(FXCollections.observableList(playerState.possibleClaimCards(route)));
    }
}
