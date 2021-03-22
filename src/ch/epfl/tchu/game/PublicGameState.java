package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 *
 * Represents a public part of the game state of tChu
 * Public, immutable
 *
 */
public class PublicGameState {

    private final int ticketsCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId, lastPlayer;
    private final Map<PlayerId, PublicPlayerState> playerState;
    /**
     * Public Constructor, it builds the public part of a game of tChu.
     * @param ticketsCount the bank of tickets has a size of ticketsCount
     * @param cardState the public state of the wagon/locomotive cards
     * @param currentPlayerId the current player
     * @param playerState the public state of the players
     * @param lastPlayer the identity of the last player (which may be null if that identity is still unknown)
     * @throws IllegalArgumentException thrown if the size of the deck is strictly negative or if playerState does not contain exactly two key/value pairs
     * @throws NullPointerException if any of the other arguments (except lastPlayer) are null
     */
    public PublicGameState(int ticketsCount, PublicCardState cardState, PlayerId currentPlayerId, Map<PlayerId, PublicPlayerState> playerState, PlayerId lastPlayer){
        boolean ticketsCountPositive = ticketsCount >= 0;
        boolean playerStateEqualsTwo = playerState.entrySet().size() == 2; //TODO: Is this correct set to verify for key/value pairs?
        Preconditions.checkArgument(ticketsCountPositive && playerStateEqualsTwo);

        this.ticketsCount = ticketsCount; //primitive int type is never null
        this.cardState = Objects.requireNonNull(cardState);
        this.currentPlayerId = Objects.requireNonNull(currentPlayerId);
        this.playerState = Objects.requireNonNull(playerState);
        this.lastPlayer = lastPlayer;
    }

    /**
     * Method which returns the size of the bank of tickets
     * @return returns the size of the bank of tickets
     */
    public int ticketsCount(){
        return ticketsCount;
    }

    /**
     * Method which returns true IFF it is possible to draw a ticket (=> If the ticket bank is not empty)
     * @return returns true IFF it is possible to draw a ticket (=> if the ticket bank is not empty)
     */
    public boolean canDrawTickets(){
        return (ticketsCount > 0);
    }

    /**
     * Method which returns the public part of the state of the wagon/locomotive cards
     * @return returns the public part of the state of wagon/locomotive cards
     */
    public PublicCardState cardState(){
        return cardState;
    }

    /**
     * Method which returns true IFF it is possible to draw cards (=> if the (drawPile + discardsPile) >= 5)
     * @return
     */
    public boolean canDrawCards(){
        return (cardState.deckSize() + cardState.discardsSize()) >= 5;
    }

    /**
     * Method which returns the identity of the current player
     * @return returns the identity of the current player
     */
    public PlayerId currentPlayerId(){
        return currentPlayerId;
    }

    /**
     * Method which returns the PublicPlayerState of the player identity given
     * @param playerId id of the player to return the public player state of
     * @return returns the PublicPlayerState of the player identity given
     */
    public PublicPlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    /**
     * Method which returns the PublicPlayerState of the current player
     * @return returns the PublicPlayerState of the current player
     */
    public PublicPlayerState currentPlayerState(){
        return playerState.get(currentPlayerId);
    }

    /**
     * Method which returns all the routes that one or the other player has claimed
     * @return returns all the routes that one or the other player has claimed
     */
    public List<Route> claimedRoutes(){
        //TODO: Is this the correct meaning + return for this method?
        return playerState.get(currentPlayerId).routes();
    }

    /**
     * Method which returns the identity of the last player, or null if they are not yet known (such as if the last round has not started)
     * @return returns the identity of the last player, or null if they are not yet known (such as if the last round has not started)
     */
    public PlayerId lastPlayer(){
        return lastPlayer;
    }


}
