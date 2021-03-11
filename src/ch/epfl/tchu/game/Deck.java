package ch.epfl.tchu.game;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.util.Collections.shuffle;

/**
 * @Author Alexandre Iskandar (324406)
 * @Author Anirudhh Ramesh (329806)
 * Public, final, immutable class
 */
public final class Deck<C extends Comparable<C>> {

    private final List<C> cards;

    public Deck(List<C> cards) {

        this.cards = List.copyOf(cards);

    }

    public static <C extends Comparable<C>> Deck<C> of(SortedBag<C> cards, Random rng) {

        List<C> copy = new ArrayList<C>(cards.toList());

        shuffle(copy, rng);

        List<C> shuffledCards = List.copyOf(copy);

        return new Deck<C>(shuffledCards);
    }

    /**
     * @return size of deck
     */
    public int size() {

        return cards.size();

    }

    /**
     * @return true iff Deck is empty
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }

    /**
     * @return top card of deck
     * Throws IllegalArgumentException if the deck is empty
     */
    public C topCard() {
        Preconditions.checkArgument(!cards.isEmpty() && cards != null);
        return cards.get(size() - 1);
    }

    /**
     * @return a new Deck similar to this but without the top card
     */
    public Deck<C> withoutTopCard() {

        Preconditions.checkArgument(!cards.isEmpty() && cards != null);

        List<C> copie = List.copyOf(cards.subList(0, this.size() - 1));

        return new Deck<C>(copie);
    }

    /**
     * @param count number of cards to be selected
     * @return the top count cards of this
     */
    public SortedBag<C> topCards(int count) {

        Preconditions.checkArgument(0 <= count && count <= this.size());

        List<C> copie = List.copyOf(cards.subList(this.size() - count, this.size()));

        SortedBag<C> cardsBag = SortedBag.of(copie);

        return cardsBag;
    }

    /**
     * @param count number of cards to be removed
     * @return a new Deck similar to this but without the last count cards
     */
    public Deck<C> withoutTopCards(int count) {

        Preconditions.checkArgument(0 <= count && count <= this.size());

        List<C> copy = List.copyOf(cards.subList(0, this.size() - count));

        return new Deck<C>(copy);

    }


}
