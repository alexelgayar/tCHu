package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;
import ch.epfl.test.TestRandomizer;

import java.util.*;

import static ch.epfl.tchu.game.Constants.INITIAL_CARDS_COUNT;
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;
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



    @Test
    void topTicketsWorks() {
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

            assertEquals(SortedBag.of(), gs.topTickets(0));
            assertEquals(SortedBag.of(playerTickets), gs.topTickets(playerTickets.size()));
            assertThrows(IllegalArgumentException.class, () -> {
                    gs.topTickets(playerTickets.size() + 1);
            });
        }
    }

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


}