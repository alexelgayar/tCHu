package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * @author Alexandre Iskandar (324406)
 * @author Anirudhh Ramesh (329806)
 * Public interface
 * Represents an object that can serialize or deserialize other objects
 */
public interface Serde<E> {

    public String serialize(E plainObject);

    public E deserialize(String serializedObject);

    public static <T> Serde<T> of(Function<T, String> serFun, Function<String, T> deserFun) {

        return new Serde<>() {
            @Override
            public String serialize(T plainObject) {
                return serFun.apply(plainObject);
            }

            @Override
            public T deserialize(String serializedObject) {
                return deserFun.apply(serializedObject);
            }
        };
    }

    public static <T> Serde<T> oneOf(List<T> list) {
        Function<T, String> serFun = t -> Integer.toString(list.indexOf(t));
        Function<String, T> deserFun = s -> list.get(Integer.parseInt(s));

        return Serde.of(serFun, deserFun);
    }

    public static <T> Serde<List<T>> listOf(Serde serde, String serializedObject) {

        return new Serde<List<T>>() {

            @Override
            public String serialize(List<T> list) {

                List<String> strings = new ArrayList<>();

                for (T t : list) {
                    strings.add(serde.serialize(t));
                }

                String s = String.join(serializedObject, strings);

                return s;
            }

            @Override
            public List<T> deserialize(String alsoYes) {

                String[] s = alsoYes.split(Pattern.quote(serializedObject), -1);
                List<T> tList = new ArrayList<>();

                for (int i = 0; i < s.length; ++i) {
                    tList.add((T) serde.deserialize(s[i]));
                }

                return tList;
            }
        };
    }


    public static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf(Serde serde, String serializedObject) {

        return new Serde<SortedBag<T>>() {

            @Override
            public String serialize(SortedBag<T> bag) {
                List list = bag.toList();
                String y = listOf(serde, serializedObject).serialize(list);
                return y;
            }

            @Override
            public SortedBag<T> deserialize(String alsoYes) {
                List list = Serde.listOf(serde, serializedObject).deserialize(alsoYes);
                return SortedBag.of(list);
            }
        };
    }
}




