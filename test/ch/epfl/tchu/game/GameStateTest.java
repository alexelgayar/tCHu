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

            assertEquals(Constants.ALL_CARDS.size(), gs.cardState().totalSize());
            for (PlayerId playerId : PlayerId.ALL){
                assertEquals(4, gs.playerState(playerId).cards().size());
            }
            assertEquals(Constants.ALL_CARDS.size() - 8, gs.cardState().totalSize());
        }



    }

    @Test
    void initialWorksOnEmptyTickets() {
    }


    @Test
    void playerStateWorks() {
    }

    @Test
    void playerStateWorksWithNullPlayerId() {
    }


    @Test
    void currentPlayerStateWorks() {
    }

    @Test
    void currentPlayerStateWorksWithNullCurrentPlayerId() {
    }


    @Test
    void topTicketsWorks() {
    }

    @Test
    void topTicketsWorksWith0() {
    }

    @Test
    void topTicketsWorksWithMax() {
    }

    @Test
    void topTicketsFailsWithWrongCount() {
    }


    @Test
    void withoutTopTicketsWorks() {
    }

    @Test
    void withoutTopTicketsWorksWith0() {
    }

    @Test
    void withoutTopTicketsWorksWithMax() {
    }

    @Test
    void withoutTopTicketsFailsWithWrongCount() {
    }


    @Test
    void topCardWorks() {
    }

    @Test
    void topCardFailsWithEmptyPile() {
    }


    @Test
    void withoutTopCardWorks() {
    }

    @Test
    void withoutTopCardFailsWithEmptyPile() {
    }


    @Test
    void withMoreDiscardedCardsWorks() {
    }

    @Test
    void withMoreDiscardedCardsWorksWithEmptyInput() {
    }


    @Test
    void withCardsDeckRecreatedIfNeededWorksNewDeck() {
    }

    @Test
    void withCardsDeckRecreatedIfNeededWorksSameDeck() {
    }

}