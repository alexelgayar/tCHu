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
public final class GraphicalPlayerAdapter implements Player { //TODO: Does this class have to be static?

    private GraphicalPlayer graphicalPlayer;

    private final BlockingQueue<SortedBag<Ticket>> chosenTickets = new ArrayBlockingQueue<>(1);

    private final BlockingQueue<TurnKind> turnKindQueue = new ArrayBlockingQueue<>(1); //TODO: Should the capacity be hardcoded to 1
    private final BlockingQueue<Integer> chosenSlot = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<Route> chosenRoute = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<SortedBag<Card>> chosenClaimCards = new ArrayBlockingQueue<>(1);
    private final BlockingQueue<SortedBag<Card>> chosenAdditionalClaimCards = new ArrayBlockingQueue<>(1);

    /**
     * Constructor for GraphicalPlayerAdapter
     */
    public GraphicalPlayerAdapter() {
    } //TODO: Should this be empty?

    /**
     * Creates an instance of a graphical player GraphicalPlayer, which is adapted. The instance is stored as an attribute to use in other methods.
     * @param ownId       the player's own Id
     * @param playerNames Map that maps each player's Id to their name
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        runLater(() -> this.graphicalPlayer = new GraphicalPlayer(ownId, playerNames));
    }

    /**
     * Calls the method of the same name of the graphical player
     * @param info information to be sent to the players
     */
    @Override
    public void receiveInfo(String info) {
        runLater(() -> graphicalPlayer.receiveInfo(info));
    }

    /**
     * Calls the setState method of the graphical player
     * @param newState the new public game state
     * @param ownState the player's updated own state
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
          runLater(() -> graphicalPlayer.setState(newState, ownState));  ; //TODO: Causes nullPointerException
    }

    /**
     * Calls the chooseTickets method of the graphical player, so player can picks the initial tickets, by passing him a choice handler which stores the choice in a blocking queue
     * @param tickets the 5 tickets proposed to each player
     */
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

    /**
     * Blocks while waiting for the queue (also used by setInitialTicketChoice) contains a value, then returns this value
     * @return the value contained in the blocking queue (player tickets choice, otherwise blocks if no value is contained)
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        try {
            return chosenTickets.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    /**
     * Calls the method startTurn of the graphical player, by passing him the action handlers in blocking queues, then blocks while waiting for a value to be placed in the queue, which is then returned
     * @return the value that is put inside the blocking queue (otherwise block if no value is contained)
     */
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

    /**
     * Calls the methods setInitialTicketChoice and chooseInitialTickets, returns the value contained in the blocking queue (player tickets choice)
     * @param options ticket options that the player chooses from
     * @return the value contained in the blocking queue (player tickets choice)
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        setInitialTicketChoice(options);
        return chooseInitialTickets();
    }

    /**
     * Checks if queue contains a slot, if yes returns the slot else if not calls the method drawCard of the graphical player and returns the value of this
     * @return Checks if queue contains a slot, if yes returns the slot else if not calls the method drawCard of the graphical player and returns the value of this
     */
    @Override
    public int drawSlot() { //TODO: See if this can be optimized
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

    /**
     * Returns the first element of the queue containing the routes
     * @return the first element of the queue containing the routes
     */
    @Override
    public Route claimedRoute() {
        try {
            return chosenRoute.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    /**
     * Returns the first element of the queue containing the sortedBag of cards
     * @return the first element of the queue containing the sortedBag of cards
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        try {
            return chosenClaimCards.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    /**
     * Calls the method chooseAdditionalCards of the graphical player, returns the sortedBag of cards that is eventually placed in the blocking queue
     * @param options all the options of additional cards the player could use to claim the tunnel
     * @return the sortedBag of cards that is eventually placed in the blocking queue
     */
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
