package ch.epfl.tchu.game;

import org.junit.jupiter.api.Test;

public class GameStateTest2 {


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
