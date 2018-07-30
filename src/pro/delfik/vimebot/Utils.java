package pro.delfik.vimebot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public final class Utils {

    public static JSONObject[] toJavaArray(JSONArray jsonArray) {
        JSONObject[] array = new JSONObject[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                array[i] = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                array[i] = null;
            }
        }
        return array;
    }

    public static <From, To> Collection<To> transform(Iterable<From> from, Function<From, To> converter) {
        List<To> result = new ArrayList<>();
        for (From f : from) result.add(converter.apply(f));
        return result;
    }

    public static List<JSONObject> toJavaList(JSONArray members) {
        List<JSONObject> list = new ArrayList<>();
        for (int pos = 0; pos < members.length(); pos++) {
            try {
                list.add(members.getJSONObject(pos));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static class ArrayIterable<T> implements Iterable<T> {
        private final T[] array;

        public ArrayIterable(T[] array) {
            this.array = array;
        }

        @Override
        public Iterator<T> iterator() {
            return new ArrayIterator<T>(array);
        }
    }
    public static class ArrayIterator<T> implements Iterator<T> {
        @Override
        public boolean hasNext() {
            return position < array.length - 1;
        }

        @Override
        public T next() {
            return array[position++];
        }

        private final T[] array;
        private int position = 0;

        ArrayIterator(T[] array) {
            this.array = array;
        }
    }
}
