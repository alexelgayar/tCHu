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





























}