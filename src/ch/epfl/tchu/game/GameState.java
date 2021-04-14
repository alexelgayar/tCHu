package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.Constants.INITIAL_CARDS_COUNT;
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Represents the state of a game of tCHu
 * Public, final and immutable. Inherits from PublicGameState, does not offer public constructor but public, static construction method
 */
public final class GameState extends PublicGameState {

    private final Deck<Ticket> tickets;
    private final CardState cardstate;
    private final Map<PlayerId, PlayerState> completePlayerState;

    private GameState(Deck<Ticket> tickets, CardState cardState, PlayerId currentPlayerId, Map<PlayerId, PlayerState> playerState, PlayerId lastPlayerId) {
        super(tickets.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayerId);

        this.tickets = tickets;
        this.cardstate = cardState;
        this.completePlayerState = Map.copyOf(playerState);
    }

    /**
     * Returns the initial state of a game of tCHu, in which:
     * - the pile of tickets contains the given tickets and
     * - the pile of cards contains the cards of Constants.ALL_CARDS, without the top 8 (2 x 4) which are distributed to the players
     * These piles are mixed via a given random generator, which is also used to decide the identity of the first player
     *
     * @param tickets the pile of tickets for the initial gameState of tCHu
     * @param rng     the random number generator
     * @return the initial state of the game
     */
    public static GameState initial(SortedBag<Ticket> tickets, Random rng) {
        Deck<Ticket> ticketDeck = Deck.of(SortedBag.of(tickets), rng);
        Deck<Card> cardDeck = Deck.of(SortedBag.of(Constants.ALL_CARDS), rng);

        Map<PlayerId, PlayerState> playerStateMap = new EnumMap<>(PlayerId.class);

        PlayerId firstPlayer = rng.nextInt(PlayerId.COUNT) == 0 ? PLAYER_1 : PLAYER_2;

        for (PlayerId player : PlayerId.ALL) {
            PlayerState playerState = PlayerState.initial(cardDeck.topCards(INITIAL_CARDS_COUNT));
            cardDeck = cardDeck.withoutTopCards(INITIAL_CARDS_COUNT);
            playerStateMap.put(player, playerState);
        }

        return new GameState(ticketDeck, CardState.of(cardDeck), firstPlayer, playerStateMap, null);
    }

    /**
     * Redefines the method of same name of the PublicGameState class, to return the complete state of the given player identity, not only the public part
     *
     * @param playerId id of the player to return the public player state of
     * @return the complete state of the given player identity, not only the public part
     */
    public PlayerState playerState(PlayerId playerId) {
        return completePlayerState.get(playerId);
    }

    /**
     * Redefines the method of the same name from PublicGameState, to return the complete state of the current player, and not only the public part
     *
     * @return the complete state of the current player, and not only the public part
     */
    public PlayerState currentPlayerState() {
        return playerState(currentPlayerId());
    }

    /**
     * Returns the "count" tickets from the top of the pile
     *
     * @param count the number of tickets to return from the top of the pile
     * @return "count" tickets from the top of the pile
     * @throws IllegalArgumentException if the count is not between 0 and the size of the pile (inclusive)
     */
    public SortedBag<Ticket> topTickets(int count) {
        Preconditions.checkArgument(count >= 0 && count <= tickets.size());

        return SortedBag.of(tickets.topCards(count));
    }

    /**
     * Returns a state identical to the receptor, but without the "count" tickets from the top of the pile
     *
     * @param count the number of tickets to remove from the top of the pile
     * @return a state identical to the receptor, but without the "count" tickets from the top of the pile
     * @throws IllegalArgumentException if the count is not between 0 and the size of the pile (inclusive)
     */
    public GameState withoutTopTickets(int count) {
        Preconditions.checkArgument(count >= 0 && count <= tickets.size());

        return new GameState(tickets.withoutTopCards(count), cardstate, currentPlayerId(), completePlayerState, lastPlayer());
    }

