package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.Random;

/**
 * Represents the state of a game of tCHu
 * Inherits from PublicGameState, does not offer public constructor but public, static construction method
 * Public, final and immutable
 */
public final class GameState extends PublicGameState{

    private final SortedBag<Ticket> tickets;
    private final Random rng;

    private GameState(PublicCardState cardState, PlayerId currentPlayerId, PlayerState currentPlayerState, PlayerId lastPlayer){
        super(tickets.size(), cardState, currentPlayerId, currentPlayerState, lastPlayer);
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


        this.tickets = tickets;
        this.rng = rng;

        return new GameState(tickets.size(), super.cardState(), super.currentPlayerState(), super.currentPlayerState(), super.lastPlayer());
    }

    /**
     * Method which redefines the method of same name of the PublicGameState class, to return the complete state of the given player identity, not only the public part
     * @param playerId id of the player to return the public player state of
     * @return
     */
    PlayerState playerState(PlayerId playerId){

        return new PlayerState();
    }

    /**
     * Method which redefine the method of the same name from PublicGameState, to return the complete state of the current player, and not only the public part
     * @return
     */
    public PlayerState currentPlayerState(){

        return new PlayerState();
    }

    /**
     * Method which returns the "count" tickets from the top of the pile
     * @param count the number of tickets to return from the top of the pile
     * @return returns "count" tickets from the top of the pile
     * @throws IllegalArgumentException if the count is not between 0 and the size of the pile (inclusive)
     */
    public SortedBag<Ticket> topTickets(int count){
        Preconditions.checkArgument(count >= 0 && count <= tickets.size());
        return SortedBag.of(topTickets(count));
    }

    /**
     * Method which returns a state identical to the receptor, but without the "count" tickets from the top of the pile
     * @param count the number of tickets to remove from the top of the pile
     * @return returns a state identical to the receptor, but without the "count" tickets from the top of the pile
     * @throws IllegalArgumentException if the count is not between 0 and the size of the pile (inclusive)
     */
    public GameState withoutTopTickets(int count){
        Preconditions.checkArgument(count >= 0 && count <= tickets.size());

        return new GameState();
    }

    /**
     * Method which returns the card at the top of the pile
     * @return returns the card at the top of the pile
     * @throws IllegalArgumentException if the pile is empty
     */
    public Card topCard(){
        Preconditions.checkArgument(!cardState().isDeckEmpty());

    }

    /**
     * Method which returns a state identical to the receptor but without the card at the top of the deck
     * @return returns a state identical to the receptor but without the card at the top of the deck
     * @throws IllegalArgumentException if the pile is empty
     */
    public GameState withoutTopCard(){
        Preconditions.checkArgument(!cardState().isDeckEmpty());
    }

    /**
     * Method which returns a state identical to the receptor but with the given cards added to the discard pile
     * @param discardedCards cards that must be added to the discards pile
     * @return returns a state identical to the receptor but with the given cards added to the discard pile
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){

    }

    /**
     * Method which returns a state identical to the receptor except that if the pile of cards is empty, the pile is recreated from the discards, mixed using a random number generator
     * @param rng the random number generator provided to mix the cards
     * @return returns a state identical to the receptor except that if the pile of cards is empty, the pile is recreated from the discards, mixed using a random number generator
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng){

    }




}
