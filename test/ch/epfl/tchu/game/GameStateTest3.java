package ch.epfl.tchu.game;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.SortedBag.Builder;

class GameStateTest3 {

    public static final Random NON_RANDOM = new Random() {
        @Override
        public int nextInt(int i) {
            return i-1;
        }
    };


    @Test
    void initialWorks(){
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,NON_RANDOM);
        assertEquals(Constants.ALL_CARDS.size()-13,gameState.cardState().deckSize());
        assertEquals(PlayerId.ALL.get(1),gameState.currentPlayerId());

    }

    @Test
    void countTicketsWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,NON_RANDOM);
        SortedBag.Builder<Ticket> SB = new SortedBag.Builder<>();
        SB.add(tickets.get(tickets.size() - 1));
        SB.add(tickets.get(tickets.size() - 2));
        SB.add(tickets.get(tickets.size() - 3));
        SortedBag <Ticket> chosenTickets = SB.build();

        assertEquals(chosenTickets,gameState.topTickets(3));
    }



    @Test
    void withInitiallyChosenTicketsWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,NON_RANDOM);
        GameState newGameState = gameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, SortedBag.of(1,ChMap.tickets().get(0),1,ChMap.tickets().get(1)));
        assertEquals(SortedBag.of(1,ChMap.tickets().get(0),1,ChMap.tickets().get(1)),newGameState.playerState(PlayerId.PLAYER_1).tickets());

    }

    @Test
    void withInitiallyChosenTicketsWorksThrowsIllegalArgumentException() {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,NON_RANDOM);
        GameState newGameState = gameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, SortedBag.of(1,ChMap.tickets().get(0),1,ChMap.tickets().get(1)));
        assertThrows(IllegalArgumentException.class, () -> {
            newGameState.withInitiallyChosenTickets(PlayerId.PLAYER_1, SortedBag.of(ChMap.tickets().get(13)));
        });

    }

    @Test
    void withChosenAdditionalTicketsWorks() {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,NON_RANDOM);
        SortedBag<Ticket>drawnTickets = SortedBag.of(1,ChMap.tickets().get(0),1,ChMap.tickets().get(1));
        SortedBag<Ticket>chosenTickets = SortedBag.of(1,ChMap.tickets().get(0));
        GameState newGameState = gameState.withChosenAdditionalTickets(drawnTickets,chosenTickets);
        assertEquals (newGameState.playerState(newGameState.currentPlayerId()).tickets(),gameState.playerState(gameState.currentPlayerId()).withAddedTickets(chosenTickets).tickets());
    }

    @Test
    void withChosenAdditionalTicketsThrowsException() {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,NON_RANDOM);
        SortedBag<Ticket>drawnTickets = SortedBag.of(1,ChMap.tickets().get(0),1,ChMap.tickets().get(1));
        SortedBag<Ticket>chosenTickets = SortedBag.of(1,ChMap.tickets().get(3));
        assertThrows(IllegalArgumentException.class, () -> {
            gameState.withChosenAdditionalTickets(drawnTickets, chosenTickets);
        });
    }

    @Test
    void withBlindlyDrawnCardWorks() {
        Random rng = new Random();
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,rng);
        GameState newGameState = gameState.withBlindlyDrawnCard();
        assertEquals(true,newGameState.playerState(newGameState.currentPlayerId()).cards().contains(gameState.topCard()));
    }

    @Test
    void withClaimedRouteWorks() {
        Random rng = new Random();
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,rng);
        GameState newGameState = gameState.withClaimedRoute(ChMap.routes().get(0), SortedBag.of(4,Card.ORANGE));
        assertEquals(true,newGameState.playerState(newGameState.currentPlayerId()).routes().contains(ChMap.routes().get(0)));
    }
















}