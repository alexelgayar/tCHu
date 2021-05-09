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

    public ObservableGameState(PlayerId playerId) {
        this.playerId = playerId;
    }

    public void setState(PublicGameState newGameState, PlayerState newPlayerState) {
        publicGameState = newGameState;
        playerState = newPlayerState;

        //1. Public Game State
        ticketsPercentage.set((publicGameState.ticketsCount() / TOTAL_TICKETS_COUNT) * 100);
        cardsPercentage.set((publicGameState.cardState().deckSize() / TOTAL_CARDS_COUNT) * 100);
        for (int slot : FACE_UP_CARD_SLOTS) {
            Card newCard = newGameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }

        for (PlayerId id : PlayerId.ALL) {
            for (Route route : routes.keySet()) {
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
            if (playerState.cards().contains(card)) {
                playerCardTypeCount.get(card).set(playerState.cards().countOf(card));
            }
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

    public ReadOnlyIntegerProperty ticketsPercentage() {
        return ticketsPercentage;
    }

    public ReadOnlyIntegerProperty cardsPercentage() {
        return cardsPercentage;
    }

    private static List<ObjectProperty<Card>> createFaceUpCards() {
        List<ObjectProperty<Card>> faceUpCards = new ArrayList<>(FACE_UP_CARDS_COUNT);

        for (int i = 0; i < FACE_UP_CARDS_COUNT; ++i) {
            faceUpCards.add(new SimpleObjectProperty<>());
        }

        return faceUpCards;
    }

    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    private static Map<Route, ObjectProperty<PlayerId>> createRoutes() {
        Map<Route, ObjectProperty<PlayerId>> routes = new HashMap<>();
        for (Route route : ChMap.routes())
            routes.put(route, new SimpleObjectProperty<>(null));

        return routes;
    }

    //TODO:!!!! Return ReadOnly Object Properties
    public ReadOnlyObjectProperty<PlayerId> routeOwner(Route route) { //TODO: This seems wrong, do I send the whole map through?/ ReadOnlyObjectProperty doesn't work
        return routes.get(route);
    }


    //2. PublicPlayerState Properties
    public ReadOnlyIntegerProperty playerTickets(PlayerId id) {
        return playersTicketsCount.get(id);
    }

    public ReadOnlyIntegerProperty playerCards(PlayerId id) {
        return playersCardsCount.get(id);
    }

    public ReadOnlyIntegerProperty playeCars(PlayerId id) {
        return playersCarsCount.get(id);
    }

    public ReadOnlyIntegerProperty playerClaimPoints(PlayerId id) {
        return playersClaimPoints.get(id);
    }


    //3. PrivatePlayerState Properties
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

    public ReadOnlyIntegerProperty playerCardTypeCount(Card card) {
        return playerCardTypeCount.get(card);
    }

    private static Map<Route, BooleanProperty> createPlayerCanClaimRoute() {
        Map<Route, BooleanProperty> claimRouteMap = new HashMap<>();

        for (Route route : ChMap.routes())
            claimRouteMap.put(route, new SimpleBooleanProperty(false));

        return claimRouteMap;
    }

    public ReadOnlyBooleanProperty claimable(Route route) {
        return playerCanClaimRoute.get(route);
    }

    //Additional methods
    public ReadOnlyBooleanProperty canDrawTickets() {
        return new SimpleBooleanProperty(publicGameState.canDrawTickets());
    }

    public ReadOnlyBooleanProperty canDrawCards() {
        return new SimpleBooleanProperty(publicGameState.canDrawCards());
    }

    public ObservableList<SortedBag<Card>> possibleClaimCards(Route route) {
        return FXCollections.unmodifiableObservableList(FXCollections.observableList(playerState.possibleClaimCards(route))); //TODO is this correct
    }
}
