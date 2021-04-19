package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;

import java.util.List;
import java.util.function.Function;

public interface Serde<E> {

    public String serialize(E yes);

    public String deserialize(E alsoYes);

    public static <T> Serde  of(Function<T, String> serFunction, Function<String, T> deserFunction){

        return null;
    }

    public static <T> Serde oneOf(List<T> listOfYes){
        return null;
    }

    public static <T> Serde listOf(Serde yes, char yesChar){
        return null;
    }

    public static <T extends Comparable<T>> Serde bagOf(Serde yes, char yesChar){
        return null;
    }

}
