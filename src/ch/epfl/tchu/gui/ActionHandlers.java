package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Route;
import ch.epfl.tchu.game.Ticket;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Interface, has the unique goal of containing 5 nested functional (1 abstract method) interfaces, representing different action handlers
 */
public interface ActionHandlers {

    /**
     * The handler for when a player draws a ticket
     */
    @FunctionalInterface
    interface DrawTicketsHandler {
        void onDrawTickets();
    }

    /**
     * The handler for when a player draws a card
     */
    @FunctionalInterface
    interface DrawCardHandler {
        void onDrawCard(int slot);
    }

    /**
     * The handler for when a player (tries to) claims a route
     */
    @FunctionalInterface
    interface ClaimRouteHandler {
        void onClaimRoute(Route route, SortedBag<Card> claimCards);
    }

    /**
     * The handler for when a player chooses the tickets
     */
    @FunctionalInterface
    interface ChooseTicketsHandler {
        void onChooseTickets(SortedBag<Ticket> drawnTickets);
    }

    /**
     * The handler for when a player choose the initial/additional cards they will use
     */
    @FunctionalInterface
    interface ChooseCardsHandler {
        void onChooseCards(SortedBag<Card> drawnCards);
    }

    @FunctionalInterface
    interface ChooseNameHandler{
        void onChooseName(String s);
    }
}
