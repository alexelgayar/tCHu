package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.TestRandomizer;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameStateTest2 {

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


    //====== 2. ======//
    @Test
    void withInitiallyChosenTicketsWorks() {

        var rng = TestRandomizer.newRandom();
        var routes = new ArrayList<>(ChMap.routes());
        var tickets = new ArrayList<>(ChMap.tickets());
        var cards = new ArrayList<>(shuffledCards(rng));

        var routesCount = rng.nextInt(7);
        var ticketsCount = rng.nextInt(tickets.size());
        var cardsCount = rng.nextInt(cards.size());

        var playerRoutes = Collections.unmodifiableList(routes.subList(0, routesCount));
        var playerTickets = SortedBag.of(tickets.subList(0, ticketsCount));
        var playerCards = SortedBag.of(cards.subList(0, cardsCount));

        var gs = GameState.initial(SortedBag.of(playerTickets), rng);


    }

    @Test
    void withInitiallyChosenTicketsWorksWithEmptyTickets() {
    }

    @Test
    void withInitiallyChosenTicketsWorksWithNullPlayerId() {
    }

    @Test
    void withInitiallyChosenTicketsFails() {
    }


    @Test
    void withChosenAdditionalTicketsWorks() {
    }

    @Test
    void withChosenAdditionalTicketsWorksWithEmptyDrawnAndChosenTickets() {
    }

    @Test
    void withChosenAdditionalTicketsWorksWithEmptyChosenTickets() {
    }

    @Test
    void withChosenAdditionalTicketsWorksWithNullPlayerId() {
    }

    @Test
    void withChosenAdditionalTicketsFails() {
    }


    @Test
    void withDrawnFaceUpCardWorks() {
    }

    @Test
    void withDrawnFaceUpCardWorksWithWrongSlotIndex() {
    }

    @Test
    void withDrawnFaceUpCardFails() {
    }


    @Test
    void withBlindlyDrawnCardsWorks() {
    }


    @Test
    void withBlindlyDrawnCardFails() {
    }


    @Test
    void withClaimedRouteWorks() {
    }

    @Test
    void withClaimedRouteWorksWithEmptyRoute() {
    }

    @Test
    void withClaimedRouteWorksWithEmptyCards() {
    }


    @Test
    void lastTurnBeginsWorks() {
    }


    @Test
    void forNextTurnWorks() {
    }

}
