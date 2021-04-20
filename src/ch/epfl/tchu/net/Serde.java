package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public interface Serde<E> {

    public String serialize(E yes);

    public E deserialize(String alsoYes);

    public static <T> Serde of(Function<T, String> serFun, Function<String, T> deserFun) {

        return new Serde<T>() {

            @Override
            public String serialize(T t) {
                return serFun.apply(t);
            }

            @Override
            public T deserialize(String t) {

                return deserFun.apply(t);
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
            public String serialize(List<T> list) {

                List<String> strings = new ArrayList<>();

                for (T t : list) {
                    strings.add(yes.serialize(t));
                }

                String s = String.join(yesChar, strings);

                return s;
            }

            @Override
            public List<T> deserialize(String alsoYes) {

                String[] s = alsoYes.split(Pattern.quote(yesChar), -1);
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
            public String serialize(SortedBag<T> bag) {
                List list = bag.toList();
                String y = listOf(yes, yesChar).serialize(list);
                return y;
            }

            @Override
            public SortedBag<T> deserialize(String alsoYes) {
                List list = Serde.listOf(yes, yesChar).deserialize(alsoYes);
                return SortedBag.of(list);
            }
        };
    }
}




