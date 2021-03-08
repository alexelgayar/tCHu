package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 * Public, Immutable class
 * Represents (the public part of) the state of the wagon/locomotive cards that are not in the hands of the player:
 * - the 5 cards placed face up next to the board; the deck; the discard pile;
 */
public class PublicCardState {

    private final List<Card> faceUpCards;
    private final int deckSize;
    private final int discardsSize;

    /**
     * Constructor for the PublicCardSate class
     * @param faceUpCards are the visible cards given
     * @param deckSize is the size of the deck pile
     * @param discardsSize is the size of the discard pile
     * @throws IllegalArgumentException if faceUpCards != 5, or when deckSize and/or discardsSize is less than 0
     */
    public PublicCardState(List<Card> faceUpCards, int deckSize, int discardsSize){
        Preconditions.checkArgument(faceUpCards.size() == 5 && deckSize >= 0 && discardsSize >= 0);

        this.faceUpCards = faceUpCards;
        this.deckSize = deckSize;
        this.discardsSize = discardsSize;
    }

    /**
     * Returns the total number of cards that are not in the players' hand
     * Namely, the 5 whose face is up, those from the draw pile and from the discard pile
     * @return
     */
    public int totalSize(){
        return faceUpCards.size() + deckSize + discardsSize;
    }

    /**
     * Turns the 5 cards face-up, in the form of a list comprising exactly 5 elements
     * @return returns the 5-element list of the 5 cards that are face-up
     */
    public List<Card> faceUpCards(){
        return faceUpCards;
    }

    /**
     * Turns the card face up at the given index or throws IndexOutOfBoundsException if this index is not between 0 (inclusive) and 5 (excluded)
     * @param slot
     * @return the card at the given "slot" index
     * @throws IndexOutOfBoundsException if the given "slot" index is not between 0 (inclusive) and 5 (exclusive)
     */
    public Card faceUpCard(int slot){
        return faceUpCards.get(Objects.checkIndex(slot, 5));
    }

    /**
     * Returns the size of the deck
     * @return returns the deck size
     */
    public int deckSize(){
        return deckSize;
    }

    /**
     * Method which returns true if the decksize is empty
     * @return true iff deckSize = 0
     */
    public boolean isDeckEmpty(){
        return (deckSize == 0);
    }

    public int discardsSize(){
        return discardsSize;
    }
}
