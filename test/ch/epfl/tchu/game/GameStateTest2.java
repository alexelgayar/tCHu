package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameStateTest2 {
    public static final Random NON_RANDOM = new Random() {
        @Override
        public int nextInt(int i) {
            return i-1;
        }
    };


    //====== 2. ======//
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
        SortedBag<Ticket> drawnTickets = SortedBag.of(1,ChMap.tickets().get(0),1,ChMap.tickets().get(1));
        SortedBag<Ticket> chosenTickets = SortedBag.of(1,ChMap.tickets().get(0));
        GameState newGameState = gameState.withChosenAdditionalTickets(drawnTickets,chosenTickets);
        assertEquals (newGameState.playerState(newGameState.currentPlayerId()).tickets(),gameState.playerState(gameState.currentPlayerId()).withAddedTickets(chosenTickets).tickets());
    }

    @Test
    void withChosenAdditionalTicketsWorksWithEmptyDrawnAndChosenTickets() {

        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,NON_RANDOM);
        SortedBag<Ticket> drawnTickets = SortedBag.of();
        SortedBag<Ticket> chosenTickets = SortedBag.of();
        GameState newGameState = gameState.withChosenAdditionalTickets(drawnTickets,chosenTickets);
        assertEquals (newGameState.playerState(newGameState.currentPlayerId()).tickets(),gameState.playerState(gameState.currentPlayerId()).withAddedTickets(chosenTickets).tickets());

    }

    @Test
    void withChosenAdditionalTicketsWorksWithEmptyChosenTickets() {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,NON_RANDOM);
        SortedBag<Ticket> drawnTickets = SortedBag.of(1,ChMap.tickets().get(0),1,ChMap.tickets().get(1));
        SortedBag<Ticket> chosenTickets = SortedBag.of();
        GameState newGameState = gameState.withChosenAdditionalTickets(drawnTickets,chosenTickets);
        assertEquals (newGameState.playerState(newGameState.currentPlayerId()).tickets(),gameState.playerState(gameState.currentPlayerId()).withAddedTickets(chosenTickets).tickets());
    }


    @Test
    void withChosenAdditionalTicketsThrowsException() {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,NON_RANDOM);
        SortedBag<Ticket> drawnTickets = SortedBag.of(1,ChMap.tickets().get(0),1,ChMap.tickets().get(1));
        SortedBag<Ticket> chosenTickets = SortedBag.of(1,ChMap.tickets().get(3));
        assertThrows(IllegalArgumentException.class, () -> {
            gameState.withChosenAdditionalTickets(drawnTickets, chosenTickets);
        });
    }



    @Test
    void withDrawnFaceUpCardWorks() {

        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,NON_RANDOM);


    }

    @Test
    void withDrawnFaceUpCardWorksWithWrongSlotIndex() {
    }

    @Test
    void withDrawnFaceUpCardFails() {
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


    @Test
    void withClaimedRouteWorksWithEmptyCards() {
        Random rng = new Random();
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,rng);
        GameState newGameState = gameState.withClaimedRoute(ChMap.routes().get(0), SortedBag.of());
        assertEquals(true,newGameState.playerState(newGameState.currentPlayerId()).routes().contains(ChMap.routes().get(0)));
    }


    @Test
    void lastTurnBeginsWorks() {
    }


    @Test
    void forNextTurnWorks() {
    }

}
