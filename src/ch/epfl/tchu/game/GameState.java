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
 * Represents the state of a game of tCHu
 * Inherits from PublicGameState, does not offer public constructor but public, static construction method
 * Public, final and immutable
 */
public final class GameState extends PublicGameState{
    private static final int TOTAL_PLAYERS = 2;

    //TODO: Are these correct attributes + constructor
    private final Deck<Ticket> tickets;
    private final CardState cardstate;
    private final Map<PlayerId, PlayerState> completePlayerState; //Complete player state

    private GameState(Deck<Ticket> tickets, CardState cardState, PlayerId currentPlayerId, Map<PlayerId, PlayerState> playerState, PlayerId lastPlayerId){
        super(tickets.size(), cardState, currentPlayerId, Map.copyOf(playerState), lastPlayerId);

        this.tickets = tickets;
        this.cardstate = cardState;
        this.completePlayerState = playerState;
    }

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
        Deck<Ticket> ticketDeck = Deck.of(SortedBag.of(tickets), rng);
        Deck<Card> cardDeck = Deck.of(SortedBag.of(Constants.ALL_CARDS), rng);

        //TODO: Fix code? It's all hardcoded :/
        //Boucle sur PlayerId.all
        //Save CardDeck new value, in iteratrion

        PlayerId firstPlayer = rng.nextInt(TOTAL_PLAYERS) == 0 ? PLAYER_1 : PLAYER_2;
        PlayerState firstPlayerState = PlayerState.initial(cardDeck.topCards(INITIAL_CARDS_COUNT));

        PlayerId secondPlayer = firstPlayer == PLAYER_1 ? PLAYER_2 : PLAYER_1;
        PlayerState secondPlayerState = PlayerState.initial(cardDeck.withoutTopCards(INITIAL_CARDS_COUNT).topCards(INITIAL_CARDS_COUNT));

        CardState remainingCardState = CardState.of(cardDeck.withoutTopCards(TOTAL_PLAYERS * INITIAL_CARDS_COUNT));

        Map<PlayerId, PlayerState> playerStateMap = new EnumMap<>(PlayerId.class);
        playerStateMap.put(firstPlayer, firstPlayerState);
        playerStateMap.put(secondPlayer, secondPlayerState);

