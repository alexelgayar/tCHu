package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.Random;

/**
 * Represents the state of a game of tCHu
 * Inherits from PublicGameState, does not offer public constructor but public, static construction method
 * Public, final and immutable
 */
public final class GameState extends PublicGameState{

    private final SortedBag<Ticket> tickets;

    private GameState(){
        super(tickets.size());
    }

    //TODO: Plan out the initial constructor programming
    /**
     * Method which returns the initial state of a game of tCHu, in which:
     * the pile of tickets contains the given tickets and
     * the pile of cards contains the cards of Constants.ALL_CARDS, without the top 8 (2 x 4) which are distributed to the players
     * These piles are mixed via a given random generator, which is also used to decide the identity of the first player
     * @param tickets the pile of tickets for the initial gamestate of tCHu
     * @param rng the random number generator
     * @return Returns the initial state of the game
     */
    public static GameState initial(SortedBag<Ticket> tickets, Random rng){
//        * @param ticketsCount the bank of tickets has a size of ticketsCount
//        * @param cardState the public state of the wagon/locomotive cards
//        * @param currentPlayerId the current player
//        * @param playerState the public state of the players
//        * @param lastPlayer the identity of the last player (which may be null if that identity is still unknown)

        CardState cardState = new CardState();

        return new GameState();
    }

    /**
     * Method which redefines the method of same name of the PublicGameState class, to return the complete state of the given player identity, not only the public part
     * @param playerId id of the player to return the public player state of
     * @return
     */
    PlayerState playerState(PlayerId playerId){
        super(this);
        return
    }



}
