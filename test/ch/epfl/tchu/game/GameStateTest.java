package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

public class GameStateTest {

    @Test
    void initialWorks(){ }

    @Test
    void initialWorksOnEmptyTickets(){}


    @Test
    void playerStateWorks(){ }

    @Test
    void playerStateWorksWithNullPlayerId() { }


    @Test
    void currentPlayerStateWorks(){ }

    @Test
    void currentPlayerStateWorksWithNullCurrentPlayerId(){ }


    @Test
    void topTicketsWorks() {}

    @Test
    void topTicketsWorksWith0(){}

    @Test
    void topTicketsWorksWithMax(){}

    @Test
    void topTicketsFailsWithWrongCount() {}


    @Test
    void withoutTopTicketsWorks(){ }

    @Test
    void withoutTopTicketsWorksWith0(){}

    @Test
    void withoutTopTicketsWorksWithMax() {}

    @Test
    void withoutTopTicketsFailsWithWrongCount(){ }


    @Test
    void topCardWorks() {}

    @Test
    void topCardFailsWithEmptyPile(){}


    @Test
    void withoutTopCardWorks(){}

    @Test
    void withoutTopCardFailsWithEmptyPile(){}


    @Test
    void withMoreDiscardedCardsWorks(){}

    @Test
    void withMoreDiscardedCardsWorksWithEmptyInput(){}


    @Test
    void withCardsDeckRecreatedIfNeededWorksNewDeck(){}

    @Test
    void withCardsDeckRecreatedIfNeededWorksSameDeck(){}


    //====== 2. ======//
    @Test
    void withInitiallyChosenTicketsWorks(){}

    @Test
    void withInitiallyChosenTicketsWorksWithEmptyTickets(){}

    @Test
    void withInitiallyChosenTicketsWorksWithNullPlayerId(){}

    @Test
    void withInitiallyChosenTicketsFails(){}


    @Test
    void withChosenAdditionalTicketsWorks(){}

    @Test
    void withChosenAdditionalTicketsWorksWithEmptyDrawnAndChosenTickets(){}

    @Test
    void withChosenAdditionalTicketsWorksWithEmptyChosenTickets(){}

    @Test
    void withChosenAdditionalTicketsWorksWithNullPlayerId(){}

    @Test
    void withChosenAdditionalTicketsFails(){}


    @Test
    void withDrawnFaceUpCardWorks(){}

    @Test
    void withDrawnFaceUpCardWorksWithWrongSlotIndex(){}

    @Test
    void withDrawnFaceUpCardFails(){}


    @Test
    void withBlindlyDrawnCardsWorks(){}


    @Test
    void withBlindlyDrawnCardFails(){}


    @Test
    void withClaimedRouteWorks(){}

    @Test
    void withClaimedRouteWorksWithEmptyRoute(){}

    @Test
    void withClaimedRouteWorksWithEmptyCards(){}


    @Test
    void lastTurnBeginsWorks(){}


    @Test
    void forNextTurnWorks(){}

}
