package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 * Public, final, Immutable
 * Represents (the private part of) the state of the wagon/locomotive cards that are not in the hands of the player:
 * It inherits from PublicCardState and adds to it the private elements of the state, as well as the corresponding methods
 */
public final class CardState extends PublicCardState{

    private final List<Card> faceUpCards;
    private final Deck<Card> drawPile;
    private final SortedBag<Card> discardsPile;

    //TODO: Is this constructor correct?
    private CardState(List<Card> faceUpCards, Deck<Card> drawPile, SortedBag<Card> discardsPile){
        super(faceUpCards, drawPile.size(), discardsPile.size());
        this.faceUpCards = faceUpCards;
        this.drawPile = drawPile;
        this.discardsPile = discardsPile;
    }

    //TODO: Unsure if this is what the instructions have asked me to do
    /**
     * Class does not provide a public constructor, but instead this public static construction method
     * This construction method returns the state in which the 5 face-up cards
     * @param deck
     * @return
     * @throws
     */
    public static CardState of(Deck<Card> deck){
        Preconditions.checkArgument(deck.size() >= 5);
        /*
        Returns a CardState in which:
        - 5 cards placed face up are first 5 of given pile
        - draw pile consists of remeaining cards of the pile
        - discard pile is empty
        Throws illegalArgumentExcpetion if the given pile contains less than 5 cards
         */
        //TODO: Unsure what instructions want me to do
        List<Card> faceUpCards = deck.topCards(5).toList();
        Deck<Card> drawPile = deck.withoutTopCards(5);

        //TODO: Is this how we create a discard Pile with size 0?
        SortedBag<Card> discardsPile = SortedBag.of();

        return new CardState(faceUpCards, drawPile, discardsPile);
    }

    /**
     * Turns over a set of identical cards to the receiver(this) except the card at index "slot" has been replaced by the card at the top of the draw pile, which is also removed
     * @param slot int index of the face-up card
     * @return Returns a new card state with a face-up cards (card at index is replaced with card from top of deck) and a deck without the top card
     * @throws IndexOutOfBoundsException if the given index is not between 0 (inclusive) and 5(excluded)
     * @throws IllegalArgumentException if the draw pile is empty
     */
    public CardState withDrawnFaceUpCard(int slot){
        Preconditions.checkArgument(!drawPile.isEmpty());
        //TODO: Is this good Object-Oriented Programming?
        faceUpCards.set(Objects.checkIndex(slot, 5), drawPile.topCard());

        //TODO: Check this, Remove the topcard from the drawPile
        drawPile.withoutTopCard();

        faceUpCard(Objects.checkIndex(slot, 5));

        return new CardState(faceUpCards, drawPile.withoutTopCard(), discardsPile);
    }

    /**
     * Turns over the card at the top of the draw pile
     * @return returns the card at the top of the draw pile
     * @throws IllegalArgumentException if the draw pile is empty
     */
    public Card topDeckCard(){
        Preconditions.checkArgument(!drawPile.isEmpty());

        return drawPile.topCard();
    }

    /**
     * Method returns a set of cards identical to the receiver(this) but without the card at the top of the deck
     * @return set of cards identical to the reciever(this) but without the card at the top of the deck
     */
    public CardState withoutTopDeckCard(){
        return new CardState(faceUpCards, drawPile.withoutTopCard(), discardsPile);
    }

    /**
     * Returns a set of identical cards to the receiver (this), except that the cards from the discard pile have been shuffled by means of the given random generator and forms new drawPile
     * In order to constitute the new draw pile
     * @param rng Randomly generated number used to shuffle the cards
     * @return A set of identical cards to the receiver(this) except that the cards from the discard pile have been shuffled by means of the given random generator and forms new drawPile
     * @throws IllegalArgumentException uf the receiver's deck is not empty
     */
    public CardState withDeckRecreatedFromDiscards(Random rng){
        Preconditions.checkArgument(drawPile.isEmpty());

        Deck<Card> newDrawPile = Deck.of(SortedBag.of(discardsPile), rng);

        return new CardState(faceUpCards, newDrawPile, discardsPile.difference(discardsPile));
    }

    /**
     * Method returns a set of cards identical to the receiver (this) but with the given cards added to the discard pile
     * @param additionalDiscards
     * @return a new cardstate with set of cards identical to the receiver(this) but with the given cards added to the discard pile
     */
    public CardState withMoreDiscardedCards(SortedBag<Card> additionalDiscards){
        return new CardState(faceUpCards, drawPile.withoutTopCards(additionalDiscards.size()), discardsPile.union(additionalDiscards));
    }
}
