package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.Random;

import static java.util.Collections.shuffle;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public, final, immutable class. Represents a deck of cards (any type of card)
 */
public final class Deck<C extends Comparable<C>> {

    private final List<C> cards;

    private Deck(List<C> cards) {
        this.cards = List.copyOf(cards);
    }

    /**
     * Returns a deck of cards made from the sortedBag of cards, mixed with a random number generator
     * @param cards the sortedBag of cards (any type) to store in a deck
     * @param rng the random number generator to shuffle the cards
     * @param <C> the type of card to store in the deck
     * @return a deck of cards made from the sortedBag of cards, mixed with a random number generator
     */
    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng) {
        List<C> copy = cards.toList();
        shuffle(copy, rng);

        return new Deck<>(copy);
    }

    /**
     * Returns the size of the deck
     * @return the size of deck
     */
    public int size() {
        return cards.size();
    }

    /**
     * Returns true iff the Deck is empty
     * @return true iff the Deck is empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * Returns the top card of the deck
     * @return the top card of the deck
     * @throws IllegalArgumentException if the deck is empty
     */
    public C topCard() {
        Preconditions.checkArgument(!isEmpty());
        return cards.get(size() - 1);
    }

    /**
     * Returns a new deck similar to (this), but without the top card
     * @return a new deck similar to (this), but without the top card
     * @throws IllegalArgumentException if the deck is empty
     */
    public Deck<C> withoutTopCard() {
        Preconditions.checkArgument(!isEmpty());
        List<C> copy = cards.subList(0, cards.size() - 1);

        return new Deck<>(copy);
    }

    /**
     * Returns a SortedBag containing the "count" cards at the top of the deck
     * @param count number of cards to be returned from the top of the deck
     * @return a SortedBag containing the "count" cards at the top of the deck
     * @throws IllegalArgumentException if the count is not between 0 (inclusive) and the size of the deck (inclusive)
     */
    public SortedBag<C> topCards(int count) {
        Preconditions.checkArgument(0 <= count && count <= size());
        List<C> copy = cards.subList(size() - count, size());

        return SortedBag.of(copy);
    }

    /**
     * Returns a deck identical to (this), but without the "count" cards from the top of the deck
     * @param count number of cards to be removed from the top of the deck
     * @return a deck identical to (this), but without the "count" cards from the top of the deck
     * @throws IllegalArgumentException if the count is not between 0 (inclusive) and the size of the deck (inclusive)
     */
    public Deck<C> withoutTopCards(int count) {
        Preconditions.checkArgument(0 <= count && count <= size());
        List<C> copy = cards.subList(0, this.size() - count);

        return new Deck<>(copy);
    }


}
