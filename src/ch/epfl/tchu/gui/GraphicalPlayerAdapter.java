package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Instantiable class, adapts the instance of GraphicalPlayer to a Player type. All methods run by non-JavaFX thread.
 */
public class GraphicalPlayerAdapter implements Player {

    private static GraphicalPlayer graphicalPlayer;
    private static ActionHandlers.ChooseTicketsHandler chooseTicketsHandler;
    private static ActionHandlers.ClaimRouteHandler claimRouteHandler;
    private static ActionHandlers.DrawTicketsHandler drawTicketHandler;
    private static ActionHandlers.DrawCardHandler drawCardHandler;

    //Constructor
    public GraphicalPlayerAdapter() {

    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        graphicalPlayer = new GraphicalPlayer(ownId, playerNames);
    }

    @Override
    public void receiveInfo(String info) {
        runLater(() -> graphicalPlayer.receiveInfo(info));
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        graphicalPlayer.setState(newState, ownState);
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(() -> graphicalPlayer.chooseTickets(tickets, chooseTicketsHandler));
        //Using a blocking thread
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        //Using a blocking thread
        BlockingQueue<Integer> q = new ArrayBlockingQueue<>(1);

        //TODO: Can I write this out with a single try-catch?
        new Thread(() -> {
            try {
                q.put(1000);
            } catch (InterruptedException e) {
                throw new Error();
            }
        }).start();

        try {
            System.out.println(q.take());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public TurnKind nextTurn() {
        //Blocking thread
        //runLater(() -> graphicalPlayer.startTurn());
        return null;
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
//        runLater(() -> graphicalPlayer.chooseTickets(options, ???));
//        //The handler should place the playerChoice in a blocking Queue
//        //Return the element that the handler would have placed
//        //return ???;
//
//        BlockingQueue<SortedBag<Ticket>> chosenTickets = new ArrayBlockingQueue<>(1);
//
//        runLater(() -> graphicalPlayer.chooseTickets(options,
//                try {
//            chosenTickets.put();
//        }));
//        return ;
//
//
//        //TODO: Can I write this out with a single try-catch?
//        new Thread(() -> {
//            try {
//                q.put(1000);
//            } catch (InterruptedException e) {
//                throw new Error();
//            }
//        }).start();
//
//        try {
//            System.out.println(q.take());
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }



        return null;
    }

    @Override
    public int drawSlot() {
        //Tests without blocking
        return 0;
    }

    @Override
    public Route claimedRoute() {
        //Extract and returns the first element of th thread containing the routes
        return null;
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        //Similar to claimedRoute, but uses thread containg the SortedBag of cards
        return null;
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        //calls runlater then blocks waiting for an element tu be placed in the thread containing the sortedBag of cards, which is then returned
        return null;
    }
}