        return new GameState(ticketDeck, remainingCardState, firstPlayer, playerStateMap, secondPlayer);
    }

    /**
     * Method which redefines the method of same name of the PublicGameState class, to return the complete state of the given player identity, not only the public part
     * @param playerId id of the player to return the public player state of
     * @return returns the complete state of the given player identity, not only the public part
     */
    public PlayerState playerState(PlayerId playerId){

        return completePlayerState.get(playerId);
    }

    /**
     * Method which redefine the method of the same name from PublicGameState, to return the complete state of the current player, and not only the public part
     * @return returns the complete state of the current player, and not only the public part
     */
    public PlayerState currentPlayerState(){

        return playerState(currentPlayerId());
    }

    /**
     * Method which returns the "count" tickets from the top of the pile
     * @param count the number of tickets to return from the top of the pile
     * @return returns "count" tickets from the top of the pile
     * @throws IllegalArgumentException if the count is not between 0 and the size of the pile (inclusive)
     */
    public SortedBag<Ticket> topTickets(int count){
        Preconditions.checkArgument(count >= 0 && count <= tickets.size());
        return SortedBag.of(); //TODO: Correct
    }

    /**
     * Method which returns a state identical to the receptor, but without the "count" tickets from the top of the pile
     * @param count the number of tickets to remove from the top of the pile
     * @return returns a state identical to the receptor, but without the "count" tickets from the top of the pile
     * @throws IllegalArgumentException if the count is not between 0 and the size of the pile (inclusive)
     */
    public GameState withoutTopTickets(int count){
        Preconditions.checkArgument(count >= 0 && count <= tickets.size());

        return new GameState(tickets.withoutTopCards(count), cardstate, currentPlayerId(), completePlayerState, lastPlayer());
    }

    /**
     * Method which returns the card at the top of the pile
     * @return returns the card at the top of the pile
     * @throws IllegalArgumentException if the pile is empty
     */
    public Card topCard(){
        Preconditions.checkArgument(!cardState().isDeckEmpty());

        return cardstate.topDeckCard();
    }

    /**
     * Method which returns a state identical to the receptor but without the card at the top of the deck
     * @return returns a state identical to the receptor but without the card at the top of the deck
     * @throws IllegalArgumentException if the pile is empty
     */
    public GameState withoutTopCard(){
        Preconditions.checkArgument(!cardState().isDeckEmpty());

        return new GameState(tickets, cardstate.withoutTopDeckCard(), currentPlayerId(), completePlayerState, lastPlayer());
    }

    /**
     * Method which returns a state identical to the receptor but with the given cards added to the discard pile
     * @param discardedCards cards that must be added to the discards pile
     * @return returns a state identical to the receptor but with the given cards added to the discard pile
     */
    public GameState withMoreDiscardedCards(SortedBag<Card> discardedCards){
        return new GameState(tickets, cardstate.withMoreDiscardedCards(discardedCards), currentPlayerId(), completePlayerState, lastPlayer());
    }

    /**
     * Method which returns a state identical to the receptor except that if the pile of cards is empty, the pile is recreated from the discards, mixed using a random number generator
     * @param rng the random number generator provided to mix the cards
     * @return returns a state identical to the receptor except that if the pile of cards is empty, the pile is recreated from the discards, mixed using a random number generator
     */
    public GameState withCardsDeckRecreatedIfNeeded(Random rng){
        return (cardstate.isDeckEmpty())
                ? new GameState(tickets, cardstate.withDeckRecreatedFromDiscards(rng), currentPlayerId(), completePlayerState, lastPlayer())
                : new GameState(tickets, cardstate, currentPlayerId(), completePlayerState, lastPlayer());
    }


    //====== 2. ======//

    /**
     * Method which returns an identical state to the receiver but in which the given tickets have been added to the given player's hand
     * @param playerId the player to whom the tickets should be added to
     * @param chosenTickets the tickets that must be added to the given player
     * @return returns an identical state to the receiver but in which the given tickets have been added to the given player's hand
     * @throws IllegalArgumentException if the currentPlayer already owns at least one ticket
     */
    public GameState withInitiallyChosenTickets(PlayerId playerId, SortedBag<Ticket> chosenTickets){
        //Tickets already remvoed from pile
        Preconditions.checkArgument(completePlayerState.get(playerId).tickets().isEmpty());

        Map<PlayerId, PlayerState> newPlayerStateMap = new EnumMap<>(completePlayerState);

        newPlayerStateMap.put(playerId, completePlayerState.get(playerId).withAddedTickets(chosenTickets));

        return new GameState(tickets, cardstate, currentPlayerId(), newPlayerStateMap, lastPlayer());
    }

    /**
     * Method which returns a state identical to the receptor, but in which the current player has drawn the tickets "drawnTickets" from the top of the pile, and chooses to keep those of "chosenTickets"
     * @param drawnTickets the tickets drawn from the top of the pile
     * @param chosenTickets the tickets picked by the player from the drawnTickets
     * @return returns a state identical to the receptor, but in which the current player has drawn the tickets "drawnTickets" from the top of the pile, and chooses to keep those of "chosenTickets"
     * @throws IllegalArgumentException if the list of tickets kept are not included in the drawnTickets
     */
    public GameState withChosenAdditionalTickets(SortedBag<Ticket> drawnTickets, SortedBag<Ticket> chosenTickets){
        //drawnTickets not removed from pile
        Preconditions.checkArgument(drawnTickets.contains(chosenTickets));
        //Delete drawtickets.size from pile
        //Add chosenTickets to player

        //TODO: RETURN METHOD IS WRONG, WE can't use another method as return (to different outcomes)
        return withInitiallyChosenTickets(currentPlayerId(), chosenTickets);
    }

    /**
     * Method which returns a state identical to the receptor except that the face-up card at the given location has been placed in the current player's hand, and replaced by the one at the top of the draw pile
     * @param slot the slot index for the face-up card that should be replaced
     * @return returns a state identical to the receptor except that the face-up card at the given location has been placed in the current player's hand, and replaced by the one at the top of the draw pile
     * @throws IllegalArgumentException if it is not possible to draw cards, i.e. if canDrawCards returns false
     */
    public GameState WithDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(this.canDrawCards());
        CardState newCardState = cardstate.withDrawnFaceUpCard(slot);
        return new GameState(tickets, newCardState, currentPlayerId(), completePlayerState, lastPlayer());
    }









}
