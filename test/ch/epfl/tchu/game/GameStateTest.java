package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;
import ch.epfl.test.TestRandomizer;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameStateTest {

    private static final Random rng = TestRandomizer.newRandom();
    private static final SortedBag<Card> CARDS = Constants.ALL_CARDS;

    private static final int DECK_SIZE = 4;
    private static final Deck<Card> DECK = Deck.of(SortedBag.of(DECK_SIZE, Card.BLUE), rng);

    @Test
    void initialWorks() {
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