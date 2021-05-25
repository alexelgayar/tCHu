package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, immutable class. Represents a public part of the game state of tChu
 */
public class PublicGameState {

    public static final int MIN_DRAW_VALUE = 5;
    private final int ticketsCount;
    private final PublicCardState cardState;
    private final PlayerId currentPlayerId, lastPlayer;
    private final Map<PlayerId, PublicPlayerState> playerState;

    /**
     * Public Constructor, it builds the public part of a game of tChu.
     *
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
        boolean playerStateEqualsTwo = playerState.keySet().size() == PlayerId.COUNT;
        Preconditions.checkArgument(ticketsCountPositive && playerStateEqualsTwo);

        this.ticketsCount = ticketsCount;
        this.cardState = Objects.requireNonNull(cardState);
        this.currentPlayerId = Objects.requireNonNull(currentPlayerId);
        this.playerState = Map.copyOf(Objects.requireNonNull(playerState));
        this.lastPlayer = lastPlayer;
    }

    /**
     * Returns the size of the bank of tickets
     * @return the size of the bank of tickets
     */
    public int ticketsCount(){
        return ticketsCount;
    }

    /**
     * Returns true IFF it is possible to draw a ticket (=> If the ticket bank is not empty)
     * @return true IFF it is possible to draw a ticket (=> if the ticket bank is not empty)
     */
    public boolean canDrawTickets(){
        return (ticketsCount > 0);
    }

    /**
     * Returns the public part of the state of the wagon/locomotive cards
     * @return the public part of the state of wagon/locomotive cards
     */
    public PublicCardState cardState(){
        return cardState;
    }

    /**
     * Returns true IFF it is possible to draw cards (-> if the (drawPile + discardsPile) >= 5)
     * @return true IFF it is possible to draw cards (=> if the (drawPile + discardsPile) >= 5)
     */
    public boolean canDrawCards(){
        return (cardState.deckSize() + cardState.discardsSize()) >= MIN_DRAW_VALUE;
    }

    /**
     * Returns the identity of the current player
     * @return the identity of the current player
     */
    public PlayerId currentPlayerId(){
        return currentPlayerId;
    }

    /**
     * Returns the PublicPlayerState of the player identity given
     *
     * @param playerId id of the player to return the public player state of
     * @return the PublicPlayerState of the player identity given
     */
    public PublicPlayerState playerState(PlayerId playerId){
        return playerState.get(playerId);
    }

    /**
     * Returns the PublicPlayerState of the current player
     * @return the PublicPlayerState of the current player
     */
    public PublicPlayerState currentPlayerState(){
        return playerState.get(currentPlayerId);
    }

    /**
     * Returns all the routes that one or the other player has claimed
     * @return all the routes that one or the other player has claimed
     */
    public List<Route> claimedRoutes(){
        List<Route> allRoutes = new ArrayList<>();
        allRoutes.addAll(playerState.get(currentPlayerId).routes());
        allRoutes.addAll(playerState.get(currentPlayerId.next()).routes());

        return allRoutes;
    }

    /**
     * Returns the identity of the last player, or null if they are not yet known (such as if the last round has not started)
     * @return the identity of the last player, or null if they are not yet known (such as if the last round has not started)
     */
    public PlayerId lastPlayer(){
        return lastPlayer;
    }


}
