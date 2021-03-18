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
    private <E> List<E> generateList(List<E> list, int nbrOfItems, int startIndex){
        List<E> generatedList = new ArrayList<>();
        for (int i = startIndex; i < nbrOfItems + startIndex; ++i){
            generatedList.add(list.get(i));
        }

        return generatedList;
    }

    @Test
    void PublicPlayerStateFailsOnNegativeTicketsAndCards() {

        assertThrows(IllegalArgumentException.class, () -> {
            //Negative Tickets
            new PublicPlayerState(-3, 3, generateList(ALL_ROUTES, 5, 4));

            //Negative Cards
            new PublicPlayerState(1, -4, generateList(ALL_ROUTES, 3, 15));

            //Negative Tickets And Cards
            new PublicPlayerState(-3, -4, generateList(ALL_ROUTES, 9, 18));

            //Negative Tickets and 0 Cards and 0 Routes
            new PublicPlayerState(-3, 0, generateList(ALL_ROUTES, 0, 0));

            //0 Tickets and negative Cards and 2 Routes
            new PublicPlayerState(0, -3, generateList(ALL_ROUTES, 2, 25));
        });
    }

    @Test
    void PublicPlayerStateWorksOnNormalTicketsAndCards1(){
        List<Route> routes = generateList(ALL_ROUTES, 3,0);
        PublicPlayerState publicPlayerState = new PublicPlayerState(3, 4, routes);

        //TicketCount
        assertEquals(3, publicPlayerState.ticketCount());

        //CardCount
        assertEquals(4, publicPlayerState.cardCount());

        //Routes
        assertEquals(routes, publicPlayerState.routes());
        //Autriche Saint-Gall
        //Autriche Vaduz
        //Baden Bâle

        //carCount
        assertEquals(Constants.INITIAL_CAR_COUNT - 8, publicPlayerState.carCount());

        //claimPoints
        assertEquals(12, publicPlayerState.claimPoints());
    }

    @Test
    void PublicPlayerStateWorksOnEmptySets(){
        List<Route> routes = generateList(ALL_ROUTES, 5,17);
        PublicPlayerState publicPlayerState = new PublicPlayerState(0, 0, routes);

        //TicketCount
        assertEquals(0, publicPlayerState.ticketCount());

        //CardCount
        assertEquals(0, publicPlayerState.cardCount());

        //Routes
        assertEquals(routes, publicPlayerState.routes());

        for (Route route: routes){
            System.out.println("Routes: " + route.stations());
        }

        //Routes: [Berne, Lucerne]
        //Routes: [Berne, Neuchâtel]
        //Routes: [Berne, Soleure]
        //Routes: [Brigue, Interlaken]
        //Routes: [Brigue, Italie]

        //carCount
        assertEquals(Constants.INITIAL_CAR_COUNT - 13, publicPlayerState.carCount());

        //claimPoints
        assertEquals(17, publicPlayerState.claimPoints());
    }

    @Test
    void PublicPlayerStateWorksOnEmptyRoutes(){
        List<Route> routes = generateList(ALL_ROUTES, 0,0); //Empty Routes
        PublicPlayerState publicPlayerState = new PublicPlayerState(0, 0, routes);

        //TicketCount
        assertEquals(0, publicPlayerState.ticketCount());

        //CardCount
        assertEquals(0, publicPlayerState.cardCount());

        //Routes
        assertEquals(routes, publicPlayerState.routes());

        for (Route route: routes){
            System.out.println("Routes: " + route.stations());
        }
        //Routes: No routes

        //carCount
        assertEquals(Constants.INITIAL_CAR_COUNT, publicPlayerState.carCount());

        //claimPoints
        assertEquals(0, publicPlayerState.claimPoints());
    }

    @Test
    void PublicPlayerStateWorksWithPlayerStateConstructor(){
        SortedBag<Ticket> tickets = SortedBag.of(generateList(ALL_TICKETS, 4, 5));
        SortedBag<Card> cards = SortedBag.of(generateList(ALL_CARDS, 6, 3));
        List<Route> routes = generateList(ALL_ROUTES, 3,5); //Empty Routes
        PublicPlayerState publicPlayerState = new PlayerState(tickets, cards, routes);

        //TicketCount
        assertEquals(4, publicPlayerState.ticketCount());
        for (Ticket ticket: tickets){
            System.out.println("Tickets: " + ticket.text());
        }

        //CardCount
        assertEquals(6, publicPlayerState.cardCount());
        for (Card card: cards){
            System.out.println("Cards: " + card.name());
        }

        //Routes
        assertEquals(routes, publicPlayerState.routes());

        for (Route route: routes){
            System.out.println("Routes: " + route.stations());
        }
        //Routes: No routes

        //carCount
        assertEquals(Constants.INITIAL_CAR_COUNT - 5, publicPlayerState.carCount());

        //claimPoints
        assertEquals(5, publicPlayerState.claimPoints());
    }

    @Test
    void PublicPlayerStateWorksWithEmptyPlayerStateConstructor(){
        SortedBag<Ticket> tickets = SortedBag.of(generateList(ALL_TICKETS, 0, 0));
        SortedBag<Card> cards = SortedBag.of(generateList(ALL_CARDS, 0, 0));
        List<Route> routes = generateList(ALL_ROUTES, 0,0); //Empty Routes
        PublicPlayerState publicPlayerState = new PlayerState(tickets,cards, routes);

        //TicketCount
        assertEquals(0, publicPlayerState.ticketCount());
        for (Ticket ticket: tickets){
            System.out.println("Tickets: " + ticket.text());
        }

        //CardCount
        assertEquals(0, publicPlayerState.cardCount());
        for (Card card: cards){
            System.out.println("Cards: " + card.name());
        }

        //Routes
        assertEquals(routes, publicPlayerState.routes());

        for (Route route: routes){
            System.out.println("Routes: " + route.stations());
        }
        //Routes: No routes

        //carCount
        assertEquals(Constants.INITIAL_CAR_COUNT, publicPlayerState.carCount());

        //claimPoints
        assertEquals(0, publicPlayerState.claimPoints());
    }

    @Test
    void ticketCountWorks(){
        SortedBag<Ticket> tickets1 = SortedBag.of(generateList(ALL_TICKETS, 8, 5));
        SortedBag<Card> cards = SortedBag.of(generateList(ALL_CARDS, 6, 3));
        List<Route> routes = generateList(ALL_ROUTES, 3,5); //Empty Routes
        PublicPlayerState publicPlayerState1 = new PlayerState(tickets1, cards, routes);

        assertEquals(8, publicPlayerState1.ticketCount());

        SortedBag<Ticket> tickets2 = SortedBag.of(generateList(ALL_TICKETS, 0, 0));
        PublicPlayerState publicPlayerState2 = new PlayerState(tickets2, cards, routes);

        assertEquals(0, publicPlayerState2.ticketCount());
    }

    @Test
    void cardCountWorks(){
        SortedBag<Ticket> tickets = SortedBag.of(generateList(ALL_TICKETS, 8, 5));
        SortedBag<Card> cards1 = SortedBag.of(generateList(ALL_CARDS, 2, 3));
        List<Route> routes = generateList(ALL_ROUTES, 3,5); //Empty Routes
        PublicPlayerState publicPlayerState1 = new PlayerState(tickets, cards1, routes);

        assertEquals(2, publicPlayerState1.cardCount());

        SortedBag<Card> cards2 = SortedBag.of(generateList(ALL_CARDS, 6, 0));
        PublicPlayerState publicPlayerState2 = new PlayerState(tickets, cards2, routes);

        assertEquals(6, publicPlayerState2.cardCount());

        SortedBag<Card> cards3 = SortedBag.of(generateList(ALL_CARDS, 0, 0));
        PublicPlayerState publicPlayerState3 = new PlayerState(tickets, cards3, routes);

        assertEquals(0, publicPlayerState3.cardCount());
    }

    @Test
    void routesWorks(){
        //Already tested above
    }

    @Test
    void carCountWorks(){
        //Already tested above
    }

    @Test
    void claimPointsWorks(){
        //Already tested above
    }

}

