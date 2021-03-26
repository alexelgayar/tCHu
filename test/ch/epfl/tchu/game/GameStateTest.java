package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;
import ch.epfl.test.TestRandomizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameStateTest {

    private static final Random rng = TestRandomizer.newRandom();
    private static final SortedBag<Card> CARDS = Constants.ALL_CARDS;
    public static final Random NON_RANDOM = new Random() {
        @Override
        public int nextInt(int i) {
            return i-1;
        }
    };

    private static final int DECK_SIZE = 4;
    private static final Deck<Card> DECK = Deck.of(SortedBag.of(DECK_SIZE, Card.BLUE), rng);

    private static List<Card> shuffledCards(Random rng) {
        SortedBag<Card> cards = sixOfEachCard();
        var shuffledCards = new ArrayList<>(cards.toList());
        Collections.shuffle(shuffledCards, rng);
        return Collections.unmodifiableList(shuffledCards);
    }

    private static SortedBag<Card> sixOfEachCard() {
        return new SortedBag.Builder<Card>()
                .add(6, Card.BLACK)
                .add(6, Card.VIOLET)
                .add(6, Card.BLUE)
                .add(6, Card.GREEN)
                .add(6, Card.YELLOW)
                .add(6, Card.ORANGE)
                .add(6, Card.RED)
                .add(6, Card.WHITE)
                .add(6, Card.LOCOMOTIVE)
                .build();
    }

    @Test
    void initialWorks() {
        var rng = TestRandomizer.newRandom();
        var routes = new ArrayList<>(ChMap.routes());
        var tickets = new ArrayList<>(ChMap.tickets());
        var cards = new ArrayList<>(shuffledCards(rng));
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            Collections.shuffle(routes, rng);
            Collections.shuffle(tickets, rng);
            Collections.shuffle(cards, rng);

            var routesCount = rng.nextInt(7);
            var ticketsCount = rng.nextInt(tickets.size());
            var cardsCount = rng.nextInt(cards.size());

            var playerRoutes = Collections.unmodifiableList(routes.subList(0, routesCount));
            var playerTickets = SortedBag.of(tickets.subList(0, ticketsCount));
            var playerCards = SortedBag.of(cards.subList(0, cardsCount));

            var gs = GameState.initial(SortedBag.of(playerTickets), rng);

            assertEquals(playerTickets, gs.topTickets(gs.ticketsCount()));
            assertEquals(ticketsCount, gs.ticketsCount());

            for (PlayerId playerId : PlayerId.ALL){
                assertEquals(4, gs.playerState(playerId).cards().size());
            }
            assertEquals(Constants.ALL_CARDS.size() - 8, gs.cardState().totalSize());
        }
    }

    @Test
    void initialWorks1(){
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,NON_RANDOM);
        assertEquals(Constants.ALL_CARDS.size()-13,gameState.cardState().deckSize());
        assertEquals(PlayerId.ALL.get(1),gameState.currentPlayerId());
    }

    @Test
    void initialWorksOnEmptyTickets() {
        var rng = TestRandomizer.newRandom();
        var routes = new ArrayList<>(ChMap.routes());
        var tickets = new ArrayList<>();
        var cards = new ArrayList<>(shuffledCards(rng));
        for (int i = 0; i < TestRandomizer.RANDOM_ITERATIONS; i++) {
            Collections.shuffle(routes, rng);
            Collections.shuffle(tickets, rng);
            Collections.shuffle(cards, rng);

            var routesCount = rng.nextInt(7);
            var ticketsCount = tickets.size();
            var cardsCount = rng.nextInt(cards.size());

            var playerRoutes = Collections.unmodifiableList(routes.subList(0, routesCount));
            SortedBag<Ticket> playerTickets = SortedBag.of();
            var playerCards = SortedBag.of(cards.subList(0, cardsCount));

            var gs = GameState.initial(playerTickets, rng);

            assertEquals(playerTickets, gs.topTickets(gs.ticketsCount()));
            assertEquals(ticketsCount, gs.ticketsCount());

            for (PlayerId playerId : PlayerId.ALL){
                assertEquals(4, gs.playerState(playerId).cards().size());
            }
            assertEquals(Constants.ALL_CARDS.size() - 8, gs.cardState().totalSize());
        }
    }

    //TODO
    @Test
    void playerStateWorks() {

    }

    //TODO
    @Test
    void playerStateWorksWithNullPlayerId() {
    }

    //TODO
    @Test
    void currentPlayerStateWorks() {
    }

    //TODO
    @Test
    void currentPlayerStateWorksWithNullCurrentPlayerId() {
    }


    //TODO
    @Test
    void topTicketsWorks() {
    }

    //TODO
    @Test
    void topTicketsWorksWith0() {
    }

    //TODO
    @Test
    void topTicketsWorksWithMax() {
    }

    //TODO
    @Test
    void topTicketsFailsWithWrongCount() {
    }

    @Test
    void topTicketsWorks1() {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
        GameState gameState = GameState.initial(tickets,NON_RANDOM);
        SortedBag.Builder<Ticket> SB = new SortedBag.Builder<>();
        SB.add(tickets.get(tickets.size() - 1));
        SB.add(tickets.get(tickets.size() - 2));
        SB.add(tickets.get(tickets.size() - 3));
        SortedBag <Ticket> chosenTickets = SB.build();

        assertEquals(chosenTickets,gameState.topTickets(3));
    }

    //TODO
    @Test
    void withoutTopTicketsWorks() {
    }

    //TODO
    @Test
    void withoutTopTicketsWorksWith0() {
    }

    //TODO
    @Test
    void withoutTopTicketsWorksWithMax() {
    }

    //TODO
    @Test
    void withoutTopTicketsFailsWithWrongCount() {
    }


    //TODO
    @Test
    void topCardWorks() {
    }

    //TODO
    @Test
    void topCardFailsWithEmptyPile() {
    }


    //TODO
    @Test
    void withoutTopCardWorks() {
    }

    //TODO
    @Test
    void withoutTopCardFailsWithEmptyPile() {
    }


    //TODO
    @Test
    void withMoreDiscardedCardsWorks() {
    }

    //TODO
    @Test
    void withMoreDiscardedCardsWorksWithEmptyInput() {
    }


    //TODO
    @Test
    void withCardsDeckRecreatedIfNeededWorksNewDeck() {
    }

    //TODO
    @Test
    void withCardsDeckRecreatedIfNeededWorksSameDeck() {
    }

}