    /**
     * Returns the card at the top of the pile
     *
     * @return the card at the top of the pile
     * @throws IllegalArgumentException if the pile is empty
     */
    public Card topCard() {
        Preconditions.checkArgument(!cardState().isDeckEmpty());

        return cardstate.topDeckCard();
    }

    /**
     * Returns a state identical to the receptor but without the card at the top of the deck
     *
     * @return a state identical to the receptor but without the card at the top of the deck
     * @throws IllegalArgumentException if the pile is empty
     */
    public GameState withoutTopCard() {
        Preconditions.checkArgument(!cardState().isDeckEmpty());

        return new GameState(tickets, cardstate.withoutTopDeckCard(), currentPlayerId(), completePlayerState, lastPlayer());
    }

    /**
     * Returns a state identical to the receptor but with the given cards added to the discard pile
     *
     * @param discardedCards cards that must be added to the discards pile
     * @return a state identical to the receptor but with the given cards added to the discard pile
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards) {
        return new GameState(tickets, cardstate.withMoreDiscardedCards(discardedCards), currentPlayerId(), completePlayerState, lastPlayer());
    }

    /**
     * Returns a state identical to the receptor except that if the pile of cards is empty, the pile is recreated from the discards, mixed using a random number generator
     *
     * @param rng the random number generator provided to mix the cards
     * @return a state identical to the receptor except that if the pile of cards is empty, the pile is recreated from the discards, mixed using a random number generator
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng) {
        return (cardstate.isDeckEmpty())
                ? new GameState(tickets, cardstate.withDeckRecreatedFromDiscards(rng), currentPlayerId(), completePlayerState, lastPlayer())
                : new GameState(tickets, cardstate, currentPlayerId(), completePlayerState, lastPlayer());
    }


    //====== 2. ======//
    //Generates a new playerStateMap with the given playerState, for the current player
    private Map<PlayerId, PlayerState> newPlayerMap(PlayerState newPlayerState) {
        Map<PlayerId, PlayerState> newPlayerStateMap = new EnumMap<>(completePlayerState);
        newPlayerStateMap.put(currentPlayerId(), newPlayerState);
        return newPlayerStateMap;
    }

    /**
     * Returns an identical state to the receiver but in which the given tickets have been added to the given player's hand
     *
     * @param playerId      the player to whom the tickets should be added to
     * @param chosenTickets the tickets that must be added to the given player
     * @return an identical state to the receiver but in which the given tickets have been added to the given player's hand
     * @throws IllegalArgumentException if the currentPlayer already owns at least one ticket
     */
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets) {
        Preconditions.checkArgument(completePlayerState.get(playerId).tickets().isEmpty());

        Map<PlayerId, PlayerState> newPlayerStateMap = new EnumMap<>(completePlayerState);
        newPlayerStateMap.put(playerId, completePlayerState.get(playerId).withAddedTickets(chosenTickets));

        return new GameState(tickets, cardstate, currentPlayerId(), newPlayerStateMap, lastPlayer());
    }

    /**
     * Returns a state identical to the receptor, but in which the current player has drawn the tickets "drawnTickets" from the top of the pile, and chooses to keep those of "chosenTickets"
     *
     * @param drawnTickets  the tickets drawn from the top of the pile
     * @param chosenTickets the tickets picked by the player from the drawnTickets
     * @return a state identical to the receptor, but in which the current player has drawn the tickets "drawnTickets" from the top of the pile, and chooses to keep those of "chosenTickets"
     * @throws IllegalArgumentException if the list of tickets kept are not included in the drawnTickets
     */
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets) {
        Preconditions.checkArgument(drawnTickets.contains(chosenTickets));

        Map<PlayerId, PlayerState> newPlayerStateMap = newPlayerMap(completePlayerState.get(currentPlayerId()).withAddedTickets(chosenTickets));

        return new GameState(tickets.withoutTopCards(drawnTickets.size()), cardstate, currentPlayerId(), newPlayerStateMap, lastPlayer());
    }

    /**
     * Returns a state identical to the receptor except that the face-up card at the given location has been placed in the current player's hand, and replaced by the one at the top of the draw pile
     *
     * @param slot the slot index for the face-up card that should be replaced
     * @return a state identical to the receptor except that the face-up card at the given location has been placed in the current player's hand, and replaced by the one at the top of the draw pile
     * @throws IllegalArgumentException if it is not possible to draw cards, i.e. if canDrawCards returns false
     */
    public GameState withDrawnFaceUpCard(int slot) {
        Preconditions.checkArgument(this.canDrawCards());

        Map<PlayerId, PlayerState> newPlayerStateMap = newPlayerMap(completePlayerState.get(currentPlayerId()).withAddedCard(cardstate.faceUpCard(slot)));

        return new GameState(tickets, cardstate.withDrawnFaceUpCard(slot), currentPlayerId(), newPlayerStateMap, lastPlayer());
    }

    /**
     * Returns an identical state to the receiver except that the top card of the draw pile has been placed in the current player's hand
     *
     * @return an identical state to the receiver except that the top card of the draw pile has been placed in the current player's hand
     * @throws IllegalArgumentException if it is not possible to draw cards, i.e. if canDrawCards returns false
     */
    public GameState withBlindlyDrawnCard() {
        Preconditions.checkArgument(this.canDrawCards());

        Map<PlayerId, PlayerState> newPlayerStateMap = newPlayerMap(completePlayerState.get(currentPlayerId()).withAddedCard(cardstate.topDeckCard()));

        return new GameState(tickets, cardstate.withoutTopDeckCard(), currentPlayerId(), newPlayerStateMap, lastPlayer());
    }

    /**
     * Returns an identical state to the receiver but in which the current player has seized the given route by means of the given cards
     *
     * @param route the route that will be seized with the given cards
     * @param cards the cards that will be used to seize the given route
     * @return an identical state to the receiver but in which the current player has seized the given route by means of the given cards
     */
    public GameState withClaimedRoute(Route route, SortedBag<Card> cards) {
        Map<PlayerId, PlayerState> newPlayerStateMap = newPlayerMap(completePlayerState.get(currentPlayerId()).withClaimedRoute(route, cards));

        return new GameState(tickets, cardstate.withMoreDiscardedCards(cards), currentPlayerId(), newPlayerStateMap, lastPlayer());
    }


    //====== 3. ======//

    /**
     * Returns true IFF the last turn begins, i.e. if the identity of the last player is currently unknown but the current player has only two cars or less left. Should only be called at the end of a player's turn
     *
     * @return true IFF the last turn begins, i.e. if the identity of the last player is currently unknown but the current player has only two cars or less left. Should only be called at the end of a player's turn
     */
    public boolean lastTurnBegins() {
        boolean lastPlayerIdentityUnknown = lastPlayer() == null;
        boolean currentPlayerHasLessThanThreeCars = currentPlayerState().carCount() < 3;

        return (lastPlayerIdentityUnknown && currentPlayerHasLessThanThreeCars);
    }

    /**
     * Ends the current player's turn, i.e. returns an identical state to the receiver except that the current player is the one following the current currentPlayer.
     * Moreover, if lastTurnBegins returns true, the current currentPlayer becomes the last player
     *
     * @return an identical state to the receiver except that the current player is the one following the current currentPlayer. If (lastTurnBegins), current currentPlayer becomes the last player
     */
    public GameState forNextTurn() {
        return (lastTurnBegins())
                ? new GameState(tickets, cardstate, currentPlayerId().next(), completePlayerState, currentPlayerId())
                : new GameState(tickets, cardstate, currentPlayerId().next(), completePlayerState, lastPlayer());
    }

}
