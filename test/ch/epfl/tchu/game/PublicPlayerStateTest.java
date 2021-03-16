package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PublicPlayerStateTest {
    private static final List<Card> ALL_CARDS = List.of(Card.values());
    private static final List<Route> ALL_ROUTES = ChMap.routes();
    private static final List<Ticket> ALL_TICKETS = ChMap.tickets();
    private static final int carCount = Constants.INITIAL_CAR_COUNT;

    //Creates a list for testing purposes, of the given size
    private <E> List<E> generateList(List<E> list, int nbrOfRoutes){
        List<E> generatedList = new ArrayList<>();
        for (int i = 0; i < nbrOfRoutes; ++i){
            generatedList.add(list.get(i));
        }

        return generatedList;
    }

    @Test
    void PublicPlayerStateFailsOnNegativeTicketsAndCards() {
        int ticketMin = 0;
        int cardMin = 0;
        int route = 0;

        //Negative Tickets
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicPlayerState(-3, 3, generateList(ALL_ROUTES, 5));
        });

        //Negative Cards
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicPlayerState(1, -4, generateList(ALL_ROUTES, 3));
        });

        //Negative Tickets And Cards
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicPlayerState(-3, -4, generateList(ALL_ROUTES, 9));
        });

        //Negative Tickets and 0 Cards and 0 Routes
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicPlayerState(-3, 0, generateList(ALL_ROUTES, 0));
        });

        //0 Tickets and negative Cards and 0 Routes
        assertThrows(IllegalArgumentException.class, () -> {
            new PublicPlayerState(0, -3, generateList(ALL_ROUTES, 2));
        });
    }

    @Test
    void PublicPlayerStateWorksOnNormalTicketsAndCards(){
        /*
        //assertEquals: Expected, Actual
        PublicPlayerState publicPlayerState = new PublicPlayerState();
        assertEquals();

        PublicPlayerState playerState = new PlayerState(
                SortedBag.of(generateList(ALL_TICKETS, 4),
                generateList(ALL_CARDS, 3),
                generateList(ALL_ROUTES, 3));
         */
    }

    @Test
    void ticketCountWorks(){

    }

    @Test
    void cardCountWorks(){

    }

    @Test
    void routesWorks(){

    }

    @Test
    void carCountWorks(){

    }

    @Test
    void claimPointsWorks(){

    }

}

