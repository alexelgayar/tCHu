package ch.epfl.tchu.game;
import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;
import ch.epfl.test.TestRandomizer;

import ch.epfl.tchu.game.CardState;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class CardStateTest {

    @Test
    void ofFailsWithDeckSmallerThanFive(){
        SortedBag<Card> sortedBag = SortedBag.of(1, Card.BLUE, 2, Card.ORANGE);
        Deck<Card> f1= new Deck<>(SortedBag.of(3, Card.BLUE).toList());
        assertThrows(IllegalArgumentException.class, () -> {
            new CardState.of(f1);
        });
    }


}
