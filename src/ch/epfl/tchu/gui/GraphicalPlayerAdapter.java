package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.beans.property.SimpleObjectProperty;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Instantiable class, adapts the instance of GraphicalPlayer to a Player type. All methods run by non-JavaFX thread.
 */
public class GraphicalPlayerAdapter implements Player {

    private GraphicalPlayer graphicalPlayer;

    private final BlockingQueue<SortedBag<Ticket>> chosenTickets = new ArrayBlockingQueue<>(1);

    private final BlockingQueue<TurnKind> turnKindQueue = new ArrayBlockingQueue<>(1); //TODO: Should the capacity be hardcoded to 1
    private final BlockingQueue<Integer> chosenSlot = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Route> chosenRoute = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<SortedBag<Card>> chosenClaimCards = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<SortedBag<Card>> chosenAdditionalClaimCards = new ArrayBlockingQueue<>(1);

    //Constructor
    public GraphicalPlayerAdapter() {
    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        runLater(() -> this.graphicalPlayer = new GraphicalPlayer(ownId, playerNames));
    }

    @Override
    public void receiveInfo(String info) {
        runLater(() -> graphicalPlayer.receiveInfo(info));
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {

          runLater(() -> graphicalPlayer.setState(newState, ownState));  ; //TODO: Causes nullPointerException

    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(() -> graphicalPlayer.chooseTickets(tickets, drawnTickets -> {
            try {
                chosenTickets.put(drawnTickets);
            } catch (InterruptedException e) {
                throw new Error(); //TODO: Is there a way to modularise the try/catch for all the methods?
            }
        }));
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        try {
            return chosenTickets.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    @Override
    public TurnKind nextTurn() {
        runLater(() -> graphicalPlayer.startTurn(
                () -> {
                    try {
                        turnKindQueue.put(TurnKind.DRAW_TICKETS);
                    } catch (InterruptedException e) {
                        throw new Error();
                    }
                },
                slot -> {
                    try {
                        turnKindQueue.put(TurnKind.DRAW_CARDS);
                        chosenSlot.put(slot);
                    } catch (InterruptedException e) {
                        throw new Error();
                    }
                },
                (route, claimCards) -> {
                    try {
                        turnKindQueue.put(TurnKind.CLAIM_ROUTE);
                        chosenRoute.put(route);
                        chosenClaimCards.put(claimCards);
                    } catch (InterruptedException e) {
                        throw new Error();
                    }
                }));

        try {
            return turnKindQueue.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        setInitialTicketChoice(options);
        return chooseInitialTickets();
    }

    @Override
    public int drawSlot() {
        if (!chosenSlot.isEmpty()){
            //Return queue value directly(below)
        }
        else{
            runLater(() -> graphicalPlayer.drawCard(slot -> {
                try {
                    chosenSlot.put(slot);
                } catch (InterruptedException e) {
                    throw new Error();
                }
            }));
        }

        try {
            return chosenSlot.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    @Override
    public Route claimedRoute() {
        try {
            return chosenRoute.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        try {
            return chosenClaimCards.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        runLater(() -> graphicalPlayer.chooseAdditionalCards(options, chosenCards -> {
            try {
                chosenAdditionalClaimCards.put(chosenCards);
            } catch (InterruptedException e){ throw new Error(); }
        }));

        try {
            return chosenAdditionalClaimCards.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }
}
