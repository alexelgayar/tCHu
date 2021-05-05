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
    /*
    ACTION HANDLERS
    Player plays their turn:
    - Draw tickets (Handler)
    - Draw cards (Handler)
    - (Try to) claim route (Handler)

    - Tickets that player will keep (Handler)
    - Initial/Additional cards that player will use (Handler)
     */

    @FunctionalInterface
    interface DrawTicketsHandler{
        abstract void onDrawTickets();
    }

    @FunctionalInterface
    interface DrawCardHandler{
        void onDrawCard(int slot);
    }

    @FunctionalInterface
    interface ClaimRouteHandler{
        void onClaimRoute(Route route, SortedBag<Card> claimCards);
    }

    @FunctionalInterface
    interface ChooseTicketsHandler{
        void onChooseTickets(SortedBag<Ticket> drawnTickets);
    }

    @FunctionalInterface
    interface ChooseCardsHandler{
        void onChooseCards(SortedBag<Card> drawnCards);
    }
}
