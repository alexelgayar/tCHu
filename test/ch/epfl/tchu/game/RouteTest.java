package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RouteTest {


    @Test
    void routeConstructorFailsForSameStation(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Route("h", new Station(1, "Lau"), new Station(1, "Lau"), 3, Route.Level.OVERGROUND, Color.BLACK);
        });
    }

    @Test
    void routeConstructorFailsForLengthOutOfBound(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Route("h", new Station(1, "Lau"), new Station(2, "Lau"), 7, Route.Level.OVERGROUND, Color.BLACK);
        });
    }

    @Test
    void possibleClaimCardsWorksOnOvergroundColored(){
        Route route = ChMap.routes().get(3);
        SortedBag<Card> expectedBag = SortedBag.of(2, Card.VIOLET);
        List expected = List.of(expectedBag);
        assertEquals(expected, route.possibleClaimCards());
    }


    @Test
    void possibleClaimCardsWorksOnOvergroundGrey(){
        Route route = ChMap.routes().get(17);
        SortedBag<Card> e1 = SortedBag.of(4, Card.BLACK);
        SortedBag<Card> e2 = SortedBag.of(4, Card.VIOLET);
        SortedBag<Card> e3 = SortedBag.of(4, Card.BLUE);
        SortedBag<Card> e4 = SortedBag.of(4, Card.GREEN);
        SortedBag<Card> e5 = SortedBag.of(4, Card.YELLOW);
        SortedBag<Card> e6 = SortedBag.of(4, Card.ORANGE);
        SortedBag<Card> e7 = SortedBag.of(4, Card.RED);
        SortedBag<Card> e8 = SortedBag.of(4, Card.WHITE);

        List expected = List.of(e1, e2, e3, e4, e5, e6, e7, e8);
        assertEquals(expected, route.possibleClaimCards());
    }

    @Test
    void possibleClaimCardsWorksOnUndergroundGrey(){
        Route route = ChMap.routes().get(0);
        SortedBag<Card> expectedBag = SortedBag.of(2, Card.VIOLET);
        List expected = List.of(expectedBag);
        assertEquals(expected, route.possibleClaimCards());
    }

    @Test
    void additionalClaimCardsCountWorks(){
        SortedBag<Card> claimCards = SortedBag.of(Card.ORANGE);
        SortedBag<Card> drawnCards = new SortedBag.Builder().add(Card.LOCOMOTIVE).add(Card.ORANGE).add(Card.GREEN).build();
        Route route = ChMap.routes().get(59);
        assertEquals(2, route.additionalClaimCardsCount(claimCards, drawnCards));
    }


}
