package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, final, Immutable
 * Represents (the private part of) the state of the wagon/locomotive cards that are not in the hands of the player:
 * It inherits from PublicCardState and adds to it the private elements of the state, as well as the corresponding methods
 */
public final class CardState extends PublicCardState{

    private final Deck<Card> drawPile;
    private final SortedBag<Card> discardsPile;

    private CardState(List<Card> faceUpCards, Deck<Card> drawPile, SortedBag<Card> discardsPile){
        super(faceUpCards, drawPile.size(), discardsPile.size());
        this.drawPile = drawPile;
        this.discardsPile = discardsPile;
    }

    /**
     * Returns a cardState in which the 5 face-up cards are first 5 of given deck, the drawPile is the remaining cards and the discardsPile is empty
     * @param deck The deck inputted for the game
     * @return a cardState of which the 5 face-up cards are first 5 of given deck, the drawPile is the remaining cards and the discardsPile is empty
     * @throws IllegalArgumentException if the deck given contains less than 5 cards
     */
    public static CardState of(Deck<Card> deck){
        Preconditions.checkArgument(deck.size() >= 5);

        List<Card> faceUpCards = deck.topCards(5).toList();
        Deck<Card> drawPile = deck.withoutTopCards(5);
        SortedBag<Card> discardsPile = SortedBag.of();

        return new CardState(faceUpCards, drawPile, discardsPile);
    }

    /**
     * Returns a cardState identical to the receiver(this) except the card at index "slot" has been replaced by the card at the top of the draw pile, which is also removed
     * @param slot int index of the face-up card
     * @return a cardState identical to the receiver(this) except the card at index "slot" has been replaced by the card at the top of the draw pile, which is also removed
     * @throws IndexOutOfBoundsException if the given index is not between 0 (inclusive) and 5(excluded)
     * @throws IllegalArgumentException if the draw pile is empty
     */
    public CardState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(!drawPile.isEmpty());

        List<Card> faceUpCardsCopy = new ArrayList<>(faceUpCards());
        faceUpCardsCopy.set(Objects.checkIndex(slot, 5), drawPile.topCard());

        return new CardState(faceUpCardsCopy, drawPile.withoutTopCard(), discardsPile);
    }

    /**
     * Returns the card at the top of the draw pile
     * @return the card at the top of the draw pile
     * @throws IllegalArgumentException if the draw pile is empty
     */
    public Card topDeckCard(){
        Preconditions.checkArgument(!drawPile.isEmpty());
        return drawPile.topCard();
    }

    /**
     * Returns a cardState identical to the receiver(this) but without the card at the top of the deck
     * @return a cardState identical to the receiver(this) but without the card at the top of the deck
     * @throws IllegalArgumentException if the drawPile is empty
     */
    public CardState withoutTopDeckCard(){
        Preconditions.checkArgument(!drawPile.isEmpty());
        return new CardState(faceUpCards(), drawPile.withoutTopCard(), discardsPile);
    }

    /**
     * Returns a cardState identical to the receiver (this), except that the discardsPile is shuffled using random number generator rng, and used as the new drawPile
     * @param rng Randomly generated number used to shuffle the cards
     * @return a set of identical cards to the receiver (this), except that the discardsPile is shuffled using random number generator rng, and used as the new drawPile
     * @throws IllegalArgumentException if the receiver's deck is not empty
     */
    public CardState withDeckRecreatedFromDiscards(Random rng){
        Preconditions.checkArgument(drawPile.isEmpty());

        Deck<Card> newDrawPile = Deck.of(SortedBag.of(discardsPile), rng);
        SortedBag<Card> emptyDiscardsPile = SortedBag.of();

        return new CardState(faceUpCards(), newDrawPile, emptyDiscardsPile);
    }

    /**
     * Returns a cardState identical to the receiver (this) but with the given cards added to the discard pile
     * @param additionalDiscards the discard cards that should be added to the discardsPile
     * @return a cardState identical to the receiver (this) but with the given cards added to the discard pile
     */
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){
        return new CardState(faceUpCards(), drawPile, discardsPile.union(additionalDiscards));
    }
}
