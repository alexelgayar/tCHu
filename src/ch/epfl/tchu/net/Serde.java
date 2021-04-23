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

    public static <T> Serde of(Function<T, String> serFun, Function<String, T> deserFun) {

        return new Serde<T>() {
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

    public static <T> Serde oneOf(List<T> list) {

        Function<T, String> serFun = t -> Integer.toString(list.indexOf(t));
        Function<String, T> deserFun = s -> list.get(Integer.parseInt(s));

        return Serde.of(serFun, deserFun);

    }

    public static <T> Serde<List<T>> listOf(Serde yes, String yesChar) {

        return new Serde<List<T>>() {

            @Override
            public String serialize(List<T> plainObject) {

                List<String> strings = new ArrayList<>();

                for (T t : plainObject) {
                    strings.add(yes.serialize(t));
                }

                String s = String.join(yesChar, strings);

                return s;
            }

            @Override
            public List<T> deserialize(String serializedObject) {

                String[] s = serializedObject.split(Pattern.quote(yesChar), -1);
                List<T> tList = new ArrayList<>();

                for (int i = 0; i < s.length; ++i) {
                    tList.add((T) yes.deserialize(s[i]));
                }

                return tList;
            }
        };
    }

    public static <T extends Comparable<T>> Serde<SortedBag<T>> bagOf(Serde yes, String yesChar) {

        return new Serde<SortedBag<T>>() {

            @Override
            public String serialize(SortedBag<T> plainObject) {
                List list = plainObject.toList();
                String y = listOf(yes, yesChar).serialize(list);
                return y;
            }

            @Override
            public SortedBag<T> deserialize(String serializedObject) {
                List list = Serde.listOf(yes, yesChar).deserialize(serializedObject);
                return SortedBag.of(list);
            }
        };
    }
}




