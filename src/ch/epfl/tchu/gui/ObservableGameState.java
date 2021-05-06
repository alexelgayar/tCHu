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
    private static PublicGameState publicGameState;
    private static PlayerState playerState; //TODO: Should playerstate be static? What do I do for the methods
    private PlayerId playerId;

    private static final int TOTAL_TICKETS_COUNT = ChMap.tickets().size();

    //PublicGameState properties
    private IntegerProperty ticketsPercentage = new SimpleIntegerProperty(0);
    private IntegerProperty cardsPercentage = new SimpleIntegerProperty(0);
    private final List<ObjectProperty<Card>> faceUpCards = createFaceUpCards();
    private static final Map<Route, ObjectProperty<PlayerId>> routes = createRoutes();

    //PublicPlayerState properties
    private IntegerProperty playerTicketsCount = new SimpleIntegerProperty(0);
    private IntegerProperty playerCardsCount = new SimpleIntegerProperty(0);
    private IntegerProperty playerCarsCount = new SimpleIntegerProperty(0);
    private IntegerProperty playerClaimPoints = new SimpleIntegerProperty(0);

    //PlayerState properties
    private final ObservableList<Ticket> playerTickets = FXCollections.observableArrayList();
    private final Map<Card, IntegerProperty> playerCardTypeCount = createPlayerCardTypeCount();
    private final Map<Route, BooleanProperty> playerCanClaimRoute = createPlayerCanClaimRoute();

    public ObservableGameState(PlayerId playerId){
        //All the state properties are set to null, 0 or false
        this.playerId = playerId;

        //Create all the properties without initializing them
    }

    public void setState(PublicGameState newGameState, PlayerState newPlayerState){
        publicGameState = newGameState;
        playerState = newPlayerState;

        //TODO: (Remove later) NOTE: We must not change the reference of collections!! Therefore we can't re-call eg faceUpCards = createFaceUpCards inside the setState!!
        //1. Public Game State
        ticketsPercentage = computeTicketsPercentage();
        cardsPercentage = computeCardsPercentage();
        for (int slot : FACE_UP_CARD_SLOTS) {
            Card newCard = newGameState.cardState().faceUpCard(slot);
            faceUpCards.get(slot).set(newCard);
        }
        for (Route route: routes.keySet()){
            if (playerState.routes().contains(route))
                routes.put(route, new SimpleObjectProperty<>(newGameState.currentPlayerId()));
        }

        //2. Public Player State
        playerTicketsCount = computePlayerTicketsCount();
        playerCardsCount = computePlayerCardsCount();
        playerCarsCount = computePlayerCarsCount();
        playerClaimPoints = computePlayerClaimPoints();

        //3. Player State
        if (playerTickets.size() == 0){
            playerTickets.addAll(playerState.tickets().toList());
        }
        else{
            playerTickets.setAll(playerState.tickets().toList());
        }

        Map<Card, IntegerProperty> cardMap = new HashMap<>(); //TODO: How to optimize the double for loops?
        for (Card card: playerCardTypeCount.keySet()){
            cardMap.put(card, new SimpleIntegerProperty(0));
        }

        for (Card card: Card.values()){
            for (Card playerCard: playerState.cards()){
                if (card == playerCard){
                    playerCardTypeCount.put(card, new SimpleIntegerProperty((cardMap.getOrDefault(card, new SimpleIntegerProperty(0))).get() + 1));
                }
            }
        }

        //TODO: Complete this condition: Player = currentPlayer? double route not claimed?
        //Double route: A route where the start + end stations are the same
        for (Route route: ChMap.routes()){
            playerCanClaimRoute.put(route, new SimpleBooleanProperty(
                    publicGameState.currentPlayerId() == playerId
                            && routes.get(route) == null //TODO: Treat double routes here
                            && playerState.canClaimRoute(route)));
        }
    }

    //1. PublicGameState Properties
    private static IntegerProperty computeTicketsPercentage(){
        return new SimpleIntegerProperty((publicGameState.ticketsCount() / TOTAL_TICKETS_COUNT) * 100);
    }
    public ReadOnlyIntegerProperty ticketsPercentage(){
        return ticketsPercentage;
    }

    private static IntegerProperty computeCardsPercentage(){
        return new SimpleIntegerProperty((publicGameState.cardState().deckSize() / TOTAL_CARDS_COUNT) * 100); //TODO: Do i remove face up cards?
    }
    public ReadOnlyIntegerProperty cardsPercentage(){
        return cardsPercentage;
    }

    private static List<ObjectProperty<Card>> createFaceUpCards(){
        List<ObjectProperty<Card>> faceUpCards = new ArrayList<>(FACE_UP_CARDS_COUNT);

        for (int i = 0; i < FACE_UP_CARDS_COUNT; ++i){
            faceUpCards.add(new SimpleObjectProperty<>());
        }

        return faceUpCards;
    }
    public ReadOnlyObjectProperty<Card> faceUpCard(int slot) {
        return faceUpCards.get(slot);
    }

    private static Map<Route, ObjectProperty<PlayerId>> createRoutes(){
        Map<Route, ObjectProperty<PlayerId>> routes = new HashMap<>();
        for (Route route: ChMap.routes())
            routes.put(route, new SimpleObjectProperty<>());

        return routes;
    }
    public ObjectProperty<PlayerId> routeOwner(Route route){ //TODO: This seems wrong, do I send the whole map through?/ ReadOnlyObjectProperty doesn't work
        return routes.get(route);
    }


    //2. PublicPlayerState Properties
    private static IntegerProperty computePlayerTicketsCount(){
        return new SimpleIntegerProperty(playerState.ticketCount());
    }
    public ReadOnlyIntegerProperty playersTickets(){
        return playerTicketsCount;
    }

    private static IntegerProperty computePlayerCardsCount(){
        return new SimpleIntegerProperty(playerState.cardCount());
    }
    public ReadOnlyIntegerProperty playersCards(){
        return playerCardsCount;
    }

    private static IntegerProperty computePlayerCarsCount(){
        return new SimpleIntegerProperty(playerState.carCount());
    }
    public ReadOnlyIntegerProperty playerCars(){
        return playerCarsCount;
    }

    private static IntegerProperty computePlayerClaimPoints(){
        return new SimpleIntegerProperty(playerState.claimPoints());
    }
    public ReadOnlyIntegerProperty playerClaimPoints(){
        return playerClaimPoints;
    }


    //3. PrivatePlayerState Properties
    public ObservableList<Ticket> playerTickets(){
        return FXCollections.unmodifiableObservableList(playerTickets);
    }

    private static Map<Card, IntegerProperty> createPlayerCardTypeCount(){
        Map<Card, IntegerProperty> cardMap = new HashMap<>();

        for (Card card: Card.values())
            cardMap.put(card, new SimpleIntegerProperty(0));

        return cardMap;
    }
    public IntegerProperty playerCardTypeCount(Card card){
        return playerCardTypeCount.get(card);
    }

    private static Map<Route, BooleanProperty> createPlayerCanClaimRoute(){
        Map<Route, BooleanProperty> claimRouteMap = new HashMap<>();

        for (Route route: ChMap.routes())
            claimRouteMap.put(route, new SimpleBooleanProperty(false));

        return claimRouteMap;
    }
    public BooleanProperty claimable(Route route){
        return playerCanClaimRoute.get(route);
    }

    //Additional methods
    public BooleanProperty canDrawTickets(){
        return new SimpleBooleanProperty(publicGameState.canDrawTickets());
    }

    public BooleanProperty canDrawCards(){
        return new SimpleBooleanProperty(publicGameState.canDrawCards());
    }

    public List<SortedBag<Card>> possibleClaimCards(Route route){
        return playerState.possibleClaimCards(route);
    }
}
