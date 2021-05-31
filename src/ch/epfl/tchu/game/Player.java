package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Map;


/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public interface. Represents a player of tCHu
 */
public interface Player {

    /**
     * Called at the be beginning of the game to communicate to players their own Id's and both player's names
     *
     * @param ownId       the player's own Id
     * @param playerNames Map that maps each player's Id to their name
     */
    void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames);

    /**
     * Called to communicate information to players during the game
     *
     * @param info information to be sent to the players
     */
    void receiveInfo(String info);

    /**
     * Called whenever the game's state changes to inform the player of the public game state and their updated own state
     *
     * @param newState the new public game state
     * @param ownState the player's updated own state
     */
    void updateState(PublicGameState newState, PlayerState ownState);


    void setPlayerName();

    String choosePlayerName();

    /**
     * Called at the beginning of the game to communicate to the players the 5 tickets that they got proposed
     *
     * @param tickets the 5 tickets proposed to each player
     */
    void setInitialTicketChoice(SortedBag<Ticket> tickets);

    /**
     * Called at the beginning the game a ask the players which tickets they wish to keep
     *
     * @return the tickets that the player wishes to keep
     */
    SortedBag<Ticket> chooseInitialTickets();

    /**
     * Called at the beginning of a player's turn to know which action they would like to do during that turn
     *
     * @return the action that the player will do during their next turn
     */
    TurnKind nextTurn();

    /**
     * Called when a player chooses to draw tickets to give them choices and to know which ones they will keep
     *
     * @param options ticket options that the player chooses from
     * @return tickets that the player chose to keep
     */
    SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options);

    /**
     * Called when a players chooses to draw a card to specify from the 5 visible card or from the top of the deck
     *
     * @return the slot of the card from the 5 visible cards or -1 if the players want the card at the top of the deck
     */
    int drawSlot();

    /**
     * Called when a player claims a route to specify which route it is
     *
     * @return the route that the player claimed
     */
    Route claimedRoute();

    /**
     * Called when player wishes to claim a tunnel to specify the initial cards they wish to claim the tunnel with
     *
     * @return the initial cards the player wishes to claim the tunnel with
     */
    SortedBag<Card> initialClaimCards();

    /**
     * Called when a player wishes to claim a tunnel so the player can or not play additional cards to claim the tunnel
     *
     * @param options all the options of additional cards the player could use to claim the tunnel
     * @return the additional cars the player used to claim the tunnel or an empty set if they cannot or do not want to claim the tunnel
     */
    SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options);

    /**
     * Enum representing the different kinds of turn a player can play
     */
    enum TurnKind {

        DRAW_TICKETS,
        DRAW_CARDS,
        CLAIM_ROUTE;


        private static final TurnKind[] AllArray = TurnKind.values();

        /**
         * List containing all TurnKinds
         */
        public static final List<TurnKind> ALL = List.of(AllArray);
    }
}
