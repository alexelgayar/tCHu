package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RouteTest {

    @Test
    void possibleClaimCardsWorks(){


    }

    @Test
    void additionalClaimCardsCountWorks(){
        SortedBag<Card> claimCards = SortedBag.of(Card.ORANGE);
        SortedBag<Card> drawnCards = new SortedBag.Builder().add(Card.LOCOMOTIVE).add(Card.ORANGE).add(Card.GREEN).build();
        Route route = ChMap.routes().get(59);
        assertEquals(2, route.additionalClaimCardsCount(claimCards, drawnCards));
    }


}